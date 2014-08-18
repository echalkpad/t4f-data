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
package io.datalayer.sql.dao;

import io.datalayer.sql.dao.domain.Person;

import java.util.List;
import java.util.Iterator;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Simple test of the PersonDao
 */
public class PersonDaoTest extends TestCase {
    private ApplicationContext factory;

    public PersonDaoTest(String s) {
        super(s);
        factory = new ClassPathXmlApplicationContext("test-applicationContext.xml");
    }

    public void testCrud() throws Exception {

        // Create
        PersonDao personDao = getPersonDao();
        Person createPerson = new Person("Mellqvist", 88);
        personDao.create(createPerson);
        assertNotNull(createPerson.getId());
        Long id = createPerson.getId();

        restartSession();

        // Read
        Person foundPerson = personDao.read(id);
        assertEquals(createPerson.getWeight(), foundPerson.getWeight());

        restartSession();

        // Update
        Integer updateWeight = 90;
        foundPerson.setWeight(updateWeight);
        personDao.update(foundPerson);
        Person updatedPerson = personDao.read(id);
        assertEquals(updateWeight, updatedPerson.getWeight());

        restartSession();

        // Delete
        personDao.delete(updatedPerson);
        restartSession();
        assertNull(personDao.read(id));
    }

    public void testFindByName() throws Exception {
        PersonDao personDao = getPersonDao();
        Person person1 = new Person("Mellqvist", 88);
        personDao.create(person1);
        Person person2 = new Person("Doe", 80);
        personDao.create(person2);

        restartSession();

        List<Person> byName = personDao.findByName("Mellqvist");
        assertTrue(byName.size() == 1);
        assertEquals(person1.getWeight(), byName.get(0).getWeight());

        restartSession();

        personDao.delete(person1);
        personDao.delete(person2);
    }

    public void testIterateByWeight() throws Exception {
        PersonDao personDao = getPersonDao();
        Person person1 = new Person("Mellqvist", 88);
        personDao.create(person1);
        Person person2 = new Person("Doe", 80);
        personDao.create(person2);

        restartSession();

        Iterator<Person> byWeight = personDao.iterateByWeight(person1.getWeight());
        assertTrue(byWeight.hasNext());
        Person found = byWeight.next();
        assertEquals(person1.getWeight(), found.getWeight());

        restartSession();

        personDao.delete(person1);
        personDao.delete(person2);
    }

    protected void setUp() throws Exception {
        openSession();
    }

    protected void tearDown() throws Exception {
        closeSession();
    }

    private void openSession() {
        SessionFactory sessionFactory = getSessionFactory();
        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
    }

    private void closeSession() {
        SessionFactory sessionFactory = getSessionFactory();
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
        sessionHolder.getSession().flush();
        sessionHolder.getSession().close();
        SessionFactoryUtils.releaseSession(sessionHolder.getSession(), sessionFactory);
    }

    private void restartSession() {
        closeSession();
        openSession();
    }

    private SessionFactory getSessionFactory() {
        return (SessionFactory) factory.getBean("sessionFactory");
    }

    private PersonDao getPersonDao() {
        PersonDao personDao = (PersonDao) factory.getBean("personDao");
        return personDao;
    }
}
