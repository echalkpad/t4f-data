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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.model.IndexedSlicesQuery;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnIndexType;
import me.prettyprint.hector.api.ddl.ColumnType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.MutationResult;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.MultigetSliceQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;
import me.prettyprint.hector.api.query.SuperColumnQuery;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.cassandra.locator.SimpleStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.thrift.transport.TTransportException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class HectorCassandraTest {
    private static final String CLUSTER_NAME = "TestCluster";
    private static final String KEYSPACE_NAME = "KEYSPACE";
    private static final String COLUMN_FAMILY_NAME_1 = "ColumnFamily1";
    private static final String COLUMN_FAMILY_INDEXED_1 = "ColumnFamilyIndexed1";
    private static final String COLUMN_FAMILY_SUPER_1 = "ColumnFamilySuper1";

    private static AosEmbeddedCassandra serverHelper;
    private Cluster cluster;
    private Keyspace keyspace;

    private final StringSerializer stringSerializer = StringSerializer.get();

    @BeforeClass
    public static void startCassandra() throws TTransportException, IOException, InterruptedException,
            ConfigurationException {

        FileUtils.deleteDirectory(new File("target/cassandra-store"));
        serverHelper = new AosEmbeddedCassandra();
        serverHelper.setup();
    }

    @AfterClass
    public static void stopCassandra() throws IOException {
        AosEmbeddedCassandra.teardown();
    }

    @Test
    public void testCassandra() {
        getCluster();
        dropKeyspace();
        createKeySpace();
        createSchema();
        describeKeySpace();
        insertSingleColumnData();
        deleteSingleColumnData();
        insertSuperColumnData();
        testGetRangeSlices();
        testGetRangeSlicesKeysOnly();
        testGetRangeSlicesPaginate();
        testIndexedSlices();
        testMultigetSliceRetrieval();
        testResultDetail();
        testTombstonedGetRangeSlices();
        shutdownCluster();
    }

    private void getCluster() {
        cluster = HFactory.getOrCreateCluster(CLUSTER_NAME, new CassandraHostConfigurator("127.0.0.1:9160"));
    }

    private void dropKeyspace() {
        try {
            cluster.dropKeyspace(KEYSPACE_NAME);
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createKeySpace() {
        keyspace = HFactory.createKeyspace(KEYSPACE_NAME, cluster);
    }

    private void createSchema() {

        // ColumnFamily 1

        BasicColumnDefinition columnDefinition1_1 = new BasicColumnDefinition();
        columnDefinition1_1.setName(stringSerializer.toByteBuffer("name"));
        columnDefinition1_1.setValidationClass(ComparatorType.LONGTYPE.getClassName());

        BasicColumnDefinition columnDefinition1_2 = new BasicColumnDefinition();
        columnDefinition1_2.setName(stringSerializer.toByteBuffer("column_A_B"));
        columnDefinition1_2.setValidationClass(ComparatorType.UTF8TYPE.getClassName());

        BasicColumnFamilyDefinition columnFamilyDefinition1 = new BasicColumnFamilyDefinition();
        columnFamilyDefinition1.setKeyspaceName(KEYSPACE_NAME);
        columnFamilyDefinition1.setName(COLUMN_FAMILY_NAME_1);
        columnFamilyDefinition1.addColumnDefinition(columnDefinition1_1);
        columnFamilyDefinition1.addColumnDefinition(columnDefinition1_2);

        ColumnFamilyDefinition cfDef1 = new ThriftCfDef(columnFamilyDefinition1);

        // ColumnFamily 2

        BasicColumnDefinition columnDefinitionIndexed1 = new BasicColumnDefinition();
        columnDefinitionIndexed1.setName(stringSerializer.toByteBuffer("column_test_1"));
        columnDefinitionIndexed1.setIndexType(ColumnIndexType.KEYS);
        columnDefinitionIndexed1.setIndexName("INDEX_COLUMN_TEST_1");
        columnDefinitionIndexed1.setValidationClass(ComparatorType.UTF8TYPE.getClassName());

        BasicColumnDefinition columnDefinitionIndexed2 = new BasicColumnDefinition();
        columnDefinitionIndexed2.setName(stringSerializer.toByteBuffer("birthdate"));
        columnDefinitionIndexed2.setIndexType(ColumnIndexType.KEYS);
        columnDefinitionIndexed2.setIndexName("INDEX_BIRTH_DATE");
        columnDefinitionIndexed2.setValidationClass(ComparatorType.LONGTYPE.getClassName());

        BasicColumnFamilyDefinition columnFamilyDefinitionIndexed = new BasicColumnFamilyDefinition();
        columnFamilyDefinitionIndexed.setKeyspaceName(KEYSPACE_NAME);
        columnFamilyDefinitionIndexed.setName(COLUMN_FAMILY_INDEXED_1);
        columnFamilyDefinitionIndexed.addColumnDefinition(columnDefinitionIndexed1);
        columnFamilyDefinitionIndexed.addColumnDefinition(columnDefinitionIndexed2);

        ColumnFamilyDefinition cfDef2 = new ThriftCfDef(columnFamilyDefinitionIndexed);

        // ColumnFamilySuper

        BasicColumnDefinition columnDefinitionSuper1 = new BasicColumnDefinition();
        columnDefinitionSuper1.setName(stringSerializer.toByteBuffer("Super_Column_1"));
        columnDefinitionSuper1.setValidationClass(ComparatorType.UTF8TYPE.getClassName());

        BasicColumnFamilyDefinition columnFamilyDefinitionSuper2 = new BasicColumnFamilyDefinition();
        columnFamilyDefinitionSuper2.setKeyspaceName(KEYSPACE_NAME);
        columnFamilyDefinitionSuper2.setName(COLUMN_FAMILY_SUPER_1);
        columnFamilyDefinitionSuper2.setColumnType(ColumnType.SUPER);
        columnFamilyDefinitionSuper2.addColumnDefinition(columnDefinitionSuper1);

        ColumnFamilyDefinition cfDefSuper = new ThriftCfDef(columnFamilyDefinitionSuper2);

        KeyspaceDefinition keyspaceDefinition = HFactory.createKeyspaceDefinition(KEYSPACE_NAME,
                SimpleStrategy.class.getName(), 1, Arrays.asList(cfDef1, cfDef2, cfDefSuper));

        cluster.addKeyspace(keyspaceDefinition, true);
        // cluster.updateKeyspace(keyspaceDefinition);

    }

    private void describeKeySpace() {
        List<KeyspaceDefinition> keyspaces = cluster.describeKeyspaces();
        for (KeyspaceDefinition kd : keyspaces) {
            if (kd.getName().equals(KEYSPACE_NAME)) {
                System.out.println("Name: " + kd.getName());
                System.out.println("RF: " + kd.getReplicationFactor());
                System.out.println("strategy class: " + kd.getStrategyClass());
                List<ColumnFamilyDefinition> cfDefs = kd.getCfDefs();
                ColumnFamilyDefinition def = cfDefs.get(0);
                System.out.println("  CF Name: " + def.getName());
                System.out.println("  CF Metadata: " + def.getColumnMetadata());
            }
        }
    }

    private void insertSingleColumnData() {

        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);

        mutator.addInsertion("jsmith0", COLUMN_FAMILY_NAME_1, HFactory.createStringColumn("first", "John"))
                .addInsertion("jsmith0", COLUMN_FAMILY_NAME_1, HFactory.createStringColumn("last", "Smith"))
                .addInsertion("jsmith0", COLUMN_FAMILY_NAME_1, HFactory.createStringColumn("middle", "Q"));
        mutator.execute();

        mutator.insert("jsmith", COLUMN_FAMILY_NAME_1, HFactory.createStringColumn("first", "John"));

        ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspace);
        columnQuery.setColumnFamily(COLUMN_FAMILY_NAME_1).setKey("jsmith").setName("first");
        QueryResult<HColumn<String, String>> result = columnQuery.execute();
        System.out.println("Read HColumn from cassandra: " + result.get());
        System.out.println("Verify on CLI with:  get DynamicKeyspace1.Keyspace1['jsmith'] ");

    }

    private void deleteSingleColumnData() {

        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
        mutator.delete("jsmith", COLUMN_FAMILY_NAME_1, "first", stringSerializer);

        mutator.addDeletion("jsmith", COLUMN_FAMILY_NAME_1, "middle", stringSerializer)
                .addDeletion("jsmith", COLUMN_FAMILY_NAME_1, "last", stringSerializer).execute();

    }

    private void insertSuperColumnData() {

        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);

        mutator.insert("billing", COLUMN_FAMILY_SUPER_1, HFactory.createSuperColumn("jsmith",
                Arrays.asList(HFactory.createStringColumn("first", "John")), stringSerializer, stringSerializer,
                stringSerializer));

        SuperColumnQuery<String, String, String, String> superColumnQuery = HFactory.createSuperColumnQuery(keyspace,
                stringSerializer, stringSerializer, stringSerializer, stringSerializer);
        superColumnQuery.setColumnFamily(COLUMN_FAMILY_SUPER_1).setKey("billing").setSuperName("jsmith");
        QueryResult<HSuperColumn<String, String, String>> result = superColumnQuery.execute();
        System.out.println("Read HSuperColumn from cassandra: " + result.get());
        System.out.println("Verify on CLI with:  get DynamicKeyspace1.ColumnFamilySuper1['billing']['jsmith'] ");

    }

    private void testGetRangeSlices() {

        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);

        RangeSlicesQuery<String, String, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(keyspace,
                stringSerializer, stringSerializer, stringSerializer);
        rangeSlicesQuery.setColumnFamily(COLUMN_FAMILY_NAME_1);
        rangeSlicesQuery.setKeys("fake_key_", "");
        rangeSlicesQuery.setColumnNames("birthdate");
        rangeSlicesQuery.setReturnKeysOnly();

        QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute();
        OrderedRows<String, String, String> orderedRows = result.get();

        Row<String, String, String> lastRow = orderedRows.peekLast();

        System.out.println("Contents of rows: \n");
        for (Row<String, String, String> r : orderedRows) {
            System.out.println("   " + r);
        }

    }

    private void testGetRangeSlicesKeysOnly() {

        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);

        for (int i = 0; i < 5; i++) {
            mutator.addInsertion("fake_key_" + i, COLUMN_FAMILY_NAME_1,
                    HFactory.createStringColumn("fake_column_0", "fake_value_0_" + i))
                    .addInsertion("fake_key_" + i, COLUMN_FAMILY_NAME_1,
                            HFactory.createStringColumn("fake_column_1", "fake_value_1_" + i))
                    .addInsertion("fake_key_" + i, COLUMN_FAMILY_NAME_1,
                            HFactory.createStringColumn("fake_column_2", "fake_value_2_" + i));
        }
        mutator.execute();

        RangeSlicesQuery<String, String, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(keyspace,
                stringSerializer, stringSerializer, stringSerializer);
        rangeSlicesQuery.setColumnFamily(COLUMN_FAMILY_NAME_1);
        rangeSlicesQuery.setKeys("fake_key_", "");
        rangeSlicesQuery.setColumnNames("birthdate");
        rangeSlicesQuery.setReturnKeysOnly();
        rangeSlicesQuery.setRowCount(5);

        QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute();
        OrderedRows<String, String, String> orderedRows = result.get();

        Row<String, String, String> lastRow = orderedRows.peekLast();

        System.out.println("Contents of rows: \n");
        for (Row<String, String, String> r : orderedRows) {
            System.out.println("   " + r);
        }

    }

    private void testGetRangeSlicesPaginate() {

        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);

        for (int i = 0; i < 20; i++) {
            mutator.addInsertion("fake_key_" + i, COLUMN_FAMILY_NAME_1,
                    HFactory.createStringColumn("fake_column_0", "fake_value_0_" + i))
                    .addInsertion("fake_key_" + i, COLUMN_FAMILY_NAME_1,
                            HFactory.createStringColumn("fake_column_1", "fake_value_1_" + i))
                    .addInsertion("fake_key_" + i, COLUMN_FAMILY_NAME_1,
                            HFactory.createStringColumn("fake_column_2", "fake_value_2_" + i));
        }
        mutator.execute();

        RangeSlicesQuery<String, String, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(keyspace,
                stringSerializer, stringSerializer, stringSerializer);
        rangeSlicesQuery.setColumnFamily(COLUMN_FAMILY_NAME_1);
        rangeSlicesQuery.setKeys("", "");
        rangeSlicesQuery.setRange("", "", false, 3);

        rangeSlicesQuery.setRowCount(11);
        QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute();
        OrderedRows<String, String, String> orderedRows = result.get();

        Row<String, String, String> lastRow = orderedRows.peekLast();

        System.out.println("Contents of rows: \n");
        for (Row<String, String, String> r : orderedRows) {
            System.out.println("   " + r);
        }
        System.out.println("Should have 11 rows: " + orderedRows.getCount());

        rangeSlicesQuery.setKeys(lastRow.getKey(), "");
        orderedRows = rangeSlicesQuery.execute().get();

        System.out.println("2nd page Contents of rows: \n");
        for (Row<String, String, String> row : orderedRows) {
            System.out.println("   " + row);
        }

    }

    private void testMultigetSliceRetrieval() {

        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);

        for (int i = 0; i < 20; i++) {
            mutator.addInsertion("fake_key_" + i, COLUMN_FAMILY_NAME_1,
                    HFactory.createStringColumn("fake_column_0", "fake_value_0_" + i))
                    .addInsertion("fake_key_" + i, COLUMN_FAMILY_NAME_1,
                            HFactory.createStringColumn("fake_column_1", "fake_value_1_" + i))
                    .addInsertion("fake_key_" + i, COLUMN_FAMILY_NAME_1,
                            HFactory.createStringColumn("fake_column_2", "fake_value_2_" + i));
        }
        mutator.execute();

        MultigetSliceQuery<String, String, String> multigetSliceQuery = HFactory.createMultigetSliceQuery(keyspace,
                stringSerializer, stringSerializer, stringSerializer);
        multigetSliceQuery.setColumnFamily(COLUMN_FAMILY_NAME_1);
        multigetSliceQuery.setKeys("fake_key_0", "fake_key_1", "fake_key_2", "fake_key_3", "fake_key_4");

        // set null range for empty byte[] on the underlying predicate
        multigetSliceQuery.setRange(null, null, false, 3);
        System.out.println(multigetSliceQuery);

        QueryResult<Rows<String, String, String>> result = multigetSliceQuery.execute();
        Rows<String, String, String> orderedRows = result.get();

        System.out.println("Contents of rows: \n");
        for (Row<String, String, String> r : orderedRows) {
            System.out.println("   " + r);
        }
        System.out.println("Should have 5 rows: " + orderedRows.getCount());

        MultigetSliceQuery<String, String, String> multigetSliceQuery2 = HFactory.createMultigetSliceQuery(keyspace,
                stringSerializer, stringSerializer, stringSerializer);
        multigetSliceQuery2.setColumnFamily(COLUMN_FAMILY_NAME_1);
        multigetSliceQuery2.setKeys("fake_key_011", "fake_key_111", "fake_key_211", "fake_key_311", "fake_key_411");

        // set null range for empty byte[] on the underlying predicate
        multigetSliceQuery2.setRange(null, null, false, 3);
        System.out.println(multigetSliceQuery2);

        QueryResult<Rows<String, String, String>> result2 = multigetSliceQuery2.execute();
        Rows<String, String, String> orderedRows2 = result2.get();

        System.out.println("Contents of rows: \n");
        for (Row<String, String, String> r : orderedRows2) {
            System.out.println("   ------------------------------" + r);
        }
        System.out.println("Should have 5 rows: " + orderedRows.getCount());

    }

    private void testIndexedSlices() {

        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);

        for (int i = 0; i < 24; i++) {
            mutator.addInsertion("fake_key_" + i, COLUMN_FAMILY_INDEXED_1,
                    HFactory.createStringColumn("fake_column_0", "fake_value_0_" + i))
                    .addInsertion("fake_key_" + i, COLUMN_FAMILY_INDEXED_1,
                            HFactory.createStringColumn("column_test_1", "column_test_1_value" + i))
                    .addInsertion("fake_key_" + i, COLUMN_FAMILY_INDEXED_1,
                            HFactory.createStringColumn("fake_column_1", "fake_value_1_" + i))
                    .addInsertion("fake_key_" + i, COLUMN_FAMILY_INDEXED_1,
                            HFactory.createStringColumn("fake_column_2", "fake_value_2_" + i))
                    .addInsertion(
                            "fake_key_" + i,
                            COLUMN_FAMILY_INDEXED_1,
                            HFactory.createColumn("birthdate", new Long(1974 + (i % 2)), stringSerializer,
                                    LongSerializer.get()))
                    .addInsertion(
                            "fake_key_" + i,
                            COLUMN_FAMILY_INDEXED_1,
                            HFactory.createColumn("birthmonth", new Long(i % 12), stringSerializer,
                                    LongSerializer.get()));
        }
        mutator.execute();

        IndexedSlicesQuery<String, String, Long> indexedSlicesQuery = HFactory.createIndexedSlicesQuery(keyspace,
                stringSerializer, stringSerializer, LongSerializer.get());
        indexedSlicesQuery.setColumnFamily(COLUMN_FAMILY_INDEXED_1);
        indexedSlicesQuery.addEqualsExpression("birthdate", 1975L);
        indexedSlicesQuery.addGtExpression("birthmonth", 6L);
        indexedSlicesQuery.addLtExpression("birthmonth", 8L);
        indexedSlicesQuery.setColumnNames("birthdate", "birthmonth");
        indexedSlicesQuery.setStartKey("");

        QueryResult<OrderedRows<String, String, Long>> result = indexedSlicesQuery.execute();

        System.out.println("The results should only have 4 entries: " + result.get().getCount());

    }

    private void testResultDetail() {

        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
        // add 10 rows
        for (int i = 0; i < 10; i++) {
            mutator.addInsertion("fake_key_" + i, COLUMN_FAMILY_NAME_1,
                    HFactory.createStringColumn("fake_column_0", "fake_value_0_" + i))
                    .addInsertion("fake_key_" + i, COLUMN_FAMILY_NAME_1,
                            HFactory.createStringColumn("fake_column_1", "fake_value_1_" + i))
                    .addInsertion("fake_key_" + i, COLUMN_FAMILY_NAME_1,
                            HFactory.createStringColumn("fake_column_2", "fake_value_2_" + i));
        }
        MutationResult me = mutator.execute();
        System.out.println("MutationResult from 10 row insertion: " + me);

        RangeSlicesQuery<String, String, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(keyspace,
                stringSerializer, stringSerializer, stringSerializer);
        rangeSlicesQuery.setColumnFamily(COLUMN_FAMILY_NAME_1);
        rangeSlicesQuery.setKeys("", "");
        rangeSlicesQuery.setRange("", "", false, 3);

        rangeSlicesQuery.setRowCount(10);
        QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute();
        System.out.println("Result from rangeSlices query: " + result.toString());

        ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspace);
        columnQuery.setColumnFamily(COLUMN_FAMILY_NAME_1).setKey("fake_key_0").setName("fake_column_0");
        QueryResult<HColumn<String, String>> colResult = columnQuery.execute();
        System.out.println("Execution time: " + colResult.getExecutionTimeMicro());
        System.out.println("CassandraHost used: " + colResult.getHostUsed());
        System.out.println("Query Execute: " + colResult.getQuery());

    }

    private void testTombstonedGetRangeSlices() {

        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
        // add 10 rows
        for (int i = 0; i < 10; i++) {
            mutator.addInsertion("fake_key_" + i, COLUMN_FAMILY_NAME_1,
                    HFactory.createStringColumn("fake_column_0", "fake_value_0_" + i))
                    .addInsertion("fake_key_" + i, COLUMN_FAMILY_NAME_1,
                            HFactory.createStringColumn("fake_column_1", "fake_value_1_" + i))
                    .addInsertion("fake_key_" + i, COLUMN_FAMILY_NAME_1,
                            HFactory.createStringColumn("fake_column_2", "fake_value_2_" + i));
        }
        mutator.execute();
        mutator.discardPendingMutations();
        // delete the odd rows
        for (int i = 0; i < 10; i++) {
            if ((i % 2) == 0)
                continue;
            mutator.addDeletion("fake_key_" + i, COLUMN_FAMILY_NAME_1, null, stringSerializer);
        }
        mutator.execute();

        RangeSlicesQuery<String, String, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(keyspace,
                stringSerializer, stringSerializer, stringSerializer);
        rangeSlicesQuery.setColumnFamily(COLUMN_FAMILY_NAME_1);
        rangeSlicesQuery.setKeys("", "");
        rangeSlicesQuery.setRange("", "", false, 3);

        rangeSlicesQuery.setRowCount(10);
        QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute();
        OrderedRows<String, String, String> orderedRows = result.get();

        for (Row<String, String, String> row : orderedRows) {
            System.out.println("===>" + row.getKey());
        }

        for (Row<String, String, String> row : orderedRows) {
            int keyNum = Integer.valueOf(row.getKey().substring(9));
            System.out.println("+-----------------------------------");
            if ((keyNum % 2) == 0) {
                System.out.println("| result key:" + row.getKey() + " which should have values: "
                        + row.getColumnSlice());
            }
            else {
                System.out.println("| TOMBSTONED result key:" + row.getKey() + " has values: " + row.getColumnSlice());
            }
            SliceQuery<String, String, String> q = HFactory.createSliceQuery(keyspace, stringSerializer,
                    stringSerializer, stringSerializer);
            q.setColumnFamily(COLUMN_FAMILY_NAME_1);
            q.setRange("", "", false, 3);
            q.setKey(row.getKey());

            QueryResult<ColumnSlice<String, String>> r = q.execute();
            System.out.println("|-- called directly via get_slice, the value is: " + r);
            // For a tombstone, you just get a null back from ColumnQuery
            System.out.println("|-- try the first column via getColumn: "
                    + HFactory.createColumnQuery(keyspace, stringSerializer, stringSerializer, stringSerializer)
                            .setColumnFamily(COLUMN_FAMILY_NAME_1).setKey(row.getKey()).setName("fake_column_0")
                            .execute());

            System.out.println("|-- verify on CLI with: get Keyspace1.Keyspace1['" + row.getKey() + "'] ");
        }
    }

    private void shutdownCluster() {
        cluster.getConnectionManager().shutdown();
    }

}
