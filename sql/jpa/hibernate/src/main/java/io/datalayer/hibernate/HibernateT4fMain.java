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
package io.datalayer.hibernate;


import io.datalayer.hibernate.model.Employee;
import io.datalayer.hibernate.util.HibernateUtil;
import io.datalayer.server.db.DatabaseServerControlRunnable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateT4fMain {

	/**
	 * @param args
	 */
	public static void main(String... args) throws Exception {
		
		Thread databaseThread = new Thread(new DatabaseServerControlRunnable());
		databaseThread.start();
		
		/** Getting the Session Factory and session */
		SessionFactory session = HibernateUtil.getSessionFactory();
		Session sess = session.getCurrentSession();
		/** Starting the Transaction */
		Transaction tx = sess.beginTransaction();
		/** Creating Pojo */
		Employee pojo = new Employee();
		pojo.setId(new Integer(5));
		pojo.setName("XYZ");
		/** Saving POJO */
		sess.save(pojo);
		/** Commiting the changes */
		tx.commit();
		System.out.println("Record Inserted");	
		/** Closing Session */
		session.close();
		
	}

}
