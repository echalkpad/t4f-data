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
package aos.camel.jetty;

import java.io.InputStream;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpEndpoint;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;

/**
 *
 */
public class CamelJettySpl1 {

	public static void main(String... args) throws Exception {

		CamelContext context = new DefaultCamelContext();

		context.addRoutes(new RouteBuilder() {

			public void configure() {

				from("direct:start")

				.process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						System.out.println(exchange.getIn().getBody().getClass().getCanonicalName() + ": " + exchange.getIn().getBody());
					}})

                .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.POST))
		        
                 .from("jetty:http://localhost:80/myapp/myservice")
                 
                 .process(new Processor(){

                	 public void process(Exchange exchange) throws Exception {

//             	        HttpServletRequest req = exchange.getIn().getBody(HttpServletRequest.class);
             	        HttpServletRequest req = (HttpServletRequest) exchange.getIn().getHeader(Exchange.HTTP_SERVLET_REQUEST);
                	    System.out.println("httprequest=" + req);

                	    String body = exchange.getIn().getBody(String.class);
                	    System.out.println("bookid=123 -> body=" + body);

                	    exchange.getOut().setBody("<html><body>Book 123 is Camel in Action</body></html>");

                	    }

                 })
				
                .process(new Processor() {

                	public void process(Exchange exchange) throws Exception {

                		System.out.println(exchange.getIn().getBody());
                		System.out.println(exchange.getOut().getClass().getName());

                		HttpServletRequest request = exchange.getIn().getBody(HttpServletRequest.class);
                		System.out.println("httpRequest=" + request);
 			      	   request = exchange.getIn().getHeader(Exchange.HTTP_SERVLET_REQUEST, HttpServletRequest.class);
			    	   System.out.println("httpRequest=" + request);
                		HttpServletResponse response = exchange.getOut().getBody(HttpServletResponse.class);
                		System.out.println("httpResponse=" + response);
                		
                	}})

                .to("mock:results");

			}
		});

		context.start();
		
        ProducerTemplate template = context.createProducerTemplate();
        template.sendBody("direct:start", "Hello\nHow are you?");

        Thread.sleep(10000);

		HttpEndpoint httpEndpoint = context.getEndpoint("http://www.google.com/search?q=camel", HttpEndpoint.class);
		System.out.println(httpEndpoint.getClientParams().getVersion());

		MockEndpoint m1 = context.getEndpoint("mock:results", MockEndpoint.class);
		
		System.out.println(m1.getExchanges().size());
		for (Exchange exchange: m1.getReceivedExchanges()) {
			System.out.println(exchange.getFromEndpoint().getEndpointUri());
			System.out.println(exchange.getIn());
			System.out.println(exchange.getOut());
		}
		
		context.stop();

	}

}
