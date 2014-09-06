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
| CLI                                                                         |
-------------------------------------------------------------------------------
+ spark-cli --master <master-url>
-------------------------------------------------------------------------------
+ <master-url>
++ local
++ local[4]
++ yarn-client
++ spark://host:port
++ mesos://host:port
-------------------------------------------------------------------------------
spark-shell --master yarn-client --
spark-submit --class "be.ing.apa.spark.scaling.EvaluateAndApply" \
  --master yarn-custer \
spark-submit --class "io.aos.spark.App" \
  --master yarn-client \
  $JAR \
  "hdfs://path/file.csv" \
-------------------------------------------------------------------------------
+ rdd.map(x=>x+1).reduce(x=>x+x)
+ rdd.map(_+1).reduce(_+_)
+ rdd.map(_+1).reduce((acc,x)=>acc+x)
-------------------------------------------------------------------------------
| UI                                                                          |
-------------------------------------------------------------------------------
http://localhost:4040
-------------------------------------------------------------------------------

The standalone mode allows to run spark in cluster mode, with a master and
workers without the need for a yarn or mesos cluster. The set-up is done fol-
lowing the guide: From the spark directory, start the master node, here with
4GB RAM:
./sbin/start-master.sh -m 4G
By default, the master starts on the port 7077, the log file indicates the url of the
master. The cluster is on http monitored from the port 8080: http://localhost:8080
We can then start a worker node, beware that the master url (spark://$SPARK_PUBLIC_DNS:7077)
must be the servername, not the IP.
./bin/spark-class org.apache.spark.deploy.worker.Worker\
spark://$SPARK_PUBLIC_DNS:7077 -m 2G
Now, the local cluster is able to process spark applications submissions, includ-
ing the spark shell
$SPARK_HOME/bin/spark-shell --master spark://$SPARK_PUBLIC_DNS:7077
And the test application:
$SPARK_HOME/bin/spark-submit --class "be.ing.apa.spark.Main" \
target/scala-2.10/spark-test_2.10-0.1-SNAPSHOT.jar \
--master "spark://$SPARK_PUBLIC_DNS:7077"

-------------------------------------------------------------------------------
cd /usr/local/spark
./bin/spark-shell --master yarn-client --driver-memory 1g --executor-memory 1g --executor-cores 1
# execute the the following command which should return 1000
scala> sc.parallelize(1 to 1000).count()
-------------------------------------------------------------------------------
There are two deploy modes that can be used to launch Spark applications on YARN. In yarn-cluster mode, the Spark driver runs inside an application master process which is managed by YARN on the cluster, and the client can go away after initiating the app
lication. In yarn-client mode, the driver runs in the client process, and the application master is only used for requesting resources from YARN.

Estimating Pi (yarn-cluster mode): 
cd /usr/local/spark
# execute the the following command which should write the "Pi is roughly 3.1418" into the logs
./bin/spark-submit --class org.apache.spark.examples.SparkPi --master yarn-cluster --driver-memory 1g --executor-memory 1g --executor-cores 1 examples/target/scala-2.10/spark-examples_2.10-1.0.1.jar

Estimating Pi (yarn-client mode):
cd /usr/local/spark
# execute the the following command which should print the "Pi is roughly 3.1418" to the screen
./bin/spark-submit --class org.apache.spark.examples.SparkPi --master yarn-client --driver-memory 1g --executor-memory 1g --executor-cores 1 examples/target/scala-2.10/spark-examples_2.10-1.0.1.jar
```

