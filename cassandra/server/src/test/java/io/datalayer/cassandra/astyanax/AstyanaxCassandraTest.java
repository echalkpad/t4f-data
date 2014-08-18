package io.datalayer.cassandra.astyanax;

import com.google.common.collect.ImmutableMap;
import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

public class AstyanaxCassandraTest {

    public static final ColumnFamily<String, String> CF_STANDARD_1 = ColumnFamily.newColumnFamily("Standard_1",
            StringSerializer.get(), // Key Serializer
            StringSerializer.get()); // Column Serializer

    public static final ColumnFamily<String, String> CF_STANDARD_2 = new ColumnFamily<String, String>("Standard_2",
            StringSerializer.get(), // Key Serializer
            StringSerializer.get()); // Column Serializer

    public static void main(String... args) throws ConnectionException {

        AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
                .forCluster("ClusterName")
                .forKeyspace("UVID_TEST_2")
                .withAstyanaxConfiguration(new AstyanaxConfigurationImpl().setDiscoveryType(NodeDiscoveryType.NONE))
                .withConnectionPoolConfiguration(
                        new ConnectionPoolConfigurationImpl("MyConnectionPool").setPort(9160).setMaxConnsPerHost(1)
                                .setSeeds("localhost:9160"))
                .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
                .buildKeyspace(ThriftFamilyFactory.getInstance());

        context.start();

        Keyspace keyspace = context.getEntity();

        try {
            keyspace.dropKeyspace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            keyspace.createKeyspace(ImmutableMap
                    .<String, Object> builder()
                    .put("strategy_options",
                            ImmutableMap.<String, Object> builder().put("replication_factor", "2").build())
                    .put("strategy_class", "SimpleStrategy").build());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            keyspace.createColumnFamily(CF_STANDARD_1, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        MutationBatch m = keyspace.prepareMutationBatch();

        m.withRow(CF_STANDARD_1, "acct1234").putColumn("firstname", "john", null).putColumn("lastname", "smith", null)
                .putColumn("address", "555 Elm St", null).putColumn("age", 30, null);

        // m.withRow(CF_STANDARD_1, "acct1234")
        // .incrementCounterColumn("loginCount", 1);

        OperationResult<Void> result = m.execute();

        OperationResult<ColumnList<String>> result2 = keyspace.prepareQuery(CF_STANDARD_1).getKey("acct1234").execute();
        ColumnList<String> columns = result2.getResult();

        // Lookup columns in response by name
        int age = columns.getColumnByName("age").getIntegerValue();
        // long counter = columns.getColumnByName("loginCount").getLongValue();
        String address = columns.getColumnByName("address").getStringValue();

        // Or, iterate through the columns
        for (Column<String> c : result2.getResult()) {
            System.out.println(c.getName());
        }

    }

}
