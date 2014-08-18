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
package org.datanucleus.samples.jpa.tutorial;

import java.util.Iterator;
import java.util.List;

import javax.jdo.JDOEnhancer;
import javax.jdo.JDOHelper;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * Controlling application for the DataNucleus Tutorial using JPA. Uses the
 * "persistence-unit" called "Tutorial".
 */
public class JpaTutMain {
	
	public static void main(String... args) {
		
		enhance();
		
		// Create an EntityManagerFactory for this "persistence-unit"
		// See the file "META-INF/persistence.xml"
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Tutorial");
		
		System.out.println("DataNucleus Tutorial with JPA");
		System.out.println("=============================");

		// Persistence of a Product and a Book.
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			JpaProduct product = new JpaProduct("Sony Discman", "A standard discman from Sony", 200.00);
			em.persist(product);

			JpaBook book = new JpaBook("Lordddd of the Rings by Tolkien", "The classic story", 49.99, "JRR Tolkien", "12345678", "MyBooks Factory");
			em.persist(book);

			tx.commit();
			System.out.println("Product and Book have been persisted");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			em.close();
		}
		System.out.println("");

		// Perform some query operations
		em = emf.createEntityManager();
		tx = em.getTransaction();
		try {
			tx.begin();
			System.out.println("Executing Query for JpaProducts with price below 150.00");
			Query q = em.createQuery("SELECT p FROM Product p WHERE p.price < 150.00 ORDER BY p.price");
			List results = q.getResultList();
			Iterator iter = results.iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				System.out.println(">  " + obj);

				// Give an example of an update
				if (obj instanceof JpaBook) {
					JpaBook b = (JpaBook) obj;
					b.setDescription(b.getDescription() + " REDUCED");
				}
			}

			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			em.close();
		}
		System.out.println("");

		// Clean out the database
		em = emf.createEntityManager();
		tx = em.getTransaction();
		try {
			tx.begin();

			System.out.println("Deleting all JpaProducts from persistence");
			Query q = em.createQuery("DELETE FROM JPA_PRODUCT p");
//			int numberInstancesDeleted = q.executeUpdate();
//			System.out.println("Deleted " + numberInstancesDeleted + " products");

			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			em.close();
		}

		System.out.println("");
		System.out.println("End of Tutorial");
		
	}

	private static void enhance() {
		JDOEnhancer enhancer = JDOHelper.getEnhancer();
		enhancer.addPersistenceUnit("Tutorial");
		enhancer.setVerbose(true);
		enhancer.enhance();
	}

}
