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
package org.apache.activemq.book.ch7.sync;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

public class Server implements MessageListener {

	private String brokerUrl = "tcp://0.0.0.0:61616";
	private String requestQueue = "requests";
	
	private BrokerService broker;
	private Session session;
	private MessageProducer producer;
	private MessageConsumer consumer;
	

	public void start() throws Exception {
		createBroker();
		setupConsumer();
	}

	private void createBroker() throws Exception {
        broker = new BrokerService();
        broker.setPersistent(false);
        broker.setUseJmx(false);
        broker.addConnector(brokerUrl);
        broker.start();		
	}

	private void setupConsumer() throws JMSException {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		
		Connection connection;
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination adminQueue = session.createQueue(requestQueue);

		producer = session.createProducer(null);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		consumer = session.createConsumer(adminQueue);
		consumer.setMessageListener(this);
	}
	
	public void stop() throws Exception {
		producer.close();
		consumer.close();
		session.close();
		broker.stop();
	}
	
	public void onMessage(Message message) {
        try {
            TextMessage response = this.session.createTextMessage();
            if (message instanceof TextMessage) {
                TextMessage txtMsg = (TextMessage) message;
                String messageText = txtMsg.getText();
                response.setText(handleRequest(messageText));
            }

            response.setJMSCorrelationID(message.getJMSCorrelationID());

            producer.send(message.getJMSReplyTo(), response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
	}
	
	public String handleRequest(String messageText) {
		return "Response to '" + messageText + "'";
	}
	
	public static void main(String... args) throws Exception {
		Server server = new Server();
		server.start();
		
		System.out.println();
		System.out.println("Press any key to stop the server");
		System.out.println();
		
		System.in.read();
		
		server.stop();
	}

}
