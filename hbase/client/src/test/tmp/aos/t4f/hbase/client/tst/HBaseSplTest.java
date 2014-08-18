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
package aos.t4f.hbase.client.tst;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.junit.Test;

public class HBaseSplTest {

    private static final Logger LOGGER = Logger.getLogger(HBaseSplTest.class);
    
    private static Configuration configuration;
    
    private static final String HDFS_NAMENODE_HOST_NAME = "ppc005";
    
    private static final String HDFS_NAMENODE_PORT_NUMBER = "54310";
    
    private static final String HBASE_MASTER_HOST_NAME = "ppc005";
    
    private static final String HBASE_MASTER_PORT_NUMBER = "60000";
    
    private static final String TABLE_NAME = "table6";

    private static final String COLUMN_FAMILY_NAME = "cf1";
    
    private static final String COLUMN_NAME = "column1";

    private static final String KEY_NAME_PREFIX = "key";
    
    private static final String KEY_VALUE_PREFIX = "value";
    
    private static int NUMBER_OF_KEYS = 10000;

    @Test
    public void test() throws IOException {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.rootdir", "hdfs://" + HDFS_NAMENODE_HOST_NAME + ":" + HDFS_NAMENODE_PORT_NUMBER + "/hbase");
        configuration.set("hbase.master", HBASE_MASTER_HOST_NAME + ":" + HBASE_MASTER_PORT_NUMBER);
        configuration.set("hbase.zookeeper.quorum", HDFS_NAMENODE_HOST_NAME);
        configuration.set("hbase.cluster.distributed","true");
        HTable table = new HTable(configuration, TABLE_NAME);
        doCreateTable(table);
        doInsertTable(table);
        doQueryTable(table);
    }
        
    private void doCreateTable(HTable table) throws IOException {
         HTableDescriptor desc = new HTableDescriptor(TABLE_NAME);
         desc.addFamily(new HColumnDescriptor(COLUMN_FAMILY_NAME));
         HBaseAdmin hbaseAdmin = new HBaseAdmin(configuration);
         try {
             hbaseAdmin.createTable(desc);
         }
         catch (TableExistsException e) {
             LOGGER.warn("Table already exists");
         }
    }
         
    private void doInsertTable(HTable table) throws IOException {
         for (int i = 1; i < NUMBER_OF_KEYS; i++) {
             Put put = new Put(Bytes.toBytes(KEY_NAME_PREFIX + i));
             put.add(Bytes.toBytes(COLUMN_FAMILY_NAME), Bytes.toBytes(COLUMN_NAME), Bytes.toBytes(KEY_VALUE_PREFIX + i));
             table.put(put);
             Get get = new Get(Bytes.toBytes(KEY_NAME_PREFIX + i));
             get.addColumn(Bytes.toBytes(COLUMN_FAMILY_NAME), Bytes.toBytes(COLUMN_NAME));
             Result result = table.get(get);
             byte[] val = result.getValue(Bytes.toBytes(COLUMN_FAMILY_NAME), Bytes.toBytes(COLUMN_NAME));
             LOGGER.info("Value=" + Bytes.toString(val));
         }
    }
    
    private void doQueryTable(HTable table) throws IOException {
         String beginKey = "a";
         String endKey = "l";
         Scan scan = new Scan(beginKey.getBytes(), endKey.getBytes());
         scan.addColumn(COLUMN_FAMILY_NAME.getBytes(),COLUMN_NAME.getBytes());
         ResultScanner resultScanner = table.getScanner(scan);
         try {
             Result result;
             while ((result = resultScanner.next()) != null) {
                 LOGGER.info("Deleting key=" + result.getRow());
                 table.delete(new Delete(result.getRow()));
             }
         } finally {
             resultScanner.close();
         }
    }
/*
         BatchUpdate batchUpdate = new BatchUpdate("test_row1");
         batchUpdate.put("columnfamily:column1", Bytes.toBytes("some value"));
         batchUpdate.delete("column1");
         table.commit(batchUpdate);
         Cell cell = table.get("test_row1", "columnfamily1:column1");
         if (cell != null) {
             String valueStr = Bytes.toString(cell.getValue());
             System.out.println("test_row1:columnfamily1:column1 " + valueStr);
         }
         RowResult singleRow = table.getRow(Bytes.toBytes("test_row1"));
         Cell cell = singleRow.get(Bytes.toBytes("columnfamily1:column1"));
         if(cell!=null) {
             System.out.println(Bytes.toString(cell.getValue()));
         }
         cell = singleRow.get(Bytes.toBytes("columnfamily1:column2"));
         if(cell!=null) {
             System.out.println(Bytes.toString(cell.getValue()));
         }
         Scanner scanner = table.getScanner(new String[] { "columnfamily1:column1" });
         RowResult rowResult = scanner.next();
         while (rowResult != null) {
             System.out.println("Found row: " + Bytes.toString(rowResult.getRow())
                 + " with value: " +
                 rowResult.get(Bytes.toBytes("columnfamily1:column1")));
             rowResult = scanner.next();
         }
         for (RowResult result : scanner) {
             System.out.println("Found row: " + Bytes.toString(result.getRow())
                 + " with value: " +
                 result.get(Bytes.toBytes("columnfamily1:column1")));
         }
         scanner.close();
    }
*/
/*
    private static void lilyIndex() throws Exception {
        Configuration config = new Configuration();
        config.addResource(new URL("file:/path/to/hbase-0.21.0-dev/conf/hbase-default.xml"));
        config.addResource(new URL("file:/path/to/hbase-0.21.0-dev/conf/hbase-site.xml"));
        config.reloadConfiguration();
        final String META_TABLE_NAME = "indexmeta";
        // Create the IndexManager - Create the indexmeta table if it would not yet exist.
        IndexManager indexManager = null;
        try {
            indexManager = new IndexManager(config, META_TABLE_NAME);
        } catch (TableNotFoundException e) {
            if (e.getMessage().contains(META_TABLE_NAME)) {
                IndexManager.createIndexMetaTable(config, META_TABLE_NAME);
                indexManager = new IndexManager(config, META_TABLE_NAME);
            } else {
                System.out.println(e.getMessage());
                System.exit(1);
        }
        // Delete the index if it would already exist - This is just to make it easy to run this sample multiple times.
        try {
            indexManager.getIndex("index1");
            indexManager.deleteIndex("index1");
        } catch (IndexNotFoundException e) {
            // ok, the index does not exit
        }
        // Define an index
        IndexDefinition indexDef = new IndexDefinition("index1");
        StringIndexFieldDefinition stringField = indexDef.addStringField("stringfield");
        stringField.setCaseSensitive(false);
        indexManager.createIndex(indexDef);

        // Add entries to the index
        Index index = indexManager.getIndex("index1");
        String[] values = {"bird", "brown", "bee", "ape", "dog", "cat"};
        for (String value : values) {
            IndexEntry entry = new IndexEntry();
            entry.addField("stringfield", value);
            entry.setIdentifier(Bytes.toBytes("id-" + value));
            index.addEntry(entry);
        }
        // Query the index
        Query query = new Query();
        query.setRangeCondition("stringfield", "b", "b");
        QueryResult result = index.performQuery(query);
        System.out.println("The identifiers of the matching index entries are:");
        byte[] identifier;
        while ((identifier = result.next()) != null) {
            System.out.println(Bytes.toString(identifier));
        }        
    }
*/    
}
