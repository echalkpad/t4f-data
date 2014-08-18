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
package org.apache.activemq.book.ch14.advisory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.command.ActiveMQDestination;

public class Test {
    protected static String brokerURL = "tcp://localhost:61616";
    protected static transient ConnectionFactory factory;
    protected transient Connection connection;
    protected transient Session session;
    
    private String jobs[] = new String[]{"suspend", "delete"};
    
    public Test() throws Exception {
    	factory = new ActiveMQConnectionFactory(brokerURL);
    	connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }
    
	public static void main(String... args) throws Exception {
		Test advisory = new Test();
		Session session = advisory.getSession();
    	for (String job : advisory.jobs) {
    		
    		ActiveMQDestination destination = (ActiveMQDestination)session.createQueue("JOBS." + job);
    		
    		Destination consumerTopic = AdvisorySupport.getConsumerAdvisoryTopic(destination);
    		System.out.println("Subscribing to advisory " + consumerTopic);
    		MessageConsumer consumerAdvisory = session.createConsumer(consumerTopic);
    		consumerAdvisory.setMessageListener(new ConsumerAdvisoryListener());
    		
    		Destination noConsumerTopic = AdvisorySupport.getNoQueueConsumersAdvisoryTopic(destination);
    		System.out.println("Subscribing to advisory " + noConsumerTopic);
    		MessageConsumer noConsumerAdvisory = session.createConsumer(noConsumerTopic);
    		noConsumerAdvisory.setMessageListener(new NoConsumerAdvisoryListener());
    		
    	}
	}

	public Session getSession() {
		return session;
	}
}
