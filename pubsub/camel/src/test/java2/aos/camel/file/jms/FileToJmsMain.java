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
package aos.camel.file.jms;

import java.io.File;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;

public class FileToJmsMain {
	
	public static void main(String... args) throws Exception {
		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");

		CamelContext context = new DefaultCamelContext();

		context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
		context.addRoutes(new CamelFileToJmsRouteBuilder());

		context.start();
		
		Thread.sleep(10000);

		MockEndpoint m1 = context.getEndpoint("mock:results", MockEndpoint.class);
		Exchange ex = m1.getExchanges().get(0);
		File file = ex.getIn().getBody(File.class);
		System.out.println(file.getAbsolutePath());
		
		context.stop();

	}
	
}

class CamelFileToJmsRouteBuilder extends RouteBuilder {
	
    @Override
    public void configure() {
//    	from("ftp://rider.com/orders?username=rider&password=secret")
        from("file:data/inbox?noop=true")
        .process(new DownloadLogger())
        .to("jms:incomingOrders");
    }
    
}
