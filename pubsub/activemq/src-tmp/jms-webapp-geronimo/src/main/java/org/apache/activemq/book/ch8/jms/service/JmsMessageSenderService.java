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
package org.apache.activemq.book.ch8.jms.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.book.ch8.jms.domain.JmsMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * @author bsnyder
 * 
 */
public class JmsMessageSenderService {

    private JmsTemplate jmsTemplate;

    public void sendMessage(final JmsMessage bean) throws JMSException {

        if (bean.isPersistent()) {
            jmsTemplate.setDeliveryPersistent(bean.isPersistent());
        }

        if (0 != bean.getTimeToLive()) {
            jmsTemplate.setTimeToLive(bean.getTimeToLive());
        }

        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {

                TextMessage message = session.createTextMessage(bean
                        .getMessagePayload());

                if (bean.getReplyTo() != null && !bean.getReplyTo().equals("")) {
                    ActiveMQQueue replyToQueue = new ActiveMQQueue(bean.getReplyTo());
                    message.setJMSReplyTo(replyToQueue);
                }
                return message;
            }
        });
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

}
