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
package io.datalayer.cassandra.hector;

import io.datalayer.cassandra.support.AosEmbeddedCassandra;

import java.io.IOException;

import me.prettyprint.cassandra.connection.HConnectionManager;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for test cases that need access to EmbeddedServerHelper
 */
public abstract class AbstractCassandraDaoTest {
    private static final Logger log = LoggerFactory.getLogger(AbstractCassandraDaoTest.class);
    private static AosEmbeddedCassandra embedded;

    protected HConnectionManager connectionManager;
    protected CassandraHostConfigurator cassandraHostConfigurator;
    protected String clusterName = "TestCluster";

    /**
     * Set embedded cassandra up and spawn it in a new thread.
     * 
     * @throws TTransportException
     * @throws IOException
     * @throws InterruptedException
     */
    @BeforeClass
    public static void setup() throws TTransportException, IOException, InterruptedException, ConfigurationException {
        log.info("in setup of BaseEmbedded.Test");
        embedded = new AosEmbeddedCassandra();
        embedded.setup();
    }

    @AfterClass
    public static void teardown() throws IOException {
        AosEmbeddedCassandra.teardown();
        embedded = null;
    }

    protected void setupClient() {
        cassandraHostConfigurator = new CassandraHostConfigurator("127.0.0.1:9170");
        connectionManager = new HConnectionManager(clusterName, cassandraHostConfigurator);
    }

    protected CassandraHostConfigurator getCHCForTest() {
        CassandraHostConfigurator chc = new CassandraHostConfigurator("127.0.0.1:9170");
        chc.setMaxActive(2);
        return chc;
    }
}
