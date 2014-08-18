package io.datalayer.cassandra.stress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import me.prettyprint.cassandra.model.AllOneConsistencyLevelPolicy;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.exceptions.HInvalidRequestException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.MutationResult;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;

import com.google.common.base.Joiner;

public class CassandraStresser {

    private static String CONNECTION_STRING = "localhost:9160";
    private static int NUMBER_OF_THREADS = 500;
    private static final StringSerializer STRING_SERIALIZER = StringSerializer.get();

    private static String KEYSPACE_NAME = "UVID_TEST";

    private static long count;

    private static Lock lock;

    public static void main(String... args) throws InterruptedException, IOException {

        printUsage();

        if (args.length > 0) {
            CONNECTION_STRING = args[0];
            NUMBER_OF_THREADS = Integer.parseInt(args[1]);
        }

        lock = new ReentrantLock();

        try {
            dropKeyspace(newCluster(), KEYSPACE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread measureThread = new Thread(new Runnable() {

            @Override
            public void run() {

                long start = Calendar.getInstance().getTimeInMillis();

                while (true) {

                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    long elapsedTime = Calendar.getInstance().getTimeInMillis() - start;
                    long elapsedTimeSecond = elapsedTime / 1000;
                    System.out.println((count / elapsedTimeSecond) + " RW/s");

                }

            }

        });
        measureThread.start();

        ThreadGroup tg = new ThreadGroup(CassandraStresser.class.getName());
        List<Thread> threadList = new ArrayList<Thread>();
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            Thread t = new Thread(tg, new CassandraStresser().new CassandraStresserRunnable(KEYSPACE_NAME,
                    "ColumnFamilyName_" + i, "ColumnName_" + i));
            threadList.add(t);
            t.start();
        }

        Thread.sleep(Long.MAX_VALUE);

    }

    private static void printUsage() {
        System.out.println("Parameters: connection-string number-of-threads");
        System.out.println("Example: localhost:9160,localhost2:9160 150");
    }

    private class CassandraStresserRunnable implements Runnable {

        private Cluster cluster;
        private String keyspaceName;
        private String columnFamilyName;
        private String columnName;
        private Keyspace keyspace;

        public CassandraStresserRunnable(String keyspaceName, String columnFamilyName, String columnName) {

            this.keyspaceName = keyspaceName;
            this.columnFamilyName = columnFamilyName;
            this.columnName = columnName;

            cluster = newCluster();

            keyspace = getKeyspace(cluster, keyspaceName);

            try {

                lock.lock();
                createKeyspace();

                createColumnFamily();
            } finally {

                lock.unlock();
            }

        }

        @Override
        public void run() {

            try {

                while (true) {

                    mutateValues(new Integer(new Random().nextInt(1000)).toString(), columnName);

                }

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }

        }

        private void createKeyspace() {

            Cluster cluster = newCluster();

            KeyspaceDefinition keyspaceDefinition = cluster.describeKeyspace(KEYSPACE_NAME);

            if (keyspaceDefinition == null) {
                keyspaceDefinition = HFactory.createKeyspaceDefinition(KEYSPACE_NAME);
                String keyspaceId = cluster.addKeyspace(keyspaceDefinition, true);
                System.out.println("Result of addKeyspace: " + keyspaceId);
            } else {
                System.out.println("Keyspace already exists with name: " + keyspaceDefinition.getName());
            }

        }

        private void createColumnFamily() {

            ColumnFamilyDefinition columnFamilyDefinition = HFactory.createColumnFamilyDefinition(keyspaceName,
                    columnFamilyName);

            try {
                String columnFamilyId = cluster.addColumnFamily(columnFamilyDefinition, true);
            } catch (HInvalidRequestException e) {
                if (!e.getMessage().contains("Cannot add already existing column family")) {
                    throw new RuntimeException("Error while adding the column family to Cassandra.", e);
                }
            }

        }

        private void mutateValues(String key, String columnName) {

            String value = key;

            columnName = columnName + "_" + new Integer(new Random().nextInt(1000)).toString();

            Mutator<String> mutator = HFactory.createMutator(keyspace, STRING_SERIALIZER);
            mutator.addInsertion(key, columnFamilyName,
                    HFactory.createColumn(columnName, value, STRING_SERIALIZER, STRING_SERIALIZER));

            long start = Calendar.getInstance().getTimeInMillis();
            MutationResult mutationResult = mutator.execute();
            long end = Calendar.getInstance().getTimeInMillis();
            // System.out.println(columnName +
            // ": Write Computed time in millisecond: "
            // + (end - start));

            count++;

            ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspace);
            columnQuery.setColumnFamily(columnFamilyName).setKey(key).setName(columnName);

            start = Calendar.getInstance().getTimeInMillis();
            QueryResult<HColumn<String, String>> result = columnQuery.execute();
            end = Calendar.getInstance().getTimeInMillis();
            // System.out.println(columnName +
            // ": Read Computed time in millisecond: "
            // + (end - start));

            if (!result.get().getValue().equals(value)) {
                throw new RuntimeException();
            }

            count++;

        }
    }

    private static Cluster newCluster() {

        String hostUrl = randomizeHosts(CONNECTION_STRING);
        CassandraHostConfigurator cf = new CassandraHostConfigurator(hostUrl);
        cf.setAutoDiscoverHosts(true);
        return HFactory.getOrCreateCluster("QubitCluster", cf);
    }

    private static void dropKeyspace(Cluster cluster, String keyspaceName) {

        cluster.dropKeyspace(keyspaceName, true);
    }

    private static Keyspace getKeyspace(Cluster cluster, String keyspaceName) {

        return HFactory.createKeyspace(keyspaceName, cluster, new AllOneConsistencyLevelPolicy());
    }

    private static String randomizeHosts(String ipList) {

        String[] ips = ipList.split(",");

        if (ips.length > 1) {
            List<String> asList = Arrays.asList(ips);
            Collections.shuffle(asList);
            String newIpList = Joiner.on(",").join(asList);
            return newIpList;
        } else {
            return ipList;
        }

    }

}
