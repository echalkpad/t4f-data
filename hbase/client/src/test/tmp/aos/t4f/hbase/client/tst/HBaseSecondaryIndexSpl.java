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

public class HBaseSecondaryIndexSpl {
/*
import java.io.IOException;
import java.util.Date;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Scanner;
import org.apache.hadoop.hbase.client.tableindexed.IndexSpecification;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTable;
import org.apache.hadoop.hbase.client.tableindexed.IndexedTableAdmin;
import org.apache.hadoop.hbase.filter.ColumnValueFilter;
import org.apache.hadoop.hbase.filter.ColumnValueFilter.CompareOp;
import org.apache.hadoop.hbase.io.BatchUpdate;
import org.apache.hadoop.hbase.io.RowResult;
import org.apache.hadoop.hbase.util.Bytes;

public class SecondaryIndexTest {

    public static void main(String... args) throws IOException {

        SecondaryIndexTest test = new SecondaryIndexTest();
        test.createTableWithSecondaryIndexes();
        test.writeToTable();
        test.addSecondaryIndexToExistingTable();
        test.removeSecondaryIndexToExistingTable();
        test.readAllRowsFromSecondaryIndex();
        test.readFilteredRowsFromSecondaryIndex();

        System.out.println("Done!");
    }

    public void writeToTable() throws IOException {
        HBaseConfiguration conf = new HBaseConfiguration();
        conf.addResource(new Path("/opt/hbase-0.19.3/conf/hbase-site.xml"));

        IndexedTable table = new IndexedTable(conf, Bytes.toBytes("test_table"));

        String row = "test_row";
        BatchUpdate update = null;

        for (int i = 0; i < 100; i++) {
            update = new BatchUpdate(row + i);
            update.put("columnfamily1:column1", Bytes.toBytes("value1-" + i));
            update.put("columnfamily1:column2", Bytes.toBytes("value2-" + i));
            table.commit(update);
        }

        table.close();
    }

    public void readAllRowsFromSecondaryIndex() throws IOException {
        HBaseConfiguration conf = new HBaseConfiguration();
        conf.addResource(new Path("/opt/hbase-0.19.3/conf/hbase-site.xml"));

        IndexedTable table = new IndexedTable(conf, Bytes.toBytes("test_table"));

        Scanner scanner = table.getIndexedScanner("column1",
            HConstants.EMPTY_START_ROW, null, null, new byte[][] {
            Bytes.toBytes("columnfamily1:column1"),
                Bytes.toBytes("columnfamily1:column2") });

        for (RowResult rowResult : scanner) {
            System.out.println(Bytes.toString(
                rowResult.get(Bytes.toBytes("columnfamily1:column1")).getValue())
                + ", " + Bytes.toString(rowResult.get(
                Bytes.toBytes("columnfamily1:column2")).getValue()
                ));
        }

        table.close();
    }

    public void readFilteredRowsFromSecondaryIndex() throws IOException {
        HBaseConfiguration conf = new HBaseConfiguration();
        conf.addResource(new Path("/opt/hbase-0.19.3/conf/hbase-site.xml"));

        IndexedTable table = new IndexedTable(conf, Bytes.toBytes("test_table"));

        ColumnValueFilter filter =
            new ColumnValueFilter(Bytes.toBytes("columnfamily1:column1"),
            CompareOp.LESS, Bytes.toBytes("value1-40"));

        Scanner scanner = table.getIndexedScanner("column1",
            HConstants.EMPTY_START_ROW, null, filter,
            new byte[][] { Bytes.toBytes("columnfamily1:column1"),
                Bytes.toBytes("columnfamily1:column2")
            });

        for (RowResult rowResult : scanner) {
            System.out.println(Bytes.toString(
                rowResult.get(Bytes.toBytes("columnfamily1:column1")).getValue())
                + ", " + Bytes.toString(rowResult.get(
                Bytes.toBytes("columnfamily1:column2")).getValue()
                ));
        }

        table.close();
    }

    public void createTableWithSecondaryIndexes() throws IOException {
        HBaseConfiguration conf = new HBaseConfiguration();
        conf.addResource(new Path("/opt/hbase-0.19.3/conf/hbase-site.xml"));

        HTableDescriptor desc = new HTableDescriptor("test_table");

        desc.addFamily(new HColumnDescriptor("columnfamily1:column1"));
        desc.addFamily(new HColumnDescriptor("columnfamily1:column2"));

        desc.addIndex(new IndexSpecification("column1",
            Bytes.toBytes("columnfamily1:column1")));
        desc.addIndex(new IndexSpecification("column2",
            Bytes.toBytes("columnfamily1:column2")));

        IndexedTableAdmin admin = null;
        admin = new IndexedTableAdmin(conf);

        if (admin.tableExists(Bytes.toBytes("test_table"))) {
            if (admin.isTableEnabled("test_table")) {
                admin.disableTable(Bytes.toBytes("test_table"));
            }

            admin.deleteTable(Bytes.toBytes("test_table"));
        }

        if (admin.tableExists(Bytes.toBytes("test_table-column1"))) {
            if (admin.isTableEnabled("test_table-column1")) {
                admin.disableTable(Bytes.toBytes("test_table-column1"));
            }

            admin.deleteTable(Bytes.toBytes("test_table-column1"));
        }

        admin.createTable(desc);
    }

    public void addSecondaryIndexToExistingTable() throws IOException {
        HBaseConfiguration conf = new HBaseConfiguration();
        conf.addResource(new Path("/opt/hbase-0.19.3/conf/hbase-site.xml"));

        IndexedTableAdmin admin = null;
        admin = new IndexedTableAdmin(conf);

        admin.addIndex(Bytes.toBytes("test_table"),
            new IndexSpecification("column2",
            Bytes.toBytes("columnfamily1:column2")));
    }

    public void removeSecondaryIndexToExistingTable() throws IOException {
        HBaseConfiguration conf = new HBaseConfiguration();
        conf.addResource(new Path("/opt/hbase-0.19.3/conf/hbase-site.xml"));

        IndexedTableAdmin admin = null;
        admin = new IndexedTableAdmin(conf);

        admin.removeIndex(Bytes.toBytes("test_table"), "column2");
    }

}

*/
}
