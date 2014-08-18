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

import com.db4o.Db4o;
import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.wideplay.codemonkey.web.startup.Initializer;
import com.wideplay.warp.persist.dao.HibernateTestAccessor;
import com.wideplay.warp.persist.db4o.Db4oPersistenceStrategy;
import com.wideplay.warp.persist.hibernate.HibernatePersistenceStrategy;
import com.wideplay.warp.persist.hibernate.HibernateTestEntity;
import com.wideplay.warp.persist.jpa.JpaPersistenceStrategy;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * On: 2/06/2007
 *
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 * @since 1.0
 */
public class EdslBuilderTest {
    @Test
    public void testEdslLanguage() {
        PersistenceService.usingHibernate().buildModule();

        PersistenceService.usingHibernate().across(UnitOfWork.REQUEST)
                .addAccessor(HibernateTestAccessor.class)
                .buildModule();

        PersistenceService.usingHibernate().across(UnitOfWork.TRANSACTION).forAll(Matchers.any()).buildModule();
    }

    @Test
    public void testHibernateConfig() {
        Injector injector = Guice.createInjector(PersistenceService.usingHibernate().across(UnitOfWork.TRANSACTION)
                .buildModule(),
                new AbstractModule() {
                    protected void configure() {
                        bind(Configuration.class).toInstance(new AnnotationConfiguration().addAnnotatedClass(HibernateTestEntity.class)
                                .setProperties(Initializer.loadProperties("spt-persistence.properties")));
                    }
                });

        injector.getInstance(PersistenceService.class).start();

        injector.getInstance(TransactionalObject.class).txnMethod();
    }

    @Test
    public final void testMultimodulesConfigJpa() {
        PersistenceStrategy jpa = JpaPersistenceStrategy.builder()
                .properties(new Properties())
                .unit("myUnit")
                .annotatedWith(MyUnit.class).build();
        Module m = PersistenceService.using(jpa)
                .across(UnitOfWork.TRANSACTION)

                .forAll(Matchers.any(), Matchers.annotatedWith(Transactional.class))
                .buildModule();

        Guice.createInjector(m);
    }

    @Test
    public final void testMultimodulesConfigHibernate() {
        PersistenceStrategy h = HibernatePersistenceStrategy.builder()
                .configuration(new Configuration())
                .annotatedWith(MyUnit.class).build();
        Module m = PersistenceService.using(h)
                .across(UnitOfWork.TRANSACTION)

                .forAll(Matchers.any(), Matchers.annotatedWith(Transactional.class))
                .buildModule();

        Guice.createInjector(m);
    }

    @Test
    public final void testPersistenceServicesProvider() {
        PersistenceStrategy h = HibernatePersistenceStrategy.builder()
                .configuration(new Configuration())
                .annotatedWith(MyUnit.class).build();
        Module hibernateModule = PersistenceService.using(h)
                .across(UnitOfWork.TRANSACTION)

                .forAll(Matchers.any(), Matchers.annotatedWith(Transactional.class))
                .buildModule();

        PersistenceStrategy jpa = JpaPersistenceStrategy.builder()
                .properties(new Properties())
                .unit("myUnit")
                .annotatedWith(MySecondUnit.class).build();
        Module jpaModule = PersistenceService.using(jpa)
                .across(UnitOfWork.TRANSACTION)

                .forAll(Matchers.any(), Matchers.annotatedWith(Transactional.class))
                .buildModule();

        List<PersistenceService> persistenceServices = Guice.createInjector(hibernateModule, jpaModule,
                new PersistenceServiceExtrasModule())
                .getInstance(Key.get(new TypeLiteral<List<PersistenceService>>() {
                }));

        assert persistenceServices.size() == 2;
    }

    @Test
    public final void testMultimodulesConfigDb4o() {
        PersistenceStrategy h = Db4oPersistenceStrategy.builder()
                .configuration(Db4o.newConfiguration())
                .annotatedWith(MyUnit.class)//.databaseFileName("TestDatabase.data")
                .host("localhost").port(4321).user("autobot").password("morethanmeetstheeye")
                .build();
        Module m = PersistenceService.using(h)
                .across(UnitOfWork.TRANSACTION)
                .forAll(Matchers.any(), Matchers.annotatedWith(Transactional.class))
                .buildModule();
        PersistenceService service = Guice.createInjector(m).getInstance(Key.get(PersistenceService.class, MyUnit.class));
        service.start();
        service.shutdown();
    }

    static class TransactionalObject {
        @Transactional
        public void txnMethod() {
        }
    }
}
