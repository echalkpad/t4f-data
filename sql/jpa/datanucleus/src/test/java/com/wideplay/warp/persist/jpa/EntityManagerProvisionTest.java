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

package com.wideplay.warp.persist.jpa;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.wideplay.warp.persist.PersistenceService;
import com.wideplay.warp.persist.Transactional;
import com.wideplay.warp.persist.UnitOfWork;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Dhanji R. Prasanna (dhanji@gmail.com)
 * Date: 1/06/2007
 * Time: 11:40:36
 *
 * A test around providing sessions (starting, closing etc.)
 *
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 * @since 1.0
 */
@Test(suiteName = "jpa")
public class EntityManagerProvisionTest {
    private Injector injector;

    @BeforeClass
    public void pre() {
        injector = Guice.createInjector(PersistenceService.usingJpa()
            .across(UnitOfWork.TRANSACTION)
            .forAll(Matchers.any())
            .buildModule(),
                new AbstractModule() {

                    protected void configure() {
                        //tell Warp the name of the jpa persistence unit
                        bindConstant().annotatedWith(JpaUnit.class).to("testUnit");
                    }
                });

        //startup persistence
        injector.getInstance(PersistenceService.class)
                .start();
    }


    @AfterClass
    public final void postClass() {
        injector.getInstance(EntityManagerFactory.class).close();
    }

    @Test
    public void testEntityManagerLifecyclePerTxn() {
        //obtain em
        JpaDao dao = injector.getInstance(JpaDao.class);

        //obtain same em again (bound to txn)
        JpaTestEntity te = new JpaTestEntity();

        dao.persist(te);

        //im not sure this hack works...
        assert !JpaDao.em.equals(injector.getInstance(EntityManager.class))
                 : "Duplicate entity managers crossing-scope";

        //try to start a new em in a new txn
        dao = injector.getInstance(JpaDao.class);

        assert !dao.contains(te) : "EntityManager wasnt closed and reopened properly around txn (persistent object persists)";
    }

    @Test //a duplicate to try and induce static crossovers
    public void testEntityManagerLifecyclePerTxn2() {
        //obtain em
        JpaDao dao = injector.getInstance(JpaDao.class);

        //obtain same em again (bound to txn)
        JpaTestEntity te = new JpaTestEntity();

        dao.persist(te);

        //im not sure this hack works...
        assert !JpaDao.em.equals(injector.getInstance(EntityManager.class))
                 : "Duplicate entity managers crossing-scope";

        //try to start a new em in a new txn
        dao = injector.getInstance(JpaDao.class);

        assert !dao.contains(te) : "EntityManager wasnt closed and reopened properly around txn (persistent object persists)";
    }

    public static class JpaDao {
        static EntityManager em;

        @Inject
        public JpaDao(EntityManager em) {
            JpaDao.em = em;
        }
        
        @Transactional
        public <T> void persist(T t) {
            assert em.isOpen() : "em is not open!";
            assert em.getTransaction().isActive() : "no active txn!";
            em.persist(t);

            assert em.contains(t) : "Persisting object failed";
        }

        @Transactional
        public <T> boolean contains(T t) {
            return em.contains(t);
        }
    }
}
