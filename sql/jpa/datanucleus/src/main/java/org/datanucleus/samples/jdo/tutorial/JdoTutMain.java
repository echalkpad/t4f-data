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
package org.datanucleus.samples.jdo.tutorial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.JDOEnhancer;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.datanucleus.jdo.JDOPersistenceManagerFactory;
import org.datanucleus.store.rdbms.SchemaTool;

/**
 * Controlling application for the DataNucleus Tutorial using JDO. Relies on the
 * user defining a file "datanucleus.properties" to be in the CLASSPATH and to
 * include the JDO properties for the DataNucleus PersistenceManager.
 * 
 * @version $Revision: 1.1 $
 **/
public class JdoTutMain {
	
	public static void main(String... args) throws Exception {

     	enhance();

     	// Create a PersistenceManagerFactory for this datastore
		PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("testFactory");

//		createSchema(pmf);

		System.out.println("DataNucleus AccessPlatform with JDO");
		System.out.println("===================================");

		// Persistence of a Product and a Book.
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			System.out.println("Persisting products");
			JdoProduct product = new JdoProduct("Sony Discman", "A standard discman from Sony", 200.00);
			JdoBook book = new JdoBook("Lord of the Rings by Tolkien", "The classic story", 49.99, "JRR Tolkien", "12345678", "MyBooks Factory");
			pm.makePersistent(product);
			pm.makePersistent(book);

			tx.commit();
			System.out.println("Product and Book have been persisted");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
		System.out.println("");

		// Basic Extent of all Products
		pm = pmf.getPersistenceManager();
		tx = pm.currentTransaction();
		try {
			tx.begin();
			System.out.println("Retrieving Extent for Products");
			Extent e = pm.getExtent(JdoProduct.class, true);
			Iterator iter = e.iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				System.out.println(">  " + obj);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
		System.out.println("");

		// Perform some query operations
		pm = pmf.getPersistenceManager();
		tx = pm.currentTransaction();
		try {
			tx.begin();
			System.out.println("Executing Query for Products with price below 150.00");
			Extent e = pm.getExtent(JdoProduct.class, true);
			Query q = pm.newQuery(e, "price < 150.00");
			q.setOrdering("price ascending");
			Collection c = (Collection) q.execute();
			Iterator iter = c.iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				System.out.println(">  " + obj);

				// Give an example of an update
				if (obj instanceof JdoBook) {
					JdoBook b = (JdoBook) obj;
					b.setDescription("This book has been reduced in price!");
				}
			}

			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
		System.out.println("");
		/*
		 * // Clean out the database pm = pmf.getPersistenceManager(); tx =
		 * pm.currentTransaction(); try { tx.begin();
		 * System.out.println("Deleting all products from persistence"); Query q
		 * = pm.newQuery(Product.class); long numberInstancesDeleted =
		 * q.deletePersistentAll(); System.out.println("Deleted " +
		 * numberInstancesDeleted + " products");
		 * 
		 * tx.commit(); } catch (Exception e) { e.printStackTrace();
		 * System.exit(-1); } finally { if (tx.isActive()) { tx.rollback(); }
		 * pm.close(); }
		 */
		System.out.println("");
		System.out.println("End of Tutorial");
	}

	private static void enhance() {
		JDOEnhancer enhancer = JDOHelper.getEnhancer();
		enhancer.addClasses(JdoBook.class.getName(), JdoProduct.class.getName());
		enhancer.setVerbose(true);
		enhancer.enhance();
	}

	private static void createSchema(PersistenceManagerFactory pmf) {
		
		List classNames = new ArrayList();
		classNames.add(JdoBook.class.getName());
		classNames.add(JdoProduct.class.getName());
		SchemaTool schemaTool = new SchemaTool();
		
		try {
			// schemaTool.deleteSchema((JDOPersistenceManagerFactory) pmf,
			// classNames);
		} catch (Exception e) {
		}
		try {
			schemaTool.createSchema((JDOPersistenceManagerFactory) pmf, classNames);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
