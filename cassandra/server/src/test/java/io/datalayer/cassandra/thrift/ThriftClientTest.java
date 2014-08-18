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
package io.datalayer.cassandra.thrift;

import io.datalayer.cassandra.support.AosEmbeddedCassandra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.cassandra.locator.SimpleStrategy;
import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.CfDef;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.CqlResult;
import org.apache.cassandra.thrift.CqlRow;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KsDef;
import org.apache.cassandra.thrift.SchemaDisagreementException;
import org.apache.cassandra.thrift.TBinaryProtocol;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ThriftClientTest {
    private static final String HOST = "localhost";
    private static final int PORT = 9170;
    private AosEmbeddedCassandra serverHelper;

    @Before
    public void startCassandra() throws TTransportException, IOException, InterruptedException, ConfigurationException {
        serverHelper = new AosEmbeddedCassandra();
        serverHelper.setup();
    }

    @After
    public void stopCassandra() throws IOException {
        AosEmbeddedCassandra.teardown();
    }

    @Test
    public void testThrift() throws InvalidRequestException, SchemaDisagreementException, TException, UnavailableException, TimedOutException {

         String keyspaceSUIDKsp = "SUIDKsp";
        
         System.out.println("Creating new keyspace: "+ keyspaceSUIDKsp);
         KsDef ksSUIDKsp = new KsDef();
         ksSUIDKsp.setName(keyspaceSUIDKsp);
         ksSUIDKsp.setReplication_factor(1);
         ksSUIDKsp.setStrategy_class(SimpleStrategy.class.getName());
        
         List<CfDef> cfSUIDKspList = new ArrayList<CfDef>();
         CfDef cfAuckland = new CfDef();
         cfAuckland.setColumn_type("Super");
         cfAuckland.setName("auckland");
         cfAuckland.setKeyspace(keyspaceSUIDKsp);
         cfAuckland.setComparator_type("UTF8Type");
         cfSUIDKspList.add(cfAuckland);
         ksSUIDKsp.setCf_defs(cfSUIDKspList);
        
         TTransport tr = new TSocket(HOST, PORT);
         TFramedTransport tf = new TFramedTransport(tr);
         TProtocol proto = new TBinaryProtocol(tf);
         Cassandra.Client client = new Cassandra.Client(proto);
         tr.open();

         client.system_drop_keyspace(keyspaceSUIDKsp);
         client.system_add_keyspace(ksSUIDKsp);
        
         CfDef cfWell = new CfDef();
         cfWell.setColumn_type("Super");
         cfWell.setName("welly");
         cfWell.setKeyspace(keyspaceSUIDKsp);
         cfWell.setComparator_type("UTF8Type");
        
         client.set_keyspace("SUIDKsp");
         client.system_add_column_family(cfWell);
        
         CqlResult result = client.execute_cql_query(null, null);
         List<CqlRow> rows = result.rows;
         for (CqlRow row: rows) {
             List<Column> columns = row.getColumns();
             for (Column column: columns) {
                 column.getName();
                 column.getTimestamp();
                 column.getValue();
             }
         }

    }

}
