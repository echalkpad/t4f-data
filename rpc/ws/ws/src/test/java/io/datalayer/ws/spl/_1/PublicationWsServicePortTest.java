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
package io.datalayer.ws.spl._1;

import io.aos.ws.spl._1.SystemError;

import java.net.MalformedURLException;

import org.junit.Test;

/**
 * Test the Publication WEB Service as a client.
 */
public class PublicationWsServicePortTest {

    /**
     * @throws SystemError
     * @throws MalformedURLException
     */
    @Test
    public void testWsServicePortSendMessage() throws SystemError, MalformedURLException {

        // MrService ws = new MrService();
        // mrPort = ws.getMrPortSoap11();
        // BindingProvider bindingProvider = (BindingProvider) mrPort;
        // bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        // END_POINT_ADDRESS);

        // JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        // factory.setServiceClass(PublicationPortType.class);
        // factory.setAddress("http://localhost:8080/t4f-ws-spl-1-ws/publish/test");
        // PublicationPortType client = (PublicationPortType) factory.create();
        // // ((BindingProvider)
        // publicationWsServicePort).getRequestContext().put(BindingProvider.USERNAME_PROPERTY,
        // "eh/username");
        // // ((BindingProvider)
        // publicationWsServicePort).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY,
        // "testrole");
        // PublicationMessageType p = new PublicationMessageType();
        // client.sendMessage(p);

    }

}
