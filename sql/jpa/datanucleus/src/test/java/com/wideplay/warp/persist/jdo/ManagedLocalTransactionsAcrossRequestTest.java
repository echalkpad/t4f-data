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
import com.google.inject.name.Named;
import com.google.inject.matcher.Matchers;
import com.wideplay.warp.persist.PersistenceService;
import com.wideplay.warp.persist.UnitOfWork;
import com.wideplay.warp.persist.WorkManager;
import com.wideplay.warp.persist.Transactional;
import com.wideplay.warp.persist.dao.Finder;

import java.util.Date;
import java.util.List;
import java.io.IOException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;


import javax.jdo.PersistenceManagerFactory;
import javax.jdo.PersistenceManager;

/**
 * @author Miroslav Genov (mgenov@gmail.com)
 */
@Test(suiteName = "jdo")
public class ManagedLocalTransactionsAcrossRequestTest {
    private Injector injector;
    private static final String UNIQUE_TEXT = "some unique text" + new Date();
    private static final String UNIQUE_TEXT_MERGE = "meRG_Esome unique text" + new Date();
    private static final String UNIQUE_TEXT_MERGE_FORDF = "aSdoaksdoaksdmeRG_Esome unique text" + new Date();
    private static final String TRANSIENT_UNIQUE_TEXT = "some other unique text" + new Date();


    @BeforeClass
    public void pre() {
        injector = Guice.createInjector(
                new AbstractModule() {

                    protected void configure() {
                        //bind persistence unit to may establish connection to the database
                        bindConstant().annotatedWith(JdoUnit.class).to("testFactory");
                    }
                },
                PersistenceService.usingJdo()
            .across(UnitOfWork.REQUEST)

            .forAll(Matchers.any())
            .buildModule());

        //startup persistence
        injector.getInstance(PersistenceService.class)
                .start();
    }


    @AfterTest
    //cleanup persistencemanager in case some of the rollback tests left it in an open state
    public final void post() {
        injector.getInstance(WorkManager.class).endWork();
    }

    @AfterClass
    public final void postClass() {
        injector.getInstance(PersistenceManagerFactory.class).close();
    }

    @Test
    public void testSimpleTransaction() {
        injector.getInstance(TransactionalObject.class).runOperationInTxn();

        PersistenceManager em = injector.getInstance(PersistenceManager.class);
        assert !em.currentTransaction().isActive() : "txn was not closed by transactional service";

        //test that the data has been stored
        List<JdoTestEntity> result = (List<JdoTestEntity>) em.newQuery(JdoTestEntity.class, "text == '" + UNIQUE_TEXT + "'").execute();
        injector.getInstance(WorkManager.class).endWork();

        assert 1 == result.size()  :  "queried entity did not match--did automatic txn fail?";
    }

    @Test
    public void testSimpleTransactionWithMerge() {
        PersistenceManager emOrig = injector.getInstance(PersistenceManager.class);
        JdoTestEntity entity = injector.getInstance(TransactionalObject.class).runOperationInTxnWithMerge();

        assert null != entity.getId() : "Entity was not given an id (was not persisted correctly?)";

        PersistenceManager em = injector.getInstance(PersistenceManager.class);
        assert !em.currentTransaction().isActive() : "txn was not closed by transactional service";


        //test that the data has been stored
        assert !em.isClosed() : "Em was closed after txn!";

        assert emOrig.equals(em) : "Em was not kept open across txns";
        assert  null != em.getObjectById(JdoTestEntity.class,entity.getId()) : "Merge did not store state or did not return persistent copy";

        List<JdoTestEntity> result = (List<JdoTestEntity>) em.newQuery(JdoTestEntity.class, "text == '" + UNIQUE_TEXT_MERGE + "'").execute();
        injector.getInstance(WorkManager.class).endWork();

        assert  1 == result.size() : "queried entity did not match--did automatic txn fail?";        
    }

    @Test
    public void testSimpleTransactionWithMergeAndDF() {
        PersistenceManager emOrig = injector.getInstance(PersistenceManager.class);
        JdoTestEntity entity = injector.getInstance(TransactionalObject.class).runOperationInTxnWithMergeForDf();


        PersistenceManager pm = injector.getInstance(PersistenceManager.class);
        assert !pm.currentTransaction().isActive() : "txn was not closed by transactional service";

        //test that the data has been stored
        assert !pm.isClosed() : "PM was closed after txn!";
        // TODO commented out because of multiple modules refactoring
        //assert pm.equals(EntityManagerFactoryHolder.checkCurrentEntityManager()) : "Em was not kept open across txns";
        assert emOrig.equals(pm) : "PM was not kept open across txns";
        assert  null != pm.getObjectById(JdoTestEntity.class,entity.getId()) : "Merge did not store state or did not return persistent copy";

        List<JdoTestEntity> result = (List<JdoTestEntity>) pm.newQuery(JdoTestEntity.class, "text == '" + UNIQUE_TEXT_MERGE_FORDF + "'").execute();
        injector.getInstance(WorkManager.class).endWork();
        
        assert  1 == result.size() : "queried entity did not match--did automatic txn fail?";
    }

    @Test
    public void testSimpleTransactionRollbackOnChecked() {
        try {
            injector.getInstance(TransactionalObject.class).runOperationInTxnThrowingChecked();
        } catch(IOException e) {
            //ignore
            System.out.println("caught (expecting rollback) " + e);

            injector.getInstance(WorkManager.class).endWork();
        }

        PersistenceManager pm = injector.getInstance(PersistenceManager.class);

        assert !pm.currentTransaction().isActive() : "Previous PM was not closed by transactional service (rollback didnt happen?)";

        //test that the data has been stored
        List<JdoTestEntity> result = (List<JdoTestEntity>) pm.newQuery(JdoTestEntity.class, "text == '" + TRANSIENT_UNIQUE_TEXT + "'").execute();
        injector.getInstance(WorkManager.class).endWork();

        assert 0 == result.size() : "a result was returned! rollback sure didnt happen!!!";
    }

    @Test
    public void testSimpleTransactionRollbackOnUnchecked() {
        try {
            injector.getInstance(TransactionalObject.class).runOperationInTxnThrowingUnchecked();
        } catch(RuntimeException re) {
            //ignore
            System.out.println("caught (expecting rollback) " + re);
            injector.getInstance(WorkManager.class).endWork();
        }

        PersistenceManager pm = injector.getInstance(PersistenceManager.class);
        assert !pm.currentTransaction().isActive() : "pmf was not closed by transactional service (rollback didnt happen?)";

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
            JdoTestEntity entity = new JdoTestEntity();
            entity.setText(UNIQUE_TEXT);
            pm.makePersistent(entity);
        }

        @Transactional
        public JdoTestEntity runOperationInTxnWithMerge() {
            JdoTestEntity entity = new JdoTestEntity();
            entity.setText(UNIQUE_TEXT_MERGE);
            return pm.makePersistent(entity);
        }

        @Transactional
        public JdoTestEntity runOperationInTxnWithMergeForDf() {
            JdoTestEntity entity = new JdoTestEntity();
            entity.setText(UNIQUE_TEXT_MERGE_FORDF);
            return pm.makePersistent(entity);
        }

        @Transactional(rollbackOn = IOException.class)
        public void runOperationInTxnThrowingChecked() throws IOException {
            JdoTestEntity entity = new JdoTestEntity();
            entity.setText(TRANSIENT_UNIQUE_TEXT);
            pm.makePersistent(entity);

            throw new IOException();
        }

        @Transactional
        public void runOperationInTxnThrowingUnchecked() {
            JdoTestEntity entity = new JdoTestEntity();
            entity.setText(TRANSIENT_UNIQUE_TEXT);
            pm.makePersistent(entity);

            throw new IllegalStateException();
        }

        @Finder(query = "from JpaTestEntity where text = :text")
        public JdoTestEntity find(@Named("text") String text) {
            return null;
        }
    }
    

}
