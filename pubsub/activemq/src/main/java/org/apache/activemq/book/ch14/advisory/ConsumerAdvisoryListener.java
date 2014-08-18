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

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.CommandTypes;
import org.apache.activemq.command.ConsumerId;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.DataStructure;
import org.apache.activemq.command.RemoveInfo;

public class ConsumerAdvisoryListener implements MessageListener {

	public void onMessage(Message message) {
		ActiveMQMessage msg = (ActiveMQMessage) message;
		DataStructure ds = msg.getDataStructure();
		if (ds != null) {
			switch (ds.getDataStructureType()) {
			case CommandTypes.CONSUMER_INFO:
				ConsumerInfo consumerInfo = (ConsumerInfo) ds;
				System.out.println("Consumer '" + consumerInfo.getConsumerId()
						+ "' subscribed to '" + consumerInfo.getDestination()
						+ "'");
				break;
			case CommandTypes.REMOVE_INFO:
				RemoveInfo removeInfo = (RemoveInfo) ds;
				ConsumerId consumerId = ((ConsumerId) removeInfo.getObjectId());
				System.out
						.println("Consumer '" + consumerId + "' unsubscribed");
				break;
			default:
				System.out.println("Unkown data structure type");
			}
		} else {
			System.out.println("No data structure provided");
		}
	}
}
