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

import com.google.inject.*;
import com.wideplay.warp.persist.internal.LazyReference;

import javax.jdo.*;
import javax.persistence.Persistence;
import java.util.Properties;
import java.util.logging.Logger;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

/**
 * @author Miroslav Genov
 */
@Immutable
@ThreadSafe
class PersistenceManagerFactoryProvider implements Provider<PersistenceManagerFactory> {
    private final Key<String> persistenceUnitName;
    private final Key<Properties> persistenceProperties;

    private final Logger log = Logger.getLogger(PersistenceManagerFactoryProvider.class.getName());

    @Inject
    private final Injector injector = null;


    /**
     * Lazily loaded PersistenceManagerFactory. (Same as used in JPA's Implementation)
     */
    private LazyReference<PersistenceManagerFactory> emFactory =
            LazyReference.of(new Provider<PersistenceManagerFactory>() {
                public PersistenceManagerFactory get() {
                    String psName = injector.getInstance(persistenceUnitName);
                    if (customPropertiesBound()) {
                        Properties props = injector.getInstance(persistenceProperties);
                        return JDOHelper.getPersistenceManagerFactory(props);
                    } else {
                        return JDOHelper.getPersistenceManagerFactory(psName);
                    }
                }
            });

    private boolean customPropertiesBound() {
        return injector.getBindings().get(persistenceProperties) != null;
    }


    public PersistenceManagerFactoryProvider(Key<String> persistenceUnitName, Key<Properties> persistenceProperties) {
        this.persistenceUnitName = persistenceUnitName;
        this.persistenceProperties = persistenceProperties;
    }
    
    public PersistenceManagerFactory get() {               
        return emFactory.get();
    }    

}
