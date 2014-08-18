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

import io.datalayer.sql.dao.finder.FinderArgumentTypeFactory;

import org.hibernate.type.Type;

/**
 * Maps Enums to a custom Hibernate user type
 */
public class SimpleFinderArgumentTypeFactory implements FinderArgumentTypeFactory {

    public Type getArgumentType(Object arg) {
        // if(arg instanceof Enum)
        // {
        // return getEnumType(arg.getClass());
        // }
        // else
        // {
        return null;
        // }
    }

    // private Type getEnumType(Class<? extends Object> argClass)
    // {
    // Properties p = new Properties();
    // p.setProperty("enumClassName", argClass.getName());
    // Type enumType =
    // TypeFactory.heuristicType("org.hibernate.demo.EnumUserType", p);
    // return enumType;
    // }
}
