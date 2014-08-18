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
package org.apache.activemq.book.ch7.spring;

import java.util.Hashtable;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.jms.core.MessageCreator;

public class StockMessageCreator implements MessageCreator {

	private int MAX_DELTA_PERCENT = 1;
	private Map<Destination, Double> LAST_PRICES = new Hashtable<Destination, Double>();

	Destination stock;

	public StockMessageCreator(Destination stock) {
		this.stock = stock;
	}

	public Message createMessage(Session session) throws JMSException {
		Double value = LAST_PRICES.get(stock);
		if (value == null) {
			value = new Double(Math.random() * 100);
		}

		// lets mutate the value by some percentage
		double oldPrice = value.doubleValue();
		value = new Double(mutatePrice(oldPrice));
		LAST_PRICES.put(stock, value);
		double price = value.doubleValue();

		double offer = price * 1.001;

		boolean up = (price > oldPrice);
		MapMessage message = session.createMapMessage();
		message.setString("stock", stock.toString());
		message.setDouble("price", price);
		message.setDouble("offer", offer);
		message.setBoolean("up", up);
		System.out.println("Sending: " + ((ActiveMQMapMessage)message).getContentMap() + " on destination: " + stock);
		return message;
	}

	protected double mutatePrice(double price) {
		double percentChange = (2 * Math.random() * MAX_DELTA_PERCENT)
				- MAX_DELTA_PERCENT;

		return price * (100 + percentChange) / 100;
	}

}
