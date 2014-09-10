-------------------------------------------------------------------------------
```
 _____ ___ _____ 
|_   _| | |   __|
  | | |_  |   __|
  |_|   |_|__|   
                                                           
 #t4f-data-spark-core
```
-------------------------------------------------------------------------------
| SHELL                                                                       |
-------------------------------------------------------------------------------

$ spark-shell
$ datalayer-spark-shell

-------------------------------------------------------------------------------

$ hdfs dfs -put /dataset/donut/donut.csv /donut.csv
$ hdfs dfs -ls /
$ spark-shell
scala> val data = sc.textFile("/dataset/donut/donut.csv")
scala> data.first

-------------------------------------------------------------------------------

scala> sc.parallelize(1 to 1000).count() # should return 1000

-------------------------------------------------------------------------------

-------------------------------------------------------------------------------
| SUGAR                                                                       |
-------------------------------------------------------------------------------

scala> rdd.map(x=>x+1).reduce(x=>x+x)
scala> rdd.map(_+1).reduce((acc,x)=>acc+x)
scala> rdd.map(_+1).reduce(_+_)

-------------------------------------------------------------------------------
| PI                                                                          |
-------------------------------------------------------------------------------

$ cd $SPARK_HOME
$ ./bin/run-example org.apache.spark.examples.SparkPi 2

-------------------------------------------------------------------------------

$ cd $SPARK_HOME
$ ./bin/spark-submit --class org.apache.spark.examples.SparkPi --master yarn-client --driver-memory 1g --executor-memory 1g --executor-cores 1 ./lib/spark-examples*.jar
$ ./bin/spark-submit --class org.apache.spark.examples.SparkPi --master yarn-cluster --driver-memory 1g --executor-memory 1g --executor-cores 1 ./lib/spark-examples*.jar

-------------------------------------------------------------------------------

scala> val NUM_SAMPLES = 1000000
scala> val count = sc.parallelize(1 to NUM_SAMPLES).map{i =>
    val x = Math.random()
    val y = Math.random()
    if (x*x + y*y < 1) 1 else 0 
  }.reduce(_ + _)
scala> println("Pi is roughly " + 4.0 * count / NUM_SAMPLES)

-------------------------------------------------------------------------------
| WORD COUNT                                                                  |
-------------------------------------------------------------------------------

$ hdfs dfs -mkdir /dataset
$ hdfs dfs -mkdir /dataset/gutenberg
$ hdfs dfs -ls /
$ hdfs dfs -put /dataset/gutenberg/pg20417.txt /dataset/gutenberg
$ hdfs dfs -ls /word

$ spark-shell

scala> val file = sc.textFile("hdfs:///dataset/gutenberg/pg20417.txt")
scala> file.count()
scala> val the = file.filter(line => line.contains("the"))
scala> the.count()
scala> the.filter(line => line.contains("that")).count()
scala> the.filter(line => line.contains("that")).collect()
scala> the.cache()

-------------------------------------------------------------------------------
| SCALA                                                                       |
-------------------------------------------------------------------------------

$ spark-class -cp "$SPARK_HOME/lib/*:./target/t4f-data-spark-core-1.0.0-SNAPSHOT.jar" Simple --master yarn-client

$ datalayer-spark-scala -cp "$SPARK_HOME/lib/*" $DATALAYER_SRC_HOME/t4f-data.git/spark/core/src/main/scala/Simple.scala

-------------------------------------------------------------------------------
| STANDALONE APPLICATION                                                      |
-------------------------------------------------------------------------------

This application reads a csv file, extracts the column names and
computes the mean and standard deviation of the first column.
```
$ mvn package; $SPARK_HOME/bin/spark-submit  \
  ./target/t4f-data-spark-core-1.0.0-SNAPSHOT.jar \
  --class "io.aos.spark.core.mean.SimpleMean" \
  --master yarn-client
```
```
$ mvn package; $SPARK_HOME/bin/spark-submit  \
  ./target/t4f-data-spark-core-1.0.0-SNAPSHOT.jar \
  --class "io.aos.spark.core.count.SimpleCount" \
  --master yarn-client
```
-------------------------------------------------------------------------------
