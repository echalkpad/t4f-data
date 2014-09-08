-------------------------------------------------------------------------------
```
 _____ ___ _____ 
|_   _| | |   __|
  | | |_  |   __|
  |_|   |_|__|   
                                                           
 #t4f-data-spark
```
-------------------------------------------------------------------------------
| BUILD                                                                       |
-------------------------------------------------------------------------------
mvn clean install -DskipTests -Dhadoop.version=2.4.0 -Dyarn.version=2.4.0
mvn clean install -Pdeb,yarn-alpha -DskipTests -Dhadoop.version=2.4.0 -Dyarn.version=2.4.0
mvn clean install -Pbigtop-dist,yarn -DskipTests -Dhadoop.version=2.4.0 -Dyarn.version=2.4.0
-------------------------------------------------------------------------------
$ make-distribution.sh --hadoop 2.4.0 --tgz --with-yarn --with-hive --name 2.4.0
-------------------------------------------------------------------------------
| SETUP STANDALONE                                                            |
-------------------------------------------------------------------------------
+ no mesos nor yarn
+ $SPARK_HOME/sbin/start-master.sh -m 4G
+ see SPARK_PUBLIC_DNS master hostnmame (port 7077) in the  log file
+ $SPARK_HOME/bin/spark-class org.apache.spark.deploy.worker.Worker spark://$SPARK_PUBLIC_DNS:7077 -m 2G
Now, the local cluster is able to process spark applications submissions, includ-
aos the spark shell
$SPARK_HOME/bin/spark-shell --master spark://$SPARK_PUBLIC_DNS:7077
And the test application:
$SPARK_HOME/bin/spark-submit --class "be.aos.apa.spark.Main" \
  target/scala-2.10/spark-test_2.10-0.1-SNAPSHOT.jar \
  --master "spark://$SPARK_PUBLIC_DNS:7077"
-------------------------------------------------------------------------------
| SETUP YARN                                                                  |
-------------------------------------------------------------------------------
There are two deploy modes that can be used to launch Spark applications on YARN. 
+ In yarn-cluster mode, the Spark driver runs inside an application master process which is managed by YARN on the cluster, and the client can go away after initiataos the app
lication. 
+ In yarn-client mode, the driver runs in the client process, and the application master is only used for requestaos resources from YARN.
-------------------------------------------------------------------------------
| SETUP SHELL                                                                 |
-------------------------------------------------------------------------------
+ spark-cli --master <master-url>
+ <master-url>
++ local
++ local[4]
++ yarn-client
++ yarn-cluster
++ spark://host:port
++ mesos://host:port
-------------------------------------------------------------------------------
HADOOP_CONF_DIR=. MASTER=local spark-shell
-------------------------------------------------------------------------------
spark-shell --master yarn-client
spark-shell --master yarn-client --driver-memory 1g --executor-memory 1g --executor-cores 1
-------------------------------------------------------------------------------
| UI                                                                          |
-------------------------------------------------------------------------------
+ http://localhost:4040
+ http://localhost:8080
-------------------------------------------------------------------------------
