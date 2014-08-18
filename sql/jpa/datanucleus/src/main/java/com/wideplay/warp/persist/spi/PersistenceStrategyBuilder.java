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
package com.wideplay.warp.persist.spi;

import com.wideplay.warp.persist.spi.Builder;

import java.lang.annotation.Annotation;

/**
 * Formalizes naming conventions for {@link com.wideplay.warp.persist.PersistenceStrategy} builders.
 * @author Robbie Vanbrabant
 */
public interface PersistenceStrategyBuilder<T> extends Builder<T> {
    /**
     * Configure this strategy to build modules bound to the specified Guice
     * Binding Annotation.
     * 
     * @param annotation a valid Guice Binding Annotation to which all persistence
     *        artifacts will be bound, including interceptors
     * @return this
     */
    PersistenceStrategyBuilder<T> annotatedWith(Class<? extends Annotation> annotation);
}
