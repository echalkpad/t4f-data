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

import java.util.HashMap;

import javax.jms.Destination;

import org.springframework.jms.core.JmsTemplate;


public class SpringPublisher {
	
    private JmsTemplate template;
    private int count = 10;
    private int total;
    private Destination[] destinations;
    private HashMap<Destination,StockMessageCreator> creators = new HashMap<Destination,StockMessageCreator>(); 
    
    public void start() {
        while (total < 1000) {
            for (int i = 0; i < count; i++) {
                sendMessage();
            }
            total += count;
            System.out.println("Published '" + count + "' of '" + total + "' price messages");
            try {
              Thread.sleep(1000);
            } catch (InterruptedException x) {
            }
          }	
    }
    
    protected void sendMessage() {
        int idx = 0;
        while (true) {
            idx = (int)Math.round(destinations.length * Math.random());
            if (idx < destinations.length) {
                break;
            }
        }
        Destination destination = destinations[idx];
        template.send(destination, getStockMessageCreator(destination));
    }
    
    private StockMessageCreator getStockMessageCreator(Destination dest) {
    	if (creators.containsKey(dest)) {
    		return creators.get(dest);
    	} else {
    		StockMessageCreator creator = new StockMessageCreator(dest);
    		creators.put(dest, creator);
    		return creator;
    	}
    }

	public JmsTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}

	public Destination[] getDestinations() {
		return destinations;
	}

	public void setDestinations(Destination[] destinations) {
		this.destinations = destinations;
	}
    

	
}
