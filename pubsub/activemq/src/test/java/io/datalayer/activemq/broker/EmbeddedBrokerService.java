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
package io.datalayer.activemq.broker;

import io.datalayer.activemq.producer.DefaultQueueSender;

import javax.jms.Connection;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.jmx.ManagementContext;
import org.apache.activemq.command.ActiveMQQueue;

/**
 * A helper class which can be handy for running a broker in your IDE from the
 * activemq-core module.
 * 
 * @version $Revision: 564814 $
 */
public final class EmbeddedBrokerService {

	protected static boolean createConsumers;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String... args) throws Exception {

		BrokerService broker = new BrokerService();
		broker.setPersistent(false);

		// for running on Java 5 without mx4j
		ManagementContext managementContext = broker.getManagementContext();
		managementContext.setFindTigerMbeanServer(true);
		managementContext.setUseMBeanServer(true);
		managementContext.setCreateConnector(false);

		broker.setUseJmx(true);
		// broker.setPlugins(new BrokerPlugin[] { new ConnectionDotFilePlugin(), new UDPTraceBrokerPlugin() });
		broker.addConnector("tcp://localhost:61616");
		broker.addConnector("stomp://localhost:61613");
		broker.start();

        // now lets wait forever to avoid the JVM terminating immediately
        Object lock = new Object();
        synchronized (lock) {
            lock.wait();
        }
        
	}

}
