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

package com.wideplay.warp.persist.hibernate;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.wideplay.codemonkey.web.startup.Initializer;
import com.wideplay.warp.persist.PersistenceService;
import com.wideplay.warp.persist.Transactional;
import com.wideplay.warp.persist.UnitOfWork;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Expression;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * On: 2/06/2007
 *
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 * @since 1.0
 */
public class ManagedLocalTransactionsTest {
    private Injector injector;
    private static final String UNIQUE_TEXT = "some unique text" + new Date();
    private static final String UNIQUE_TEXT_2 = "some asda unique text" + new Date();
    private static final String TRANSIENT_UNIQUE_TEXT = "some other unique text" + new Date();

    @BeforeMethod
    public void pre() {
        injector = Guice.createInjector(PersistenceService.usingHibernate()
            .across(UnitOfWork.TRANSACTION)
            .forAll(Matchers.any())
            .buildModule(),
                new AbstractModule() {

                    protected void configure() {
                        bind(Configuration.class).toInstance(new AnnotationConfiguration()
                            .addAnnotatedClass(HibernateTestEntity.class)
                            .setProperties(Initializer.loadProperties("spt-persistence.properties")));
                    }
                }
        );

        //startup persistence
        injector.getInstance(PersistenceService.class)
                .start();
    }

    @AfterMethod
    void post() {
        injector.getInstance(SessionFactory.class).close();
        injector = null;
    }

    @Test
    public void testSimpleTransaction() {
        injector.getInstance(TransactionalObject.class).runOperationInTxn();

        Session session = injector.getInstance(Session.class);
        assert !session.getTransaction().isActive() : "Session was not closed by transactional service";

        //test that the data has been stored
        session.beginTransaction();
        Object result = session.createCriteria(HibernateTestEntity.class).add(Expression.eq("text", UNIQUE_TEXT)).uniqueResult();
        session.getTransaction().commit();

        assert result instanceof HibernateTestEntity : "odd result returned fatal";

        assert UNIQUE_TEXT.equals(((HibernateTestEntity)result).getText()) : "queried entity did not match--did automatic txn fail?";
    }

    @Test
    public void testSimpleTransactionRollbackOnChecked() {
        try {
            injector.getInstance(TransactionalObject.class).runOperationInTxnThrowingChecked();
        } catch(IOException e) {
            //ignore
        }

        Session session = injector.getInstance(Session.class);
        assert !session.getTransaction().isActive() : "Session was not closed by transactional service (rollback didnt happen?)";

        //test that the data has been stored
        session.beginTransaction();
        Object result = session.createCriteria(HibernateTestEntity.class).add(Expression.eq("text", TRANSIENT_UNIQUE_TEXT)).uniqueResult();
        session.getTransaction().commit();

        assert null == result : "a result was returned! rollback sure didnt happen!!!";
    }

    @Test
    public void testSimpleTransactionRollbackOnCheckedExcepting() {
        Exception ex = null;
        try {
            injector.getInstance(TransactionalObject.class).runOperationInTxnThrowingCheckedExcepting();
        } catch(IOException e) {
            //ignore
            ex = e;

        }
        assert null != ex: "Exception was not thrown by test txn-al method!";

        Session session = injector.getInstance(Session.class);
        assert !session.getTransaction().isActive() : "Txn was not closed by transactional service (commit didnt happen?)";

        //test that the data has been stored
        session.beginTransaction();
        Object result = session.createCriteria(HibernateTestEntity.class).add(Expression.eq("text", UNIQUE_TEXT_2)).uniqueResult();
        session.getTransaction().commit();

        assert null != result : "a result was not returned! rollback happened anyway (exceptOn failed)!!!";
    }

    @Test
    public void testSimpleTransactionRollbackOnUnchecked() {
        try {
            injector.getInstance(TransactionalObject.class).runOperationInTxnThrowingUnchecked();
        } catch(RuntimeException re) {
            //ignore
        }

        Session session = injector.getInstance(Session.class);
        assert !session.getTransaction().isActive() : "Session was not closed by transactional service (rollback didnt happen?)";

        //test that the data has been stored
        session.beginTransaction();
        Object result = session.createCriteria(HibernateTestEntity.class).add(Expression.eq("text", TRANSIENT_UNIQUE_TEXT)).uniqueResult();
        session.getTransaction().commit();

        assert null == result : "a result was returned! rollback sure didnt happen!!!";
    }


    public static class TransactionalObject {
        @Inject Session session;

        @Transactional
        public void runOperationInTxn() {
            HibernateTestEntity entity = new HibernateTestEntity();
            entity.setText(UNIQUE_TEXT);
            session.persist(entity);
        }

        @Transactional(rollbackOn = IOException.class)
        public void runOperationInTxnThrowingChecked() throws IOException {
            HibernateTestEntity entity = new HibernateTestEntity();
            entity.setText(TRANSIENT_UNIQUE_TEXT);
            session.persist(entity);
            
            throw new IOException();
        }

        @Transactional(rollbackOn = IOException.class, exceptOn = FileNotFoundException.class)
        public void runOperationInTxnThrowingCheckedExcepting() throws IOException {
            HibernateTestEntity entity = new HibernateTestEntity();
            entity.setText(UNIQUE_TEXT_2);
            session.persist(entity);
            
            throw new FileNotFoundException();
        }
        
        @Transactional
        public void runOperationInTxnThrowingUnchecked() {
            HibernateTestEntity entity = new HibernateTestEntity();
            entity.setText(TRANSIENT_UNIQUE_TEXT);
            session.persist(entity);

            throw new IllegalStateException();
        }
    }
}
