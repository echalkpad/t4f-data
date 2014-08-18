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

import org.testng.annotations.*;

import com.google.inject.Guice;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Inject;
import com.google.inject.matcher.Matchers;
import com.wideplay.warp.persist.PersistenceService;
import com.wideplay.warp.persist.UnitOfWork;
import com.wideplay.warp.persist.WorkManager;
import com.wideplay.warp.persist.Transactional;

import javax.jdo.*;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author Miroslav Genov (mgenov@gmail.com)
 */
@Test(suiteName = "jdo")
public class JoiningLocalTransactionsTest {

    private Injector injector;



    private static final String UNIQUE_TEXT = JoiningLocalTransactionsTest.class + "some unique text" + new Date();
    private static final String TRANSIENT_UNIQUE_TEXT = JoiningLocalTransactionsTest.class + "some other unique text" + new Date();

    private static final Long ID = new Long(0);

    @BeforeClass 
    public void before() {
        injector = Guice.createInjector(PersistenceService.usingJdo()
                .across(UnitOfWork.TRANSACTION)
                .forAll(Matchers.any())
                .buildModule(),
                new AbstractModule() {
                    protected void configure() {
                        //bind persistence unit to may establish connection to the database
                        bindConstant().annotatedWith(JdoUnit.class).to("testFactory");
                    }
                });

        JDOEnhancer enhancer = JDOHelper.getEnhancer();
        enhancer.setVerbose(true);
        enhancer.addClasses(JdoTestEntity.class.getName());
        enhancer.enhance();

        injector.getInstance(PersistenceService.class).start();
    }

    @AfterMethod
    //cleanup persistencemanager in case some of the rollback tests left it in an open state
    public void post() {
        injector.getInstance(WorkManager.class).endWork();
    }

    @AfterClass
    public void postClass() {
        injector.getInstance(PersistenceService.class).shutdown();
    }

    @Test
    public void testSimpleTransaction() {
        injector.getInstance(JoiningLocalTransactionsTest.TransactionalObject.class).runOperationInTxn();

        PersistenceManager pm = injector.getInstance(PersistenceManager.class);
        assert !pm.currentTransaction().isActive() : "txn was not closed by transactional service";

        //test that the data has been stored
        List<JdoTestEntity> result = (List<JdoTestEntity>) pm.newQuery(JdoTestEntity.class, "text == '" + UNIQUE_TEXT + "'").execute();

        injector.getInstance(WorkManager.class).endWork();

        assert  1 == result.size() : "queried entity did not match--did automatic txn fail?";
    }

    @Test
    public void sampleTest() {
        PersistenceManagerFactory f = injector.getInstance(PersistenceManagerFactory.class);
        PersistenceManagerFactory f2 = injector.getInstance(PersistenceManagerFactory.class);
        
        assert  f == f2 : "factory is not singleton";

    }

    @Test
    public void testSimpleTransactionRollbackOnChecked() {



        try {
            injector.getInstance(JoiningLocalTransactionsTest.TransactionalObject.class).runOperationInTxnThrowingChecked();
        } catch (IOException e) {
            //ignore
            System.out.println("caught (expecting rollback) " + e);

            injector.getInstance(WorkManager.class).endWork();
        }

        PersistenceManager pm = injector.getInstance(PersistenceManager.class);

        assert !pm.currentTransaction().isActive() : "PM was not closed by transactional service (rollback didnt happen?)";

        //test that the data has been stored
        List<JdoTestEntity> result = (List<JdoTestEntity>)pm.newQuery(JdoTestEntity.class, "text == '" + TRANSIENT_UNIQUE_TEXT + "'").execute();
                
        injector.getInstance(WorkManager.class).endWork();

        assert 0 == result.size() : "a result was returned! rollback sure didnt happen!!!";
    }


    @Test
     public void testSimpleTransactionRollbackOnUnchecked() {
        try {
            injector.getInstance(JoiningLocalTransactionsTest.TransactionalObject.class).runOperationInTxnThrowingUnchecked();
        } catch(RuntimeException re) {
            //ignore
            System.out.println("caught (expecting rollback) " + re);
            injector.getInstance(WorkManager.class).endWork();
        }

        PersistenceManager pm = injector.getInstance(PersistenceManager.class);
        assert !pm.currentTransaction().isActive() : "Session was not closed by transactional service (rollback didnt happen?)";

        //test that the data has been stored
        List<JdoTestEntity> result = (List<JdoTestEntity>) pm.newQuery(JdoTestEntity.class, "text == '" + TRANSIENT_UNIQUE_TEXT + "'").execute();

        injector.getInstance(WorkManager.class).endWork();

        assert 0 == result.size() : "a result was returned! rollback sure didnt happen!!!";
    }


    public static class TransactionalObject {
        private final PersistenceManager pm;

        @Inject
        public TransactionalObject(PersistenceManager pm) {
            this.pm = pm;
        }

        @Transactional
        public void runOperationInTxn() {
            runOperationInTxnInternal();
        }

        @Transactional(rollbackOn = IOException.class)
        public void runOperationInTxnInternal() {
            JdoTestEntity entity = new JdoTestEntity();
            entity.setText(UNIQUE_TEXT);

            pm.makePersistent(entity);
        }

        @Transactional(rollbackOn = IOException.class)
        public void runOperationInTxnThrowingChecked() throws IOException {
            runOperationInTxnThrowingCheckedInternal();
        }

        @Transactional
        private void runOperationInTxnThrowingCheckedInternal() throws IOException {
            JdoTestEntity entity = new JdoTestEntity();
            entity.setText(TRANSIENT_UNIQUE_TEXT);

            pm.makePersistent(entity);

            throw new IOException();
        }

        @Transactional
        public void runOperationInTxnThrowingUnchecked() {
            runOperationInTxnThrowingUncheckedInternal();
        }

        @Transactional(rollbackOn = IOException.class)
        public void runOperationInTxnThrowingUncheckedInternal() {
            JdoTestEntity entity = new JdoTestEntity();
            entity.setText(TRANSIENT_UNIQUE_TEXT);
            pm.makePersistent(entity);

            throw new IllegalStateException();
        }
    }

}
