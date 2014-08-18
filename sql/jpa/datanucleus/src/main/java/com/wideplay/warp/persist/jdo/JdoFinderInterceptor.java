/****************************************************************
 * Licensed to the AOS Community (AOS) under one or more        *
 * contributor license agreements.  See the NOTICE file         *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The AOS licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package com.wideplay.warp.persist.jdo;

import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.wideplay.warp.persist.dao.*;
import org.aopalliance.intercept.*;

import javax.jdo.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Miroslav Genov
 */
class JdoFinderInterceptor implements MethodInterceptor {
    private final Map<Method, FinderDescriptor> finderCache = new ConcurrentHashMap<Method, FinderDescriptor>();
    private final Provider<PersistenceManager> emProvider;

    public JdoFinderInterceptor(Provider<PersistenceManager> emProvider) {
        this.emProvider = emProvider;
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        PersistenceManager em = emProvider.get();

        //obtain a cached finder descriptor (or create a new one)
        JdoFinderInterceptor.FinderDescriptor finderDescriptor = getFinderDescriptor(methodInvocation);

        Object result = null;

        //execute as query (named params or otherwise)
        Query jdoQuery = finderDescriptor.createQuery(em);
        if (finderDescriptor.isBindAsRawParameters)
            bindQueryRawParameters(jdoQuery, finderDescriptor, methodInvocation.getArguments());
        else
            bindQueryNamedParameters(jdoQuery, finderDescriptor, methodInvocation.getArguments());


        //depending upon return type, decorate or return the result as is
        if (JdoFinderInterceptor.ReturnType.PLAIN.equals(finderDescriptor.returnType)) {
            result = jdoQuery.execute();
        } else if (JdoFinderInterceptor.ReturnType.COLLECTION.equals(finderDescriptor.returnType)) {
            result = getAsCollection(finderDescriptor, (List) jdoQuery.execute());
        } else if (JdoFinderInterceptor.ReturnType.ARRAY.equals(finderDescriptor.returnType)) {
            result = ((List)jdoQuery.execute()).toArray();
        }

        return result;
    }

    private Object getAsCollection(JdoFinderInterceptor.FinderDescriptor finderDescriptor, List results) {
        Collection<?> collection;
        try {
            collection = (Collection) finderDescriptor.returnCollectionTypeConstructor.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Specified collection class of Finder's returnAs could not be instantated: "
                + finderDescriptor.returnCollectionType, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Specified collection class of Finder's returnAs could not be instantated (do not have access privileges): "
                + finderDescriptor.returnCollectionType, e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Specified collection class of Finder's returnAs could not be instantated (it threw an exception): "
                + finderDescriptor.returnCollectionType, e);
        }

        collection.addAll(results);
        return collection;
    }

    private void bindQueryNamedParameters(Query jdoQuery, JdoFinderInterceptor.FinderDescriptor descriptor, Object[] arguments) {
        for (int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            Object annotation = descriptor.parameterAnnotations[i];

//          //TODO: Fix this to use the jdo's query methods
//            if (null == annotation)
//                //noinspection UnnecessaryContinue
//                continue;   //skip param as it's not bindable
//            else if (annotation instanceof Named) {
//                Named named = (Named)annotation;
//
//                jdoQuery.setParameter(named.value(), argument);
//            } else if (annotation instanceof FirstResult)
//                jdoQuery.setFirstResult((Integer)argument);
//              else if (annotation instanceof MaxResults)
//                jdoQuery.setMaxResults((Integer)argument);
        }
    }

    private void bindQueryRawParameters(Query jdoQuery, JdoFinderInterceptor.FinderDescriptor descriptor, Object[] arguments) {

      boolean hasOffset = false;
      boolean hasMaxResults = false;
      Integer offset = null;
      Integer maxResults = null;

      for (int i = 0; i < arguments.length; i++) {
        Object argument = arguments[i];
        Object annotation = descriptor.parameterAnnotations[i];
        if (null == annotation) {
          //bind it as a raw param (1-based index, yes I know its different from Hibernate, blargh)
          String paramType = null;
          if (argument instanceof String) {
            paramType = "String";
          } else if (argument instanceof Integer) {
            paramType = "Integer";
          } else if (argument instanceof Long) {
            paramType = "Long";
          } else if (argument instanceof Date) {
            paramType = "Date";
          } else if (argument instanceof Double) {
            paramType = "Double";
          }

          if (paramType != null) {
            jdoQuery.declareParameters(paramType + descriptor.name);
          }

        } else if (annotation instanceof FirstResult) {
          hasOffset = true;
          offset = (Integer) argument;
        } else if (annotation instanceof MaxResults) {
          hasMaxResults = true;
          maxResults = (Integer) argument;
        }
      }

      if (hasOffset && hasMaxResults) {

        jdoQuery.setRange(new Long(offset), new Long(maxResults));
      }
    }

    private JdoFinderInterceptor.FinderDescriptor getFinderDescriptor(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        JdoFinderInterceptor.FinderDescriptor finderDescriptor = finderCache.get(method);
        if (null != finderDescriptor)
            return finderDescriptor;

        //otherwise reflect and cache finder info...
        finderDescriptor = new JdoFinderInterceptor.FinderDescriptor();

        //determine return type
        finderDescriptor.returnClass = invocation.getMethod().getReturnType();
        finderDescriptor.returnType = determineReturnType(finderDescriptor.returnClass);

        //determine finder query characteristics
        Finder finder = invocation.getMethod().getAnnotation(Finder.class);
        String query = finder.query();
        if (!"".equals(query.trim()))
            finderDescriptor.setQuery(query);
        else
            finderDescriptor.setNamedQuery(finder.namedQuery());

        //determine parameter annotations
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] discoveredAnnotations = new Object[parameterAnnotations.length];
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            //each annotation per param
            for (Annotation annotation : annotations) {
                //discover the named, first or max annotations then break out
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (Named.class.equals(annotationType)) {
                    discoveredAnnotations[i] = annotation;
                    finderDescriptor.isBindAsRawParameters = false;
                    break;
                } else if (FirstResult.class.equals(annotationType)) {
                    discoveredAnnotations[i] = annotation;
                    break;
                } else if (MaxResults.class.equals(annotationType)) {
                    discoveredAnnotations[i] = annotation;
                    break;
                }   //leave as null for no binding
            }
        }

        //set the discovered set to our finder cache object
        finderDescriptor.parameterAnnotations = discoveredAnnotations;

        //discover the returned collection implementation if this finder returns a collection
        if (JdoFinderInterceptor.ReturnType.COLLECTION.equals(finderDescriptor.returnType)) {
            finderDescriptor.returnCollectionType = finder.returnAs();
            try {
                finderDescriptor.returnCollectionTypeConstructor = finderDescriptor.returnCollectionType.getConstructor();
                finderDescriptor.returnCollectionTypeConstructor.setAccessible(true);   //UGH!
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Finder's collection return type specified has no default constructor! returnAs: "
                        + finderDescriptor.returnCollectionType, e);
            }
        }

        //cache it
        cacheFinderDescriptor(method, finderDescriptor);

        return finderDescriptor;
    }

    /**
     * writes to a chm (used to provide copy-on-write but this is bettah!)
     *
     * @param method The key
     * @param finderDescriptor The descriptor to cache
     */
    private void cacheFinderDescriptor(Method method, FinderDescriptor finderDescriptor) {
        //write to concurrent map
        finderCache.put(method, finderDescriptor);
    }

    private JdoFinderInterceptor.ReturnType determineReturnType(Class<?> returnClass) {
        if (Collection.class.isAssignableFrom(returnClass)) {
            return JdoFinderInterceptor.ReturnType.COLLECTION;
        } else if (returnClass.isArray()) {
            return JdoFinderInterceptor.ReturnType.ARRAY;
        }

        return JdoFinderInterceptor.ReturnType.PLAIN;
    }

    /**
     * A wrapper data class that caches information about a finder method.
     */
    private static class FinderDescriptor {
        private volatile boolean isKeyedQuery = false;
        volatile boolean isBindAsRawParameters = true;   //should we treat the query as having ? instead of :named params
        volatile JdoFinderInterceptor.ReturnType returnType;
        volatile Class<?> returnClass;
        volatile Class<? extends Collection> returnCollectionType;
        volatile Constructor returnCollectionTypeConstructor;
        volatile Object[] parameterAnnotations;  //contract is: null = no bind, @Named = param, @FirstResult/@MaxResults for paging

        private String query;
        private String name;

        void setQuery(String query) {
            this.query = query;
        }

        void setNamedQuery(String name) {
            this.name = name;
            isKeyedQuery = true;
        }

        public boolean isKeyedQuery() {
            return isKeyedQuery;
        }

        Query createQuery(PersistenceManager em) {
            return isKeyedQuery ? em.newNamedQuery(returnClass,name) : em.newQuery(query);
        }
    }

    private static enum ReturnType { PLAIN, COLLECTION, ARRAY }
}
