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

package com.wideplay.warp.persist.jpa;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.wideplay.warp.persist.internal.LazyReference;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

/**
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 * @author Robbie Vanbrabant
 * @since 1.0
 */
@Immutable
@ThreadSafe
class EntityManagerFactoryProvider implements Provider<EntityManagerFactory> {
    private final Key<String> persistenceUnitName;
    private final Key<Properties> persistenceProperties;
    
    @Inject
    private final Injector injector = null;

    /**
     * Lazily loaded EntityManagerFactory.
     */
    private LazyReference<EntityManagerFactory> emFactory =
            LazyReference.of(new Provider<EntityManagerFactory>() {
                public EntityManagerFactory get() {
                    String psName = injector.getInstance(persistenceUnitName);
                    if (customPropertiesBound()) {
                        Properties props = injector.getInstance(persistenceProperties);
                        return Persistence.createEntityManagerFactory(psName, props);
                    } else {
                        return Persistence.createEntityManagerFactory(psName);
                    }
                }
            });

    private boolean customPropertiesBound() {
        return injector.getBindings().get(persistenceProperties) != null;
    }

    public EntityManagerFactoryProvider(Key<String> persistenceUnitName, Key<Properties> persistenceProperties) {
        this.persistenceUnitName = persistenceUnitName;
        this.persistenceProperties = persistenceProperties;
    }

    public EntityManagerFactory get() {
        return emFactory.get();
    }
}
