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

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.AfterClass;
import com.google.inject.Guice;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.wideplay.warp.persist.PersistenceService;
import com.wideplay.warp.persist.UnitOfWork;
import com.wideplay.warp.persist.WorkManager;

import com.wideplay.codemonkey.web.startup.Initializer;

import javax.jdo.PersistenceManagerFactory;
import javax.jdo.PersistenceManager;
import java.util.Properties;

/**
 * @author Miroslav Genov (mgenov@gmail.com)
 */
@Test(suiteName = "jdo")
public class CustomPropsPersistenceManagerFactoryProvisionTest {
    private Injector injector;


    @BeforeTest
    public void pre() {
        injector = Guice.createInjector(PersistenceService.usingJdo()
                .across(UnitOfWork.REQUEST)
                .forAll(Matchers.any())
                .buildModule(),
                new AbstractModule() {

                    protected void configure() {
                        bindConstant().annotatedWith(JdoUnit.class).to("testFactory");
                        bind(Properties.class).annotatedWith(JdoUnit.class)
                                .toInstance(Initializer.loadProperties("jdo.properties"));
                    }
                });
    }

    @AfterTest
    public final void post() {
        injector.getInstance(WorkManager.class).endWork();
    }

    @AfterClass
    public final void postClass() {
        injector.getInstance(PersistenceManagerFactory.class).close();
    }

    @Test
    public void testPersistenceManagerCreateOnInjection() {

        assert injector.getInstance(JdoPersistenceService.class)
                .equals(injector.getInstance(JdoPersistenceService.class)) : "SINGLETON VIOLATION " + JdoPersistenceService.class.getName();

        //startup persistence
        injector.getInstance(PersistenceService.class)
                .start();

        //obtain pm
        assert !injector.getInstance(PersistenceManager.class).isClosed() : "PM is not open!";
    }


}
