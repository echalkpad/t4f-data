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
package org.apache.activemq.book.ch4;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.store.amq.AMQPersistenceAdapterFactory;

public class AMQStoreEmbeddedBroker {
    
    public void createEmbeddedBroker() throws Exception {
        
        BrokerService broker = new BrokerService();
        //initialize the PersistenceAdaptorFactory
        AMQPersistenceAdapterFactory persistenceFactory = new AMQPersistenceAdapterFactory();
        
        //set some properties on the factory
        persistenceFactory.setMaxFileLength(1024*16);
        persistenceFactory.setPersistentIndex(true);
        broker.setPersistenceFactory(persistenceFactory);
        
        //create a transport connector
        broker.addConnector("tcp://localhost:61616");
        //start the broker
        broker.start();
    }
}
