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

import java.util.Properties;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import org.apache.activemq.ActiveMQConnectionFactory;

public class BatchSending {
    
    public void setOptimizeAcknowledge() throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        cf.setOptimizeAcknowledge(true);
    }
    public void setSessionAsyncOff() throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        cf.setAlwaysSessionAsync(false);
    }
    
    public void setNoCopyOnSend() throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        cf.setAlwaysSyncSend(true);
    }
    
    public void setAsyncSend() throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        cf.setCopyMessageOnSend(false);
    }
    public void setDeliveryMode() throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("Test.Transactions");
        MessageProducer producer = session.createProducer(topic);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    }
    public void sendTransacted() throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
        Topic topic = session.createTopic("Test.Transactions");
        MessageProducer producer = session.createProducer(topic);
        int count =0;
        for (int i =0; i < 1000; i++) {
           Message message = session.createTextMessage("message " + i);
           producer.send(message);
           if (i!=0 && i%10==0){
              session.commit();
           }
        }
    }
    public void setPrefetchProperties() throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        Properties props = new Properties();
        props.setProperty("prefetchPolicy.queuePrefetch", "1000");
        props.setProperty("prefetchPolicy.queueBrowserPrefetch", "500");
        props.setProperty("prefetchPolicy.topicPrefetch", "60000");
        props.setProperty("prefetchPolicy.durableTopicPrefetch", "100");
        cf.setProperties(props);
       
    }

    public void sendNonTransacted() throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("Test.Transactions");
        MessageProducer producer = session.createProducer(topic);
        int count =0;
        for (int i =0; i < 1000; i++) {
           Message message = session.createTextMessage("message " + i);
           producer.send(message);
        }
    }
}
