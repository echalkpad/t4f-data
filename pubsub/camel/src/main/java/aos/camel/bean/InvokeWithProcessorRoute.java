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
package aos.camel.bean;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * Using a Processor in the route to invoke HelloBean.
 */
public class InvokeWithProcessorRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:hello").process(new Processor() {
            public void process(Exchange exchange) throws Exception {
                // extract the name parameter from the Camel message which we
                // want to use
                // when invoking the bean
                String name = exchange.getIn().getBody(String.class);

                // now create an instance of the bean
                HelloBean hello = new HelloBean();
                // and invoke it with the name parameter
                String answer = hello.hello(name);

                // store the reply from the bean on the OUT message
                exchange.getOut().setBody(answer);
            }
        });
    }
}
