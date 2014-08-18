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
package org.apache.activemq.book.ch13;
import java.util.concurrent.atomic.AtomicInteger;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;

public class RealTimeDataFeed {
    
    public void startBroker() throws Exception{
       //By default a broker always listens on vm://<broker name>
        BrokerService broker = new BrokerService();
        broker.setBrokerName("fast");
        broker.getSystemUsage().getMemoryUsage().setLimit(64*1024*1024);
        //Set the Destination policies
        PolicyEntry policy = new PolicyEntry();
        //set a memory limit of 4mb for each destination
        policy.setMemoryLimit(4 * 1024 *1024);
        //disable flow control
        policy.setProducerFlowControl(false);

        PolicyMap pMap = new PolicyMap();
        //configure the policy
        pMap.setDefaultEntry(policy);

        broker.setDestinationPolicy(pMap);
        broker.addConnector("tcp://localhost:61616");
        broker.start();
    }
    
    public void dataFeed() throws Exception{
                
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("vm://fast");
        cf.setCopyMessageOnSend(false);
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("test.topic");
        final MessageProducer producer = session.createProducer(topic);
        //send non-persistent messages
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        for (int i =0; i < 1000000;i++) {
            TextMessage message = session.createTextMessage("Test:"+i);
            producer.send(message);  
        }
        System.err.println("SENT MESSAGES");
        
    }
    
    public void consumer() throws Exception{
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("failover://(tcp://localhost:61616)");
        cf.setAlwaysSessionAsync(false);
        cf.setOptimizeAcknowledge(true);
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("test.topic?consumer.prefetchSize=32766");
        MessageConsumer consumer = session.createConsumer(topic);
        //setup a counter - so we don't print every message
        final AtomicInteger count = new AtomicInteger();
        consumer.setMessageListener(new MessageListener() {

            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage)message;
                try {
                    if (count.incrementAndGet()%10000==0)
                   System.err.println("Got = " + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
                
            }
            
        });
        
    }
    
    public static void main(String... args) throws Exception {
        RealTimeDataFeed rtdf = new RealTimeDataFeed();
        rtdf.startBroker();
        rtdf.consumer();
        rtdf.dataFeed();
       
        Thread.sleep(5000);
    }
}
