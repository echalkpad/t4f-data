-------------------------------------------------------------------------------
```
 _____ ___ _____ 
|_   _| | |   __|
  | | |_  |   __|
  |_|   |_|__|   
                                                           
 #t4f-data-spark
```

# Build

Build with maven

```
$ mvn clean install -DskipTests -Dhadoop.version=2.4.0 -Dyarn.version=2.4.0
$ mvn clean install -Pdeb,yarn-alpha -DskipTests -Dhadoop.version=2.4.0 -Dyarn.version=2.4.0
$ mvn clean install -Pbigtop-dist,yarn -DskipTests -Dhadoop.version=2.4.0 -Dyarn.version=2.4.0
```
or use the available script (which in its turn, calls maven)

```
$ make-distribution.sh --hadoop 2.5.0 --tgz --with-yarn --with-hive --name 2.5.0
```

# User Interface

When Spark runs, it gives you a useful user interface to manage and monitor your jobs and configuration.

+ http://localhost:4040

# Run Options

The available options are (see details after):

$ spark-shell --master <master-url>
+ local
+ local[4]
+ yarn-client
+ yarn-cluster
+ spark://host:port
+ mesos://host:port

# Hadoop Configuration Directory

Hadoop deployment depends on HADOOP_CONF_DIR

## Spark Local

$ HADOOP_CONF_DIR= MASTER=local spark-shell

## Spark Hadoop

Check your HADOOP_CONF_DIR

$ spark-shell

## Spark Hadoop Yarn

There are two deploy modes that can be used to launch Spark applications on YARN. 

1. In yarn-client mode, the driver runs in the client process, and the 
   application master is only used for requesting resources from YARN.

$ spark-shell --master yarn-client --driver-memory 1g --executor-memory 1g --executor-cores 1

2. In yarn-cluster mode, the Spark driver runs inside an application master 
   process which is managed by YARN on the cluster, and the client can go away 
   after initiating the application. 

$ spark-shell --master yarn-cluster

## Spark Standalone

$ $SPARK_HOME/sbin/start-master.sh -m 4G
+ see SPARK_PUBLIC_DNS master hostname (port 7077) in the log file
$ $SPARK_HOME/bin/spark-class org.apache.spark.deploy.worker.Worker spark://$SPARK_PUBLIC_DNS:7077 -m 2G
$ $SPARK_HOME/bin/spark-shell --master spark://$SPARK_PUBLIC_DNS:7077

## Spark Mesos

(to be detailed)

-------------------------------------------------------------------------------
