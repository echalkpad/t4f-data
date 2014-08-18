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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import javax.jdo.PersistenceManagerFactory;
import javax.jdo.PersistenceManager;
import java.util.List;

/**
 * @author Miroslav Genov (mgenov@gmail.com)
 */
@Test(suiteName = "jdo")
public class PersistenceManagerProvisionTest {
    private Injector injector;

    @BeforeClass
    public void pre() {
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

        //startup persistence
        injector.getInstance(PersistenceService.class)
                .start();
    }


    @AfterClass
    public final void postClass() {
        injector.getInstance(PersistenceManagerFactory.class).close();
    }

    @Test
    public void testPersistenceManagerLifecyclePerTxn() {
        //obtain pm
        JdoDao dao = injector.getInstance(JdoDao.class);

        //obtain same pm again (bound to txn)
        JdoTestEntity te = new JdoTestEntity();

        dao.persist(te);

        //im not sure this hack works...
        assert !JdoDao.pm.equals(injector.getInstance(PersistenceManager.class))
                 : "Duplicate persistence managers crossing-scope";

        //try to start a new pm in a new txn
        dao = injector.getInstance(JdoDao.class);

        assert !dao.contains(te) : "PersistenceManager wasnt closed and reopened properly around txn (persistent object persists)";
    }

    @Test //a duplicate to try and induce static crossovers
    public void testPersistenceManagerLifecyclePerTxn2() {
        //obtain pm
        JdoDao dao = injector.getInstance(JdoDao.class);

        //obtain same pm again (bound to txn)
        JdoTestEntity te = new JdoTestEntity();

        dao.persist(te);

        //im not sure this hack works...
        assert JdoDao.pm != injector.getInstance(PersistenceManager.class) : "Duplicate persistence managers crossing-scope";

        //try to start a new pm in a new txn
        dao = injector.getInstance(JdoDao.class);

        assert !dao.contains(te) : "PersistenceManager wasnt closed and reopened properly around txn (persistent object persists)";
    }

    public static class JdoDao {
        static PersistenceManager pm;

        @Inject
        public JdoDao(PersistenceManager pm) {
            JdoDao.pm = pm;
        }

        @Transactional
        public <T> void persist(T t) {
            assert !pm.isClosed() : "pm is not open!";
            assert pm.currentTransaction().isActive() : "no active txn!";
            pm.makePersistent(t);

            List<T> result = (List<T>) pm.newQuery(t.getClass()).execute();

            assert result.size() > 0 : "Persisting object failed";
        }

        @Transactional
        public <T> boolean contains(T t) {
            List<T> result = (List<T>) pm.newQuery(t.getClass()).execute();
            return result.size() == 0;
        }
    }

}
