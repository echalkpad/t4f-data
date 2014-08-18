package io.datalayer.hbase.client;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public class HBaseClientMain {
    private static final String TABLE_NAME = "table_name_2";
    private static final String COLUMN_FAMILY_NAME = "column_name_1";

    public static void main(String... args) throws IOException {

        Configuration configuration = new Configuration();
        // configuration.set("hbase.rootdir", "hdfs://localhost:9000/hbase");
        // configuration.set("hbase.master.bindAddress", "localhost");
        // configuration.setInt("hbase.master.port", 6000);
        configuration.set("hbase.zookeeper.quorum", "localhost");
        configuration.setInt("hbase.zookeeper.property.clientPort", 2181);
        configuration.setBoolean("dfs.support.append", true);
        configuration.setBoolean("hbase.cluster.distributed", true);

        HBaseAdmin hbaseAdmin = new HBaseAdmin(configuration);
        HTableDescriptor desc = new HTableDescriptor(TABLE_NAME);
        HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(COLUMN_FAMILY_NAME);
        // hColumnDescriptor.setMaxVersions(1);
        desc.addFamily(hColumnDescriptor);
        hbaseAdmin.createTable(desc);
        if (!hbaseAdmin.tableExists(TABLE_NAME)) {
            throw new IOException("table should exist");
        }
        hbaseAdmin.close();
    }
}
