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
package io.datalayer.cassandra.cql;

import io.datalayer.cassandra.support.AosEmbeddedCassandra;

import java.io.IOException;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CqlClientTest {
    private static final String HOST = "localhost";
    private static final int PORT = 9170;
    private static final String CLUSTER_NAME = "TestCluster";
    private static final String KEYSPACE_NAME = "Keyspace1";
    private AosEmbeddedCassandra serverHelper;
    private Cluster cluster;
    private StringSerializer stringSerializer = StringSerializer.get();

    @Before
    public void startCassandra() throws TTransportException, IOException, InterruptedException, ConfigurationException {
        // serverHelper = new EmbeddedServerHelper();
        // serverHelper.setup();
        // cluster = HFactory.getOrCreateCluster(CLUSTER_NAME, new
        // CassandraHostConfigurator(HOST + ":" + PORT));
    }

    @After
    public void stopCassandra() {
        // EmbeddedServerHelper.teardown();
    }

    @Test
    public void testCQL() throws InvalidRequestException, UnavailableException, TimedOutException, TException {
        // Keyspace keyspace = HFactory.createKeyspace(KEYSPACE_NAME, cluster);
        // CqlQuery<String, String, String> cqlQuery = new CqlQuery<String,
        // String, String>(keyspace,
        // stringSerializer,stringSerializer,stringSerializer);
        // cqlQuery.setQuery("select * from users");
        // QueryResult<CqlRows<String, String, String>> result =
        // cqlQuery.execute();
        // if (result != null && result.get() != null) {
        // List<Row<String, String, String>> list = result.get().getList();
        // for (Row row : list) {
        // System.out.println(".");
        // List columns = row.getColumnSlice().getColumns();
        // for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
        // HColumn column = (HColumn) iterator.next();
        // System.out.print(column.getName() + ":" + column.getValue()
        // + "\t");
        // }
        // System.out.println("");
        // }
        // }
    }

}
