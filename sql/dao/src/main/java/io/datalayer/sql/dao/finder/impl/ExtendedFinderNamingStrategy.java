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
package io.datalayer.sql.dao.finder.impl;

import io.datalayer.sql.dao.finder.FinderNamingStrategy;

import java.lang.reflect.Method;

/**
 * Looks up Hibernate named queries based on the simple name of the invoced
 * class and the method name of the invocation
 */
public class ExtendedFinderNamingStrategy implements FinderNamingStrategy {
    // Always look for queries that start with findBy (even if method is
    // iterateBy.. or scrollBy...)
    public String queryNameFromMethod(Class findTargetType, Method finderMethod) {
        String methodName = finderMethod.getName();
        String methodPart = methodName;
        if (methodName.startsWith("findBy")) {
            // No-op
        }
        else if (methodName.startsWith("listBy")) {
            methodPart = "findBy" + methodName.substring("listBy".length());
        }
        else if (methodName.startsWith("iterateBy")) {
            methodPart = "findBy" + methodName.substring("iterateBy".length());
        }
        else if (methodName.startsWith("scrollBy")) {
            methodPart = "findBy" + methodName.substring("scrollBy".length());
        }
        return findTargetType.getSimpleName() + "." + methodPart;
    }
}
