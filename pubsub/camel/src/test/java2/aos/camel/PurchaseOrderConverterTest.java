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

import junit.framework.TestCase;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import io.aos.camel.PurchaseOrderImmutable;

/**
 * @version $Revision: 59 $
 */
public class PurchaseOrderConverterTest extends TestCase {

    public void testPurchaseOrderConverter() throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").convertBodyTo(PurchaseOrderImmutable.class);
            }
        });
        context.start();

        ProducerTemplate template = context.createProducerTemplate();

        byte[] data = "##START##AKC4433   179.95    3##END##".getBytes();
        PurchaseOrderImmutable order = template.requestBody("direct:start", data, PurchaseOrderImmutable.class);
        assertNotNull(order);

        System.out.println(order);

        assertEquals("AKC4433", order.getName());
        assertEquals("179.95", order.getPrice().toString());
        assertEquals(3, order.getAmount());
    }

}
