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
 * @version $Revision: 165 $
 */
public class PurchaseOrderVelocityTest extends CamelTestSupport {

    @Test
    public void testVelocity() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:mail");
        mock.expectedMessageCount(1);
        mock.message(0).header("Subject").isEqualTo("Thanks for ordering");
        mock.message(0).header("From").isEqualTo("donotreply@riders.com");
        mock.message(0).body().contains("Thank you for ordering 1.0 piece(s) of Camel in Action at a cost of 4995.0.");

        PurchaseOrder order = new PurchaseOrder();
        order.setName("Camel in Action");
//        order.setPrice(4995);
        order.setAmount(1);

        template.sendBody("direct:mail", order);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:mail")
                    .setHeader("Subject", constant("Thanks for ordering"))
                    .setHeader("From", constant("donotreply@riders.com"))
                    .to("velocity://camelinaction/mail.vm")
                    .to("log:mail")
                    .to("mock:mail");
            }
        };
    }

}
