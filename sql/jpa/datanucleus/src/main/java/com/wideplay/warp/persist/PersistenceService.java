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

package com.wideplay.warp.persist;

import com.wideplay.warp.persist.dao.Finder;
import com.wideplay.warp.persist.PersistenceStrategy;
import com.wideplay.warp.persist.spi.PersistenceModuleVisitor;
import com.wideplay.warp.persist.internal.PersistenceFlavor;
import com.wideplay.warp.persist.internal.PersistenceServiceBuilderImpl;

import java.lang.reflect.Method;

/**
 * <p>
 * This is the core warp-persist artifact. It providers factories for generating guice
 * modules for your persistence configuration. It also must be injected into your code later
 * as a service abstraction for starting the underlying persistence engine (Hibernate or JPA).
 * </p>
 * <p>
 * Implementations of this type should make sure {@link #start()} and {@link #shutdown()} are
 * thread safe.
 * </p>
 *
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 * @since 1.0
 */
public abstract class PersistenceService {
    private static final PersistenceModuleVisitor persistenceModuleVisitor = new PersistenceModuleVisitor() {
        public void publishWorkManager(WorkManager wm) {
             PersistenceFilter.registerWorkManager(wm);
        }
        public void publishPersistenceService(PersistenceService persistenceService) {
            PersistenceFilter.registerPersistenceService(persistenceService);
        }
    };

    /**
     * Starts the underlying persistence engine and makes warp-persist ready for use.
     * For instance, with hibernate, it creates a SessionFactory and may open connection pools.
     * This method *must* be called by your code prior to using any warp-persist or hibernate artifacts.
     * If already started, calling this method does nothing, if already stopped, it also does nothing.
     */
    public abstract void start();

    /**
     * Stops the underlying persistence engine.
     * For instance, with Hibernate, it closes the SessionFactory.
     * If already stopped, calling this method does nothing. If not yet started, it also does nothing.
     */
    public abstract void shutdown();

    /**
     * A factory for warp-persist using Hibernate in your Guice module. See http://www.wideplay.com
     * for proper documentation on the EDSL.
     *
     * @return Returns the next step in the configuration chain.
     */
    public static SessionStrategyBuilder usingHibernate() {
        return new PersistenceServiceBuilderImpl(PersistenceFlavor.HIBERNATE, persistenceModuleVisitor);
    }


    /**
     * A factory for warp-persist using JPA in your Guice module. See http://www.wideplay.com
     * for proper documentation on the EDSL. Any compliant implementation of JPA is supported (in theory).
     * Currently, TopLink, Hibernate and OpenJPA have shown positive results.
     *
     * @return Returns the next step in the configuration chain.
     */
    public static SessionStrategyBuilder usingJpa() {
        return new PersistenceServiceBuilderImpl(PersistenceFlavor.JPA, persistenceModuleVisitor);
    }

    /**
     * A factory for warp-persist using Db4o in your Guice module. See http://www.wideplay.com
     * for proper documentation on the EDSL. Note that Db4o has slightly different semantics
     * than ORM frameworks like Hibernate and JPA. Consult the documentation carefully.
     *
     * @return Returns the next step in the configuration chain.
     */
    public static SessionStrategyBuilder usingDb4o() {
        return new PersistenceServiceBuilderImpl(PersistenceFlavor.DB4O, persistenceModuleVisitor);
    }

  /**
     * A factory for warp-persist using Jdo in your Guice module. See http://www.wideplay.com
     * for proper documentation on the EDSL. Note that Db4o has slightly different semantics
     * than ORM frameworks like Hibernate and JPA. Consult the documentation carefully.
     * //TODO: need to be documented in the main site or comment to be changed to different one
     *
     * @return Returns the next step in the configuration chain.
     */
    public static SessionStrategyBuilder usingJdo() {
        return new PersistenceServiceBuilderImpl(PersistenceFlavor.JDO, persistenceModuleVisitor);
    }

    /**
     * Configure a given {@link PersistenceStrategy}, either
     * because it is not part of Warp Persist, or because you need support for multiple
     * persistence modules (bound to an annotation).
     *
     * @param ps the {@code PersistenceService} to configure
     * @return the next step in the configuration chain
     */
    public static SessionStrategyBuilder using(PersistenceStrategy ps) {
        return new PersistenceServiceBuilderImpl(ps, persistenceModuleVisitor);
    }

    
    /**
     * A utility for testing if a given method is a dynamic finder.
     *
     * @param method A method you suspect is a Dynamic Finder.
     * @return Returns true if the method is annotated {@code @Finder}
     */
    public static boolean isDynamicFinder(Method method) {
        return method.isAnnotationPresent(Finder.class);
    }
}
