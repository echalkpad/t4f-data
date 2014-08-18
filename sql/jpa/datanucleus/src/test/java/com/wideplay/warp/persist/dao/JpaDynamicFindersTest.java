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

package com.wideplay.warp.persist.dao;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.wideplay.warp.persist.PersistenceService;
import com.wideplay.warp.persist.Transactional;
import com.wideplay.warp.persist.UnitOfWork;
import com.wideplay.warp.persist.jpa.JpaTestEntity;
import com.wideplay.warp.persist.jpa.JpaUnit;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Dhanji R. Prasanna (dhanji@gmail.com)
 * Date: 4/06/2007
 * Time: 15:57:24
 * <p/>
 *
 * @author Dhanji R. Prasanna (dhanji@gmail.com)
 * @since 1.0
 */
public class JpaDynamicFindersTest {
    private Injector injector;
    private static final String TEXT_1 = "unique text1" + new Date();
    private static final String TEXT_2 = "unique text2" + new Date();

    @BeforeClass
    public void pre() {
        injector = Guice.createInjector(PersistenceService.usingJpa()
                .across(UnitOfWork.TRANSACTION)
                .addAccessor(JpaTestAccessor.class)
                .buildModule(),

                new AbstractModule() {

                    protected void configure() {
                        bindConstant().annotatedWith(JpaUnit.class).to("testUnit");
                    }
                });

        injector.getInstance(PersistenceService.class).start();
    }

    @AfterTest
    public void post() {
        injector.getInstance(EntityManagerFactory.class).close();
        injector = null;
    }

    @Test
    public void testListAll() {

        //set up some test data
        injector.getInstance(FinderDao.class).store();

        //now attempt to query it out
        JpaTestAccessor accessor = injector.getInstance(JpaTestAccessor.class);

        List<JpaTestEntity> results = accessor.listAll();

        assert results.size() >= 2 : "all results not returned!";

        assert results.get(0).getText().equals(TEXT_1) || results.get(0).getText().equals(TEXT_2) : "attribs not persisted correctly";
        assert results.get(1).getText().equals(TEXT_1) || results.get(1).getText().equals(TEXT_2) : "attribs not persisted correctly";
    }

    @Test
    public void testListAllDDD() {

        //set up some test data
        injector.getInstance(FinderDao.class).store();

        //now attempt to query it out

        List<JpaTestEntity> results = injector.getInstance(JpaTestEntity.class).listAll();

        assert null != results : "Finder did not intercept annotated DDD method!";

        assert results.size() >= 2 : "all results not returned!";

        assert results.get(0).getText().equals(TEXT_1) || results.get(0).getText().equals(TEXT_2) : "attribs not persisted correctly";
        assert results.get(1).getText().equals(TEXT_1) || results.get(1).getText().equals(TEXT_2) : "attribs not persisted correctly";
    }


    @Test
    public void testReturnAsSet() {

        //set up some test data
        injector.getInstance(FinderDao.class).store();

        //now attempt to query it out
        JpaTestAccessor accessor = injector.getInstance(JpaTestAccessor.class);

        Set<JpaTestEntity> results = accessor.set();

        assert results.size() >= 2 : "all results not returned!";

        for (JpaTestEntity result : results) {
            assert result.getText().equals(TEXT_1) || result.getText().equals(TEXT_2) : "attribs not persisted correctly";
        }
    }

    @Test
    public void testSingleResultFetchWithNamedParameter() {

        //set up some test data
        Long id = injector.getInstance(FinderDao.class).store().getId();

        //now attempt to query it out
        JpaTestAccessor accessor = injector.getInstance(JpaTestAccessor.class);

        JpaTestEntity result = accessor.fetch(id);

        assert null != result : "result not returned!";

        assert result.getId().equals(id) : "attribs not persisted correctly";
    }

    @Test
    public void testSingleResultFetchWithIndexedParameter() {

        //set up some test data
        Long id = injector.getInstance(FinderDao.class).store().getId();

        //now attempt to query it out
        JpaTestAccessor accessor = injector.getInstance(JpaTestAccessor.class);

        JpaTestEntity result = accessor.fetchById(id);

        assert null != result : "result not returned!";

        assert result.getId().equals(id) : "attribs not persisted correctly";
    }

    @Test
    public void testListParamemter() {

        //set up some test data
        Long id = injector.getInstance(FinderDao.class).store().getId();

        //now attempt to query it out
        JpaTestAccessor accessor = injector.getInstance(JpaTestAccessor.class);

        List<Long> list = new ArrayList<Long>();
        list.add(id);
        JpaTestEntity result = accessor.fetchByIdList(list);

        assert null != result : "result not returned!";

        assert result.getId().equals(id) : "attribs not persisted correctly";
    }

    @Test(expectedExceptions = ClassCastException.class)
    public void testUnnamedListParamemter() {

        //set up some test data
        Long id = injector.getInstance(FinderDao.class).store().getId();

        //now attempt to query it out
        JpaTestAccessor accessor = injector.getInstance(JpaTestAccessor.class);

        List<Long> list = new ArrayList<Long>();
        list.add(id);
        JpaTestEntity result = accessor.fetchByIdUnnamedList(list);

        assert null != result : "result not returned!";

        assert result.getId().equals(id) : "attribs not persisted correctly";
    }

    @Test(expectedExceptions = ClassCastException.class)
    public void testArrayParamemter() {

        //set up some test data
        Long id = injector.getInstance(FinderDao.class).store().getId();

        //now attempt to query it out
        JpaTestAccessor accessor = injector.getInstance(JpaTestAccessor.class);

        JpaTestEntity result = accessor.fetchByIdArray(new Long[]{id});

        assert null != result : "result not returned!";

        assert result.getId().equals(id) : "attribs not persisted correctly";
    }
    
    public static class FinderDao {
        private final EntityManager em;

        @Inject
        public FinderDao(EntityManager em) {
            this.em = em;
        }

        @Transactional
        JpaTestEntity store() {
            JpaTestEntity entity = new JpaTestEntity();
            entity.setText(TEXT_1);

            em.persist(entity);

            entity = new JpaTestEntity();
            entity.setText(TEXT_2);
            
            em.persist(entity);

            return entity;
        }
    }
}
