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
package io.datalayer.sql.model;

import static org.junit.Assert.assertTrue;
import io.datalayer.sql.model.Family;
import io.datalayer.sql.model.Person;
import io.datalayer.sql.server.DatabaseServerControlRunnable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EclipseLinkJPATest {

	private static final String PERSISTENCE_UNIT_NAME = "people";
	
	private EntityManagerFactory factory;

	@BeforeClass
	public static void beforeClass() throws Exception {
		Thread databaseServerControlThread = new Thread(new DatabaseServerControlRunnable(), "DatabaseServerControlThread");
		databaseServerControlThread.start();
	}
		
	@Before
	public void setUp() throws Exception {
		
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		
		EntityManager em = factory.createEntityManager();

		// Begin a new local transaction so that we can persist a new entity
		em.getTransaction().begin();

		// Read the existing entries
		Query q = em.createQuery("select m from Person m");
		// Persons should be empty

		// Do we have entries?
		boolean createNewEntries = (q.getResultList().size() == 0);

		// No, so lets create new entries
		if (createNewEntries) {
			assertTrue(q.getResultList().size() == 0);
			Family family = new Family();
			family.setDescription("Family for the Knopfs");
			em.persist(family);
			for (int i = 0; i < 40; i++) {
				Person person = new Person();
				person.setFirstName("Jim_" + i);
				person.setLastName("Knopf_" + i);
				em.persist(person);
				// First we have to persists the job
				// Now persists the new person
				family.getMembers().add(person);
				em.persist(person);
				em.persist(family);
			}
		}

		// Commit the transaction, which will cause the entity to
		// be stored in the database
		em.getTransaction().commit();

		// It is always good practice to close the EntityManager so that
		// resources are conserved.
		em.close();
		
		System.out.println("Finished.");

	}
		
	@Test
	public void checkAvailablePeople() {

		// Now lets check the database and see if the created entries are there
		// Create a fresh, new EntityManager
		EntityManager em = factory.createEntityManager();

		// Perform a simple query for all the Message entities
		Query q = em.createQuery("select m from Person m");

		// We should have 40 Persons in the database
		assertTrue(q.getResultList().size() == 40);

		em.close();
	}

	@Test
	public void checkFamily() {
		EntityManager em = factory.createEntityManager();
		// Go through each of the entities and print out each of their
		// messages, as well as the date on which it was created
		Query q = em.createQuery("select f from Family f");

		// We should have one family with 40 persons
		assertTrue(q.getResultList().size() == 1);
		assertTrue(((Family) q.getSingleResult()).getMembers().size() == 40);
		em.close();
	}

	@Test(expected = javax.persistence.NoResultException.class)
	public void deletePerson() {
		EntityManager em = factory.createEntityManager();
		// Begin a new local transaction so that we can persist a new entity
		em.getTransaction().begin();
		Query q = em
				.createQuery("SELECT p FROM Person p WHERE p.firstName = :firstName AND p.lastName = :lastName");
		q.setParameter("firstName", "Jim_1");
		q.setParameter("lastName", "Knopf_!");
		Person user = (Person) q.getSingleResult();
		em.remove(user);
		em.getTransaction().commit();
		Person person = (Person) q.getSingleResult();
		// Begin a new local transaction so that we can persist a new entity

		em.close();
		
	}
}
