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
/*
 * Licensed to the AOS Community (AOS) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The AOS licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package io.datalayer.sql.jpa.common.hellojpa;

import hellojpa.Message;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * A very simple, stand-alone program that stores a new entity in the database
 * and then performs a query to retrieve it.
 */
public class HelloOpenJPAMain {

	@SuppressWarnings("unchecked")
	public static void main(String... args) throws Exception {
		
//		Thread databaseThread = new Thread(new DatabaseServerControlRunnable());
//		databaseThread.start();
		
		// Create a new EntityManagerFactory using the System properties.
		// The "hellojpa" name will be used to configure based on the
		// corresponding name in the META-INF/persistence.xml file
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("hellojpa", System.getProperties());

		// Create a new EntityManager from the EntityManagerFactory. The
		// EntityManager is the main object in the persistence API, and is
		// used to create, delete, and query objects, as well as access
		// the current transaction
		EntityManager em = factory.createEntityManager();

		// Begin a new local transaction so that we can persist a new entity
		em.getTransaction().begin();

		// Create and persist a new Message entity
		em.persist(new Message("Hello Persistence 1!"));
		em.persist(new Message("Hello Persistence 2!"));

		// Commit the transaction, which will cause the entity to
		// be stored in the database
		em.getTransaction().commit();

		// It is always good practice to close the EntityManager so that
		// resources are conserved.
		em.close();

		// Create a fresh, new EntityManager
		EntityManager em2 = factory.createEntityManager();

		// Perform a simple query for all the Message entities
		Query q = em2.createQuery("select m from Message m");

		// Go through each of the entities and print out each of their
		// messages, as well as the date on which it was created
		for (Message m : (List<Message>) q.getResultList()) {
			System.out.println(m.getMessage() + " (created on: " + m.getCreated() + ")");
		}

		// Again, it is always good to clean up after ourselves
		em2.close();
		
		factory.close();
		
	}
}
