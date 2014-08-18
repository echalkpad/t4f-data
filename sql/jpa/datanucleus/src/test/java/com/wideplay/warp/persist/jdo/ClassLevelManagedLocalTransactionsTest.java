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

import com.wideplay.warp.persist.Transactional;
import com.wideplay.warp.persist.jpa.JpaTestEntity;

import java.util.Date;
import java.util.List;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.testng.annotations.*;

import javax.jdo.JDOEnhancer;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.persistence.EntityManager;


/**
 * @author Miroslav Genov (mgenov@gmail.com)
 */
@Test(suiteName = "jdo")
public class ClassLevelManagedLocalTransactionsTest {
    private Injector injector;
    private static final String UNIQUE_TEXT = "JDOsome unique text88888" + new Date();
    private static final String UNIQUE_TEXT_2 = "JDOsome asda unique teasdalsdplasdxt" + new Date();
    private static final String TRANSIENT_UNIQUE_TEXT = "JDOsome other unique texaksoksojadasdt" + new Date();

    @BeforeMethod
    public void before() {
        injector = Guice.createInjector(PersistenceService.usingJdo()
                .across(UnitOfWork.TRANSACTION)
                .forAll(Matchers.annotatedWith(Transactional.class), Matchers.any())
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
    public void post() {
        injector.getInstance(PersistenceService.class).shutdown();
    }

    @Test
    public void testSimpleTransaction() {
        injector.getInstance(TransactionalObject.class).runOperationInTxn();

        PersistenceManager pm = injector.getInstance(PersistenceManager.class);
        assert !pm.currentTransaction().isActive() : "PersistenceManager was not closed by transactional service";

        //test that the data has been stored
        pm.currentTransaction().begin();

        List<JdoTestEntity> result = (List<JdoTestEntity>) pm.newQuery(JdoTestEntity.class, "text == '" + UNIQUE_TEXT + "'").execute();

        pm.currentTransaction().commit();
       
        assert result.size() == 1 : "queried entity did not match--did automatic txn fail?";
    }

    @Test
    public void testSimpleTransactionRollbackOnChecked() {
        try {
            injector.getInstance(TransactionalObject2.class).runOperationInTxnThrowingChecked();
        } catch(IOException e) {
            //ignore
        }

        PersistenceManager pm = injector.getInstance(PersistenceManager.class);
        assert !pm.currentTransaction().isActive() : "PersistenceManager was not closed by transactional service (rollback didnt happen?)";

        //test that the data has been stored
        pm.currentTransaction().begin();
        List<JdoTestEntity> result = (List<JdoTestEntity>) pm.newQuery(JdoTestEntity.class, "text == '" + TRANSIENT_UNIQUE_TEXT + "'").execute();

        pm.currentTransaction().commit();

        assert result.isEmpty() : "a result was returned! rollback sure didnt happen!!!";
    }

    @Test
    public void testSimpleTransactionRollbackOnCheckedExcepting() {
        Exception ex = null;
        try {
            injector.getInstance(TransactionalObject3.class).runOperationInTxnThrowingCheckedExcepting();
        } catch (IOException e) {
            //ignore
            ex = e;

        }
        assert null != ex : "Exception was not thrown by test txn-al method!";

        PersistenceManager pm = injector.getInstance(PersistenceManager.class);
        assert !pm.currentTransaction().isActive() : "Txn was not closed by transactional service (commit didnt happen?)";

        //test that the data has been stored
        pm.currentTransaction().begin();

        List<JdoTestEntity> result = (List<JdoTestEntity>) pm.newQuery(JdoTestEntity.class, "text == '" + UNIQUE_TEXT_2 + "'").execute();

        pm.currentTransaction().commit();

        assert 0 != result.size() : "a result was not returned! rollback happened anyway (exceptOn failed)!!!";
    }

    @Test
    public void testSimpleTransactionRollbackOnUnchecked() {
        try {
            injector.getInstance(TransactionalObject4.class).runOperationInTxnThrowingUnchecked();
        } catch(RuntimeException re) {
            //ignore
        }

        PersistenceManager pm = injector.getInstance(PersistenceManager.class);
        assert !pm.currentTransaction().isActive() : "PersistenceManager was not closed by transactional service (rollback didnt happen?)";

        //test that the data has been stored
        pm.currentTransaction().begin();

        List<JdoTestEntity> result = (List<JdoTestEntity>) pm.newQuery(JdoTestEntity.class, "text == '" + TRANSIENT_UNIQUE_TEXT + "'").execute();
        
        pm.currentTransaction().commit();

        assert result.isEmpty() : "a result was returned! rollback sure didnt happen!!!";
    }


    @Transactional
    public static class TransactionalObject {
        @Inject
        PersistenceManager pm;

        public void runOperationInTxn() {
            JdoTestEntity entity = new JdoTestEntity();
            entity.setText(UNIQUE_TEXT);
            pm.makePersistent(entity);
        }

    }


    @Transactional
    public static class TransactionalObject4 {
        @Inject PersistenceManager pm;

        @Transactional
        public void runOperationInTxnThrowingUnchecked() {
            JdoTestEntity entity = new JdoTestEntity();
            entity.setText(TRANSIENT_UNIQUE_TEXT);
            pm.makePersistent(entity);

            throw new IllegalStateException();
        }
    }


    @Transactional(rollbackOn = IOException.class, exceptOn = FileNotFoundException.class)
    public static class TransactionalObject3 {
        @Inject PersistenceManager pm;

        public void runOperationInTxnThrowingCheckedExcepting() throws IOException {
            JdoTestEntity entity = new JdoTestEntity();
            entity.setText(UNIQUE_TEXT_2);
            pm.makePersistent(entity);

            throw new FileNotFoundException();
        }
    }


    @Transactional(rollbackOn = IOException.class)
    public static class TransactionalObject2 {
        @Inject PersistenceManager pm;

        public void runOperationInTxnThrowingChecked() throws IOException {
            JdoTestEntity entity = new JdoTestEntity();
            entity.setText(TRANSIENT_UNIQUE_TEXT);
            pm.makePersistent(entity);

            throw new IOException();
        }
    }


}
