package io.datalayer.cassandra.helper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraHelper
{

    private final static Logger LOGGER = LoggerFactory.getLogger(CassandraHelper.class);

    private static boolean isStarted = false;
    private static EmbeddedCassandraService cassandra;
    private static Thread zkThread;

    public synchronized static void start(String... keyspaces) throws Exception
    {

        if (!isStarted)
        {

            File tmpCassandraFile = new File("tmp");
            if (tmpCassandraFile.exists())
            {
                // FileUtils.deleteDirectory(tmpCassandraFile);
            }

            // FileUtils.deleteDirectory(new File("./target/cassandra"));

            startCassandra();

            for (String keyspace : keyspaces)
            {
                createMutableKeyspace(keyspace);
            }

            isStarted = true;

        }

    }

    public synchronized static void stop()
            throws IOException,
            InterruptedException
    {

        if (cassandra != null)
        {
            cassandra.stop();
            TimeUnit.SECONDS.sleep(1);
        }

        if (zkThread != null)
        {
            zkThread.interrupt();
        }

    }

    private static void startCassandra() throws Exception
    {

        File storageCassandraFile = new File("target/cassandra-storage");
        if (storageCassandraFile.exists())
        {
            // FileUtils.deleteDirectory(storageCassandraFile);
        }

        cassandra = new EmbeddedCassandraService();
        cassandra.start();
    }

    private static void createMutableKeyspace(String keyspaceString)
    {

    }

}
