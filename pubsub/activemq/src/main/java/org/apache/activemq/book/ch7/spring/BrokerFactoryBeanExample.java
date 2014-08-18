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

import org.apache.activemq.book.ch6.Publisher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class BrokerFactoryBeanExample {

	public static void main(String... args) throws Exception {
//    	if (args.length == 0) {
//    		System.err.println("Please define a configuration file!");
//    		return;
//    	}
    	String config = "org/apache/activemq/book/ch7/pure-spring.xml";
    	System.out.println("Starting broker with the following configuration: " + config);
		System.setProperty("activemq.base", System.getProperty("user.dir"));
		ApplicationContext context = new ClassPathXmlApplicationContext(config);
		
		Publisher publisher = new Publisher();
		for (int i = 0; i < 100; i++) {
			publisher.sendMessage(new String[]{"JAVA", "IONA"});
		}
		
	}

}
