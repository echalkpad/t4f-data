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

import com.google.inject.Injector;
import com.google.inject.Guice;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.matcher.Matchers;
import com.wideplay.warp.persist.PersistenceService;
import com.wideplay.warp.persist.UnitOfWork;
import com.wideplay.warp.persist.WorkManager;
import com.wideplay.warp.persist.Transactional;

import java.util.Date;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import javax.jdo.PersistenceManager;

/**
 * @author Miroslav Genov (mgenov@gmail.com)
 */
@Test(suiteName = "jdo")
public class JdoWorkManagerTest {

    private Injector injector;
    private static final String UNIQUE_TEXT_3 = JdoWorkManagerTest.class.getSimpleName()
            + "CONSTRAINT_VIOLATING some other unique text" + new Date();

    @BeforeClass
    public void pre() {
        injector = Guice.createInjector(PersistenceService.usingJdo()
            .across(UnitOfWork.REQUEST)
            .forAll(Matchers.any())
            .buildModule(),
                new AbstractModule() {

                    protected void configure() {
                        //bind persistence unit to may establish connection to the database
                        bindConstant().annotatedWith(JdoUnit.class).to("testFactory");
                    }
                });

        //startup persistence
        injector.getInstance(PersistenceService.class)
                .start();
    }

    @AfterClass
    public void post() {
        injector.getInstance(PersistenceService.class).shutdown();
    }


    @Test
    public void workManagerTest() {
        injector.getInstance(WorkManager.class).beginWork();
        try {
            injector.getInstance(TransactionalObject.class).runOperationInTxn();
        } finally {
            injector.getInstance(WorkManager.class).endWork();

        }


        injector.getInstance(WorkManager.class).beginWork();
        injector.getInstance(PersistenceManager.class).currentTransaction().begin();
        try {

            final List<JdoTestEntity> result = (List<JdoTestEntity>) injector.getInstance(PersistenceManager.class).newQuery(JdoTestEntity.class, "text == '" + UNIQUE_TEXT_3 + "'").execute();
            
            assert 1 == result.size() : "no result!!";

        } finally {
            injector.getInstance(PersistenceManager.class).currentTransaction().commit();
            injector.getInstance(WorkManager.class).endWork();
        }
    }

    @Test
    public void testCloseMoreThanOnce() {
        injector.getInstance(PersistenceService.class).shutdown();
        injector.getInstance(PersistenceService.class).shutdown();
    }

    public static class TransactionalObject {
        @Inject
        PersistenceManager pm;

        @Transactional
        public void runOperationInTxn() {
            JdoTestEntity testEntity = new JdoTestEntity();

            testEntity.setText(UNIQUE_TEXT_3);
            pm.makePersistent(testEntity);
        }

        @Transactional
        public void runOperationInTxnError() {

            JdoTestEntity testEntity = new JdoTestEntity();

            testEntity.setText(UNIQUE_TEXT_3 + "transient never in db!" + hashCode());
            pm.makePersistent(testEntity);
        }
    }

}
