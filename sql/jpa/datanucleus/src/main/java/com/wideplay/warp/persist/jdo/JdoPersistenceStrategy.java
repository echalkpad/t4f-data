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
import com.wideplay.warp.persist.*;
import com.wideplay.warp.persist.internal.InternalWorkManager;
import com.wideplay.warp.persist.spi.*;
import org.aopalliance.intercept.MethodInterceptor;

import javax.jdo.*;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author Miroslav Genov
 */
public class JdoPersistenceStrategy implements PersistenceStrategy {
    private final Properties jdoProperties;
    private final String unit;
    private final Class<? extends Annotation> annotation;

    public JdoPersistenceStrategy(JdoPersistenceStrategyBuilder builder) {
        this.jdoProperties = builder.jdoProperties;
        this.unit = builder.unit;
        this.annotation = builder.annotation;
    }


    public PersistenceModule getBindings(PersistenceConfiguration persistenceConfiguration) {
        return new JdoPersistenceModule(persistenceConfiguration);
    }

    class JdoPersistenceModule extends AbstractPersistenceModule {
        private final PersistenceConfiguration config;
        private final PersistenceManagerFactoryProvider pmfProvider;
        private final PersistenceManagerProvider pmProvider;
        private final WorkManager workManager;
        private final JdoPersistenceService pService;
        // needed for bindings created in the constructor.
        private final List<Module> scheduledBindings = new ArrayList<Module>();
        private InternalWorkManager<PersistenceManager> internalWm;

        private JdoPersistenceModule(PersistenceConfiguration configuration) {
            super(configuration, annotation);
            this.config = configuration;
            this.pmfProvider = new PersistenceManagerFactoryProvider(getPersistenceUnitKey(),
                    getExtraPersistencePropertiesKey());
            internalWm = new JdoInternalWorkManager(pmfProvider);
            this.pmProvider = new PersistenceManagerProvider(internalWm);
            this.workManager = new JdoWorkManager(internalWm);
            this.pService = new JdoPersistenceService(pmfProvider);
        }

        protected void configure() {
            for (Module m : scheduledBindings) install(m);
            // Set up JDO.
            bindWithUnitAnnotation(PersistenceManagerFactory.class).toProvider(pmfProvider).in(Singleton.class);
            bindWithUnitAnnotation(PersistenceManager.class).toProvider(pmProvider);
            bindWithUnitAnnotation(PersistenceService.class).toInstance(pService);
            bindWithUnitAnnotation(JdoPersistenceService.class).toInstance(pService);
            bindWithUnitAnnotation(WorkManager.class).toInstance(workManager);

            MethodInterceptor txInterceptor = new JdoLocalTxnInterceptor(this.internalWm, config.getUnitOfWork());
            bindTransactionInterceptor(txInterceptor);

            // Set up Dynamic Finders.
            MethodInterceptor finderInterceptor = new JdoFinderInterceptor(pmProvider);
            bindFinderInterceptor(finderInterceptor);
            bindTransactionalDynamicAccessors(finderInterceptor, txInterceptor);
        }

        private Key<String> getPersistenceUnitKey() {
            if (!inMultiModulesMode()) {
                return Key.get(String.class, JdoUnit.class);
            } else {
                final Key<String> key = Key.get(String.class, JdoUnitInstance.of(annotation));
                if (unit != null) {
                    scheduledBindings.add(new AbstractModule() {
                        protected void configure() {
                            bind(key).toInstance(unit);
                        }
                    });
                }
                return key;
            }
        }

        private Key<Properties> getExtraPersistencePropertiesKey() {
            if (!inMultiModulesMode()) {
                return Key.get(Properties.class, JdoUnit.class);
            } else {
                final Key<Properties> key = Key.get(Properties.class, JdoUnitInstance.of(JdoPersistenceStrategy.this.annotation));
                if (jdoProperties != null) {
                    scheduledBindings.add(new AbstractModule() {
                        protected void configure() {
                            bind(key).toInstance(jdoProperties);
                        }
                    });
                }
                return key;
            }
        }

        public void visit(PersistenceModuleVisitor visitor) {
            if (unitOfWorkRequest()) {
                visitor.publishWorkManager(this.workManager);
                visitor.publishPersistenceService(this.pService);
            }
        }
    }

    public static JdoPersistenceStrategyBuilder builder() {
        return new JdoPersistenceStrategyBuilder();
    }

    public static class JdoPersistenceStrategyBuilder implements PersistenceStrategyBuilder<JdoPersistenceStrategy> {
        private Properties jdoProperties;
        private String unit;
        private Class<? extends Annotation> annotation;

        public JdoPersistenceStrategyBuilder properties(Properties jpaProperties) {
            this.jdoProperties = jpaProperties;
            return this;
        }

        public JdoPersistenceStrategyBuilder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public JdoPersistenceStrategyBuilder annotatedWith(Class<? extends Annotation> annotation) {
            this.annotation = annotation;
            return this;
        }

        public JdoPersistenceStrategy build() {
            return new JdoPersistenceStrategy(this);
        }
    }
}
