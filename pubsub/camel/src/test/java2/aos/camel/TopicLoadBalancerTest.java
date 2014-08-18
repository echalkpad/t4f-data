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
package aos.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Demonstrates how to use the Load Balancer EIP pattern.
 * <p/>
 * Using the topic strategy.
 *
 * @version $Revision: 137 $
 */
public class TopicLoadBalancerTest extends CamelTestSupport {

    @Test
    public void testLoadBalancer() throws Exception {
        // both mocks should get all the messages as its topic strategy
        MockEndpoint a = getMockEndpoint("mock:a");
        a.expectedMessageCount(4);

        MockEndpoint b = getMockEndpoint("mock:b");
        b.expectedMessageCount(4);

        // send in 4 messages
        template.sendBody("direct:start", "Hello");
        template.sendBody("direct:start", "Camel rocks");
        template.sendBody("direct:start", "Cool");
        template.sendBody("direct:start", "Bye");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    // use load balancer with topic strategy
                    .loadBalance().topic()
                        // this is the 2 processors which we will balance across
                        .to("seda:a").to("seda:b")
                    .end();

                // service A
                from("seda:a")
                    .log("A received: ${body}")
                    .to("mock:a");

                // service B
                from("seda:b")
                    .log("B received: ${body}")
                    .to("mock:b");
            }
        };
    }

}
