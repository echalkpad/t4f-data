package io.datalayer.cassandra.helper;

import java.io.IOException;

import org.apache.cassandra.service.CassandraDaemon;

/**
 * java 
 *   -ea 
 *   -javaagent:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/jamm-0.2.5.jar 
 *   -XX:+UseThreadPriorities -XX:ThreadPriorityPolicy=42 
 *   -Xms3988M 
 *   -Xmx3988M 
 *   -Xmn800M 
 *   -XX:+HeapDumpOnOutOfMemoryError 
 *   -Xss256k 
 *   -XX:StringTableSize=1000003 
 *   -XX:+UseParNewGC 
 *   -XX:+UseConcMarkSweepGC 
 *   -XX:+CMSParallelRemarkEnabled 
 *   -XX:SurvivorRatio=8 
 *   -XX:MaxTenuringThreshold=1 
 *   -XX:CMSInitiatingOccupancyFraction=75 
 *   -XX:+UseCMSInitiatingOccupancyOnly 
 *   -XX:+UseTLAB 
 *   -XX:+UseCondCardMark 
 *   -Djava.net.preferIPv4Stack=true 
 *   -Dcom.sun.management.jmxremote.port=7199 
 *   -Dcom.sun.management.jmxremote.ssl=false 
 *   -Dcom.sun.management.jmxremote.authenticate=false 
 *   -Dlog4j.configuration=log4j-server.properties 
 *   -Dlog4j.defaultInitOverride=true 
 *   -Dcassandra-foreground=yes 
 *   -cp /opt/apache-cassandra-2.0.5-SNAPSHOT/conf:/opt/apache-cassandra-2.0.5-SNAPSHOT/build/classes/main:/opt/apache-cassandra-2.0.5-SNAPSHOT/build/classes/thrift:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/antlr-3.2.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/apache-cassandra-2.0.5-SNAPSHOT.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/apache-cassandra-clientutil-2.0.5-SNAPSHOT.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/apache-cassandra-thrift-2.0.5-SNAPSHOT.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/commons-cli-1.1.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/commons-codec-1.2.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/commons-lang3-3.1.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/compress-lzf-0.8.4.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/concurrentlinkedhashmap-lru-1.3.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/disruptor-3.0.1.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/guava-15.0.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/high-scale-lib-1.1.2.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/jackson-core-asl-1.9.2.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/jackson-mapper-asl-1.9.2.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/jamm-0.2.5.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/jbcrypt-0.3m.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/jline-1.0.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/json-simple-1.1.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/libthrift-0.9.1.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/log4j-1.2.16.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/lz4-1.2.0.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/metrics-core-2.2.0.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/netty-3.6.6.Final.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/reporter-config-2.1.0.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/servlet-api-2.5-20081211.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/slf4j-api-1.7.2.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/slf4j-log4j12-1.7.2.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/snakeyaml-1.11.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/snappy-java-1.0.5.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/snaptree-0.1.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/super-csv-2.1.0.jar:/opt/apache-cassandra-2.0.5-SNAPSHOT/lib/thrift-server-0.3.3.jar 
 *   
 *   org.apache.cassandra.service.CassandraDaemon
 *
 */
class EmbeddedCassandraService
{

    private CassandraDaemon cassandraDaemon;

    public void start() throws IOException
    {
        cassandraDaemon = new CassandraDaemon();
        cassandraDaemon.init(null);
        cassandraDaemon.start();
    }

    public void stop()
    {
        cassandraDaemon.stop();
    }

}
