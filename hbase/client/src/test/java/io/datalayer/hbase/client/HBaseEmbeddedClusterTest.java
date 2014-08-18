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
package io.datalayer.hbase.client;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MiniHBaseCluster;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
public class HBaseEmbeddedClusterTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseEmbeddedClusterTest.class);

    private static final byte[] TABLE_NAME_1 = Bytes.toBytes("TableName1");
    private static final byte[] COLUMN_FAMILY_NAME_1 = Bytes.toBytes("ColumnFamilyName1");
    private static final String KEY_PREFIX = "ke";
    private static final String COLUMN_PREFIX = "col_";
    private static final String VALUE_PREFIX = "val_";
    private static final int NUMBER_FACTOR = 10;
    private static final int NUMBER_OF_KEYS = 10 * NUMBER_FACTOR;
    private static final int NUMBER_OF_ROWS = 10 * NUMBER_FACTOR;

    private static MiniHBaseCluster hbaseCluster;

    @BeforeClass
    public static void setup() throws Exception {
        HBaseTestingUtility htu = new HBaseTestingUtility();
        htu.getConfiguration().setBoolean("dfs.support.append", true);
        hbaseCluster = htu.startMiniCluster();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (hbaseCluster != null) {
            hbaseCluster.shutdown();
        }
    }

    @Test
    public void testHbaseClientScenario() throws IOException {
        createTable(COLUMN_FAMILY_NAME_1, TABLE_NAME_1);
        for (int i = 0; i < NUMBER_OF_KEYS; i++) {
            put(TABLE_NAME_1, COLUMN_FAMILY_NAME_1, KEY_PREFIX + i);
        }
        for (int i = 0; i < NUMBER_OF_KEYS; i++) {
            get(TABLE_NAME_1, COLUMN_FAMILY_NAME_1, KEY_PREFIX + i);
        }
        scanAll(TABLE_NAME_1, COLUMN_FAMILY_NAME_1);
        scanWithFilter(TABLE_NAME_1, COLUMN_FAMILY_NAME_1, "val_" + (NUMBER_OF_KEYS + 1), 0);
        scanWithFilter(TABLE_NAME_1, COLUMN_FAMILY_NAME_1, "val_" + (NUMBER_OF_KEYS - 1), 1);
        for (int i = 0; i < NUMBER_OF_KEYS; i++) {
            delete(TABLE_NAME_1, COLUMN_FAMILY_NAME_1, KEY_PREFIX + i);
        }
    }

    private void createTable(byte[] columnFamilyName, byte[] tableName) throws IOException {
        LOGGER.info("Creating HBase table with name=" + tableName);
        HBaseAdmin hbaseAdmin = new HBaseAdmin(hbaseCluster.getConfiguration());
        if (!hbaseAdmin.tableExists(tableName)) {
            HTableDescriptor desc = new HTableDescriptor(tableName);
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(columnFamilyName);
            hColumnDescriptor.setMaxVersions(1);
            desc.addFamily(hColumnDescriptor);
            hbaseAdmin.createTable(desc);
        }
    }

    private void put(byte[] tableName, byte[] columnFamilyName, String key) throws IOException {
        HTable table = new HTable(hbaseCluster.getConfiguration(), tableName);
        // LOGGER.info("Putting in HBase table key=" + key);
        Put put = new Put(Bytes.toBytes(key));
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            put.add(columnFamilyName, Bytes.toBytes(COLUMN_PREFIX + i), Bytes.toBytes(VALUE_PREFIX + i));
        }
        table.put(put);
        table.close();
    }

    private void get(byte[] tableName, byte[] columnFamilyName, String key) throws IOException {
        HTable table = new HTable(hbaseCluster.getConfiguration(), tableName);
        // LOGGER.info("Getting from HBase table key=" + key);
        Get get = new Get(Bytes.toBytes(key));
        get.addFamily(columnFamilyName);
        Result result = table.get(get);
        Assert.assertEquals(false, result.isEmpty());
        logResult(result, columnFamilyName);
        table.close();
    }

    private void scanAll(byte[] tableName, byte[] columnFamilyName) throws IOException {
        HTable table = new HTable(hbaseCluster.getConfiguration(), tableName);
        LOGGER.info("Scanning all from HBase table name=" + tableName);
        Scan scan = new Scan();
        LOGGER.info("Getting from HBase table.");
        scan.addFamily(columnFamilyName);
        scan.setCaching(table.getScannerCaching() * 2);
        ResultScanner resultScanner = table.getScanner(scan);
        int resultCount = 0;
        Result result;
        while ((result = resultScanner.next()) != null) {
            logResult(result, columnFamilyName);
            resultCount++;
        }
        Assert.assertEquals(NUMBER_OF_KEYS, resultCount);
        table.close();
    }

    private void scanWithFilter(byte[] tableName, byte[] columnFamilyName, String val, int expected) throws IOException {
        HTable table = new HTable(hbaseCluster.getConfiguration(), tableName);
        LOGGER.info("Scanning with filter from HBase table.");
        Filter filter = new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL, new BinaryComparator(Bytes.toBytes(val)));
        Scan scan = new Scan();
        LOGGER.info("Getting from HBase table.");
        scan.setFilter(filter);
        int resultCount = 0;
        ResultScanner resultScanner = table.getScanner(scan);
        for (Result result : resultScanner) {
            logResult(result, columnFamilyName);
            resultCount++;
        }
        resultScanner.close();
        table.close();
    }

    private void delete(byte[] tableName, byte[] columnFamilyName, String key) throws IOException {
        HTable table = new HTable(hbaseCluster.getConfiguration(), tableName);
        // LOGGER.info("Deleting from HBase table.");
        Delete delete = new Delete(Bytes.toBytes(key));
        table.delete(delete);
        table.close();
    }

    private void logResult(Result result, byte[] columnFamilyName) {
        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            byte[] byteValue = result.getValue(columnFamilyName, Bytes.toBytes(COLUMN_PREFIX + i));
            if (byteValue != null) {
                // LOGGER.info("Value for column " + COLUMN_PREFIX + i +"=" +
                // Bytes.toString(byteValue));
            }
        }
    }

    /*
     * BatchUpdate batchUpdate = new BatchUpdate("test_row1");
     * batchUpdate.put("columnfamily:column1", Bytes.toBytes("some value"));
     * batchUpdate.delete("column1"); table.commit(batchUpdate); Cell cell =
     * table.get("test_row1", "columnfamily1:column1"); if (cell != null) {
     * String valueStr = Bytes.toString(cell.getValue());
     * System.out.println("test_row1:columnfamily1:column1 " + valueStr); }
     * RowResult singleRow = table.getRow(Bytes.toBytes("test_row1")); Cell cell
     * = singleRow.get(Bytes.toBytes("columnfamily1:column1")); if(cell!=null) {
     * System.out.println(Bytes.toString(cell.getValue())); } cell =
     * singleRow.get(Bytes.toBytes("columnfamily1:column2")); if(cell!=null) {
     * System.out.println(Bytes.toString(cell.getValue())); } Scanner scanner =
     * table.getScanner(new String[] { "columnfamily1:column1" }); RowResult
     * rowResult = scanner.next(); while (rowResult != null) {
     * System.out.println("Found row: " + Bytes.toString(rowResult.getRow()) +
     * " with value: " + rowResult.get(Bytes.toBytes("columnfamily1:column1")));
     * rowResult = scanner.next(); } for (RowResult result : scanner) {
     * System.out.println("Found row: " + Bytes.toString(result.getRow()) +
     * " with value: " + result.get(Bytes.toBytes("columnfamily1:column1"))); }
     * scanner.close(); }
     */
    /*
     * private static void lilyIndex() throws Exception { Configuration config =
     * new Configuration(); config.addResource(new
     * URL("file:/path/to/hbase-0.21.0-dev/conf/hbase-default.xml"));
     * config.addResource(new
     * URL("file:/path/to/hbase-0.21.0-dev/conf/hbase-site.xml"));
     * config.reloadConfiguration(); final String META_TABLE_NAME = "indexmeta";
     * // Create the IndexManager - Create the indexmeta table if it would not
     * yet exist. IndexManager indexManager = null; try { indexManager = new
     * IndexManager(config, META_TABLE_NAME); } catch (TableNotFoundException e)
     * { if (e.getMessage().contains(META_TABLE_NAME)) {
     * IndexManager.createIndexMetaTable(config, META_TABLE_NAME); indexManager
     * = new IndexManager(config, META_TABLE_NAME); } else {
     * System.out.println(e.getMessage()); System.exit(1); } // Delete the index
     * if it would already exist - This is just to make it easy to run this
     * sample multiple times. try { indexManager.getIndex("index1");
     * indexManager.deleteIndex("index1"); } catch (IndexNotFoundException e) {
     * // ok, the index does not exit } // Define an index IndexDefinition
     * indexDef = new IndexDefinition("index1"); StringIndexFieldDefinition
     * stringField = indexDef.addStringField("stringfield");
     * stringField.setCaseSensitive(false); indexManager.createIndex(indexDef);
     * 
     * // Add entries to the index Index index =
     * indexManager.getIndex("index1"); String[] values = {"bird", "brown",
     * "bee", "ape", "dog", "cat"}; for (String value : values) { IndexEntry
     * entry = new IndexEntry(); entry.addField("stringfield", value);
     * entry.setIdentifier(Bytes.toBytes("id-" + value)); index.addEntry(entry);
     * } // Query the index Query query = new Query();
     * query.setRangeCondition("stringfield", "b", "b"); QueryResult result =
     * index.performQuery(query);
     * System.out.println("The identifiers of the matching index entries are:");
     * byte[] identifier; while ((identifier = result.next()) != null) {
     * System.out.println(Bytes.toString(identifier)); } }
     */

}
