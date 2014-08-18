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

package com.wideplay.warp.persist.internal;

import net.jcip.annotations.Immutable;
import net.sf.cglib.proxy.InvocationHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * <p>
 *
 * This is a simple adapter to convert a JDK dynamic proxy invocation into an aopalliance invocation.
 * Mainly for supporting {@code @Finder} on interface methods.
 * </p>
 *
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 * @since 1.0
 */
@Immutable
public class AopAllianceJdkProxyAdapter implements InvocationHandler {
    private final MethodInterceptor interceptor;

    public AopAllianceJdkProxyAdapter(MethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public Object invoke(final Object object, final Method method, final Object[] objects) throws Throwable {
        return interceptor.invoke(new MethodInvocation() {

            public Method getMethod() {
                return method;
            }

            public Object[] getArguments() {
                return objects;
            }

            public Object proceed() throws Throwable {
                return method.invoke(object,  objects);
            }

            public Object getThis() {
                return object;
            }

            public AccessibleObject getStaticPart() {
                return method;
            }
        });
    }
}
