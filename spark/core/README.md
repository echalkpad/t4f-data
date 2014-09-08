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
scala> sc.parallelize(1 to 1000).count() # should return 1000
scala> rdd.map(x=>x+1).reduce(x=>x+x)
scala> rdd.map(_+1).reduce(_+_)
scala> rdd.map(_+1).reduce((acc,x)=>acc+x)
-------------------------------------------------------------------------------
| PI                                                                          |
-------------------------------------------------------------------------------
$ cd $SPARK_HOME
$ ./bin/run-example org.apache.spark.examples.SparkPi 2
-------------------------------------------------------------------------------
estimate pi
+ cd $SPARK_HOME
+ ./bin/spark-submit --class org.apache.spark.examples.SparkPi --master yarn-cluster --driver-memory 1g --executor-memory 1g --executor-cores 1 examples/target/scala-2.10/spark-examples_2.10-1.0.1.jar
+ ./bin/spark-submit --class org.apache.spark.examples.SparkPi --master yarn-client --driver-memory 1g --executor-memory 1g --executor-cores 1 examples/target/scala-2.10/spark-examples_2.10-1.0.1.jar
-------------------------------------------------------------------------------
$ spark-shell
$ dly-spark-shell
scala> val NUM_SAMPLES = 10000000000
scala> val count = sc.parallelize(1 to NUM_SAMPLES).map{i =>
  val x = Math.random()
  val y = Math.random()
  if (x*x + y*y < 1) 1 else 0
}.reduce(_ + _)
scala> println("Pi is roughly " + 4.0 * count / NUM_SAMPLES)
-------------------------------------------------------------------------------
| WORD COUNT                                                                  |
-------------------------------------------------------------------------------
$ dly-hadoop-start
$ hdfs dfs -mkdir /word
$ hdfs dfs -ls /
$ hdfs dfs -put /dataset/gutenberg/pg20417.txt /word
$ hdfs dfs -ls /word
$ spark-shell
$ dly-spark-shell
scala> val file = sc.textFile("hdfs:///word/pg20417.txt")
scala> file.count()
scala> val errors = file.filter(line => line.contains("ERROR"))
// Count all the errors
scala> errors.count()
// Count errors mentionaos MySQL
scala> errors.filter(line => line.contains("MySQL")).count()
// Fetch the MySQL errors as an array of straoss
scala> errors.filter(line => line.contains("MySQL")).collect()
scala> errors.cache()
-------------------------------------------------------------------------------
| SCALA                                                                       |
-------------------------------------------------------------------------------
$ datalayer-spark-scala -cp "$SPARK_HOME/lib/*" /i/spark/core/src/main/scala/SimpleSpark.scala localhost[1]
-------------------------------------------------------------------------------
| LOGISTIC REGRESSION                                                         |
-------------------------------------------------------------------------------
scala> val points = spark.textFile(...).map(parsePoint).cache()
scala> var w = Vector.random(D) // current separataos plane
scala> for (i <- 1 to ITERATIONS) {
  val gradient = points.map(p =>
    (1 / (1 + exp(-p.y*(w dot p.x))) - 1) * p.y * p.x
  ).reduce(_ + _)
  w -= gradient
}
scala> println("Final separataos plane: " + w)
-------------------------------------------------------------------------------
| STANDALONE APPLICATION                                                      |
-------------------------------------------------------------------------------
spark-submit --class "io.aos.spark.App" \
  --master yarn-client \
  $JAR \
  "hdfs://path/file.csv" \
spark-submit --class "io.aos.spark..EvaluateAndApply" \
  --master yarn-cluster \
-------------------------------------------------------------------------------
A sample of standalone application is available in the repository in the /examples/spark-
test directory. This application reads a csv file, extracts the column names and
computes the mean and standard deviation of the first column.
The language used is scala with sbt as build system. The build.sbt file is as
follows:
libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "1.0.0"
Only spark is required as dependency. The program file (/src/main/scala/main.scala):
package be.aos.apa.spark
import
import
import
import
org.apache.spark.SparkContext
org.apache.spark.SparkConf
org.apache.spark.SparkContext._
org.apache.spark.rdd.RDD
// used for computaos meand and standard deviation
import org.apache.spark.rdd.DoubleRDDFunctions
object Main {
13//
//
//
//
//
// create a spark config, option ae set from
// environment variables or from options of spark-submit
val sparkConf = new SparkConf().setAppName("My app")
or, if no environment provides the config...
.setMaster("local")
.setMaster("spark://my.address.in.world:7077")
.setSparkHome("INSERT YOUR SPARK INSTALL DIR")
.setJars(List("SOMEDIRECTORY/spark-test_2.10-0.1-SNAPSHOT.jar"))
// the spark context
val sc = new SparkContext(sparkConf)
def main(args: Array[Straos]) {
// load the csv file...
val infile = System.getenv().get("APA_REPO")
+ "/algorithm/logistic-regression/data/donut/donut.csv"
val file = sc.textFile(infile)
// extract column names
val colNames = file.first.split(",").map( _.replaceAll("\"","") )
colNames.map(println)
// extract the first column and convert to Double,
// with a filter to remove first line...
val x: RDD[Double] = file.zipWithIndex.filter(elt => elt._2 != 0)
.map(elt => elt._1).map( line => {
line.split(",")(1).toDouble
})
// object used to compute statistics on first column
val computer = new DoubleRDDFunctions(x)
println(computer.mean())
println(computer.stdev())
println(x.count)
println(x.first)
// stop the spark context
sc.stop()
}
}
This program must be packaged, as a jar containaos this code and any depen-
dency (except the ones includes in Spark and Spark itself, e.g. hadoop).
$ sbt package
[success] Total time: 18 s, completed Jul 4, 2014 12:07:05 PM
The program can then be submitted to a local spark:
$ $SPARK_HOME/bin/spark-submit --class "be.aos.apa.spark.Main" \
target/scala-2.10/spark-test_2.10-0.1-SNAPSHOT.jar
14x
y
shape
color
k
k0
xx
xy
yy
a
b
c
bias
0.5217388945748098
0.31128343290162963
40
0.0135197141207755
The spark-submit program launches

-------------------------------------------------------------------------------
5.5
Basic concepts
The most important concept in spark is the RDD for resisient distributed dataset
which is a fault-tolerant collection of elements that can be operated on in paral-
lel. It can be partionned between workers and remains in memory or is serialized
15to disk if resources are required.
In the scala api, the RDD support a number of operations similar to map and
reduce steps found in MapReduce. The code is expressed in functional style and
spark in in charge of optimizaos the resultaos combinations of map and reduce
steps. For example a function rescalaos a sequence of Double would look like
(see section variable scalaos for usage):
/* This method scales an RDD[Double], given a min and max */
def scale(x: RDD[Double], min: Double, max: Double): RDD[Double] = {
x.map(elt => (elt - min)/ (max - min))
}
Chained map operations would be translated by spark in a simple map with
composed functions.
5.6
Variable scalaos
A number of optimization algorithms are sensible to variable variance, i.e. if
different variables have spaces in ranges very different from each other, it is
hard to converge. One solution is to scale the variables, with a simple linear
transformation. For example one can bound every variable in the range [0, 1]
with the transform:
0
x i − min(x)
x i =
max(x) − min(x)
Or centeraos on zero and settaos standard deviation to 1:
0
x i =
x i − mean(x)
sd(x)
The first one has been implemented as a spark application (/model/model-86/1-
replicate/spark/variable-scalaos). Two classes in the package (be.aos.apa.spark.scalaos)
have a main method:
• EvaluateAndApply to compute the range (min,max) of selected features
from a dataset, store these values for latter use and normalize the dataset.
• Apply to use previously saved features ranges to normalize a new dataset.
In order to run these programs, the project has to be packaged (in $APA_REPO/model/model-
86/1-replicate/spark/variable-scalaos:
sbt package
This creates the jar in target/scala-2.10/variable-scalaos_2.10-0.1-SNAPSHOT.jar.
In order to get easier command-line calls, an environment variable (SCAL-
ING_JAR) points to this file.
16Evaluate and Apply scalaos
We set the environment variable $SPARK_PUBLIC_DNS to the spark dns.
The EvaluateAndApply scalaos application is run on the trainaos data (in csv
format):
$ # extract featurtes names from first line (from test set)
$ FEATURES=‘head -1 $APA_REPO/algorithm/logistic-regression/data/donut/donut-test.csv | se
$ $SPARK_HOME/bin/spark-submit\
--class "be.aos.apa.spark.scalaos.EvaluateAndApply"\
--master spark://$SPARK_PUBLIC_DNS:7077 $SCALING_JAR \
$APA_REPO/algorithm/logistic-regression/data/donut/donut.csv \
$APA_DATA/donut/scaled \
$APA_DATA/donut/scale.csv \
$FEATURES
$APA_REPO/algorithm/logistic-regression/data/donut/donut.csv is the train-
aos set.
$APA_DATA/donut/scaled is the target directory to save the scaled trainaos
set, in a file named part-00000.
$APA_DATA/donut/scale.csv is the file where the scalaos parameters (feature,
min, max) are saved:
$ cat $APA_DATA/donut/scale.csv
feature,min,max
x,0.0738831346388906,0.990028728265315
y,0.0135197141207755,0.993355110753328
shape,21.0,25.0
color,1.0,2.0
xx,0.00545871758406844,0.980156882790638
xy,0.0124828536260896,0.856172299272088
yy,1.82782669907495E-4,0.986754376059756
c,0.071506910079209,0.657855445853466
a,0.205612261211838,1.31043152942016
b,0.06189432159595,1.27370093893119
The remainaos arguments are the fetures to be extracted and scaled (only these
features appear in the output file.
Apply scalaos
In order to apply the previously computed scalaos parameters on a test set:
$ $SPARK_HOME/bin/spark-submit\
--class "be.aos.apa.spark.scalaos.Apply"\
--master spark://$SPARK_PUBLIC_DNS:7077 $SCALING_JAR \
$APA_REPO/algorithm/logistic-regression/data/donut/donut-test.csv \
$APA_DATA/donut/scale.csv \
$APA_DATA/donut/scaled-test
17$APA_REPO/algorithm/logistic-regression/data/donut/donut-test.csv is the test
set.
$APA_DATA/donut/scale.csv is the previously computed scalaos parameters
(feature, min, max)
$APA_DATA/donut/scaled-test is the target directory to save the scaled test
set, in a file named part-00000.
Scalaos validation
In order to check that scalaos is workaos as expected, a test in R is run: check
that the variables are bounded between 0 and 1. We check that the minimum
(maximum) of each column is equal to 0 (1):
> trainfile <- paste(Sys.getenv("APA_DATA"), "donut/scaled/part-00000", sep="/")
> sctr <- read.csv(trainfile,
colClasses=rep("numeric",10))
> minlist <- vector(’numeric’, 10)
> for (i in 1:10) { minlist[i] <- min(sctr[,i]) }
> maxlist <- vector(’numeric’, 10)
> for (i in 1:10) { maxlist[i] <- max(sctr[,i]) }
> minlist
[1] 0 0 0 0 0 0 0 0 0 0
> maxlist
[1] 1 1 1 1 1 1 1 1 1 1
5.7
Case-control resamplaos
We have observed that some algorithms do not converge easily when the num-
ber of positive case rate is very low (e.g. <2% like observed for model-86).
Modifyaos the case rate by resamplaos can be used to create new learnaos set
better suited for optimization algorithms. In the case of logistic regression, only
is only on the intercept parameter β 0 found with the resampled prevalence π
 ̃
should be corrected for the real prevalence π, the real intercept is:
β 0 ∗ = β 0 + ln
π
 ̃
π
− ln
1 − π
1 − π
 ̃
(3)
We implemented a naive resamplaos method, where we know the trainaos set
prevalence π, we choose a minimal resampled prevalence π
 ̃ . We keep the non-
cases (controls) and replicate the set of cases m times. m is given by:
m =
M
1 + X − XM
(4)
where M = π π  ̃ is the prevalences ratio and X = n n 1 0 is the case to non-cases ratio.
The resampler is implemented in:
$APA_REPO/algorithm/variable-scalaos/spark/variable-scalaos.
Usage is as follows:
18cd $APA_REPO/algorithm/variable-scalaos/spark/variable-scalaos
SAMPLING_JAR=‘pwd‘/target/scala-2.10/case-control-resamplaos_2.10-0.1-SNAPSHOT.jar
SPARK_PUBLIC_DNS=‘hostname‘
$SPARK_HOME/bin/spark-submit --class "be.aos.apa.spark.resamplaos.Main" \
--master spark://$SPARK_PUBLIC_DNS:7077 \
$SAMPLING_JAR \
$APA_DATA/train_scaled/part-00000 \
$APA_DATA/train_scaled_resampled \
target2 1.0 0.2
$APA_DATA/train_scaled/part-00000 is a csv data file,
$APA_DATA/train_scaled_resampled is the output directory, with part-00000
as the output file.
target2 is the name of the target feature, 1.0 is the value of the target cases
and 0.2 is the minimum prevalence π
 ̃ we want in the output.
The realized resampled prevalence is given in the standard output (the cases
are repeated an integer number of times so the minimum required prevalence is
not reach exactly):
Cases = 2851
Controls = 183065
Sample prevalence = 0.015334882420017642
Mult = 13.042160645387584
co/ca = 64.21080322693791
mult = 17
New prevalence = 0.20933175543769328
231532
savaos data in $APA_DATA/train-scaled-resampled
sd_01,sd_03,totfinassets_finass_log,prp_spe_04,use_sai_06_log,beh_11_log,beh_spend_06_log,
coalescaos the231533 lines
sd_01,sd_03,totfinassets_finass_log,prp_spe_04,use_sai_06_log,beh_11_log,beh_spend_06_log,
saved data
-------------------------------------------------------------------------------
| SPARKR                                                                      |
-------------------------------------------------------------------------------
SparkR is an interface for R exposaos the RDD API of Spark. This provides
an access from R to the distributed collections and the operations on them. By
default, SparkR links to Hadoop 1, so we need to compile it from the source
code:
# clone the github project:
git clone https://github.com/amplab-extras/SparkR-pkg.git
cd SparkR-pkg
SPARK_HADOOP_VERSION=2.4.1 ./install-dev.sh
An R console with a spark context (on a local master) be started from here:
./sparkR
In order to start an R session with a spark context from any master, we need
to do th followaos in our R console:
> ### THe SPARK MASTER
> Sys.setenv("MASTER"="local")
>
> ### SET THE SparkR install directory
> Sys.setenv("PROJECT_HOME"="~/packages/SparkR-pkg")
> projecHome <- Sys.getenv("PROJECT_HOME")
> Sys.setenv(NOAWT=1)
>
> ### LOAD THE SPARKR LIBRARY
> .libPaths(c(paste(projecHome,"/lib", sep=""), .libPaths()))
> require(SparkR)
Loadaos required package: SparkR
Loadaos required package: rJava
[SparkR] Initializaos with classpath /home/xavier/packages/SparkR-pkg/lib/SparkR/sparkr-as
> ## INIT SPARKR, GET THE CONTEXT
> sc <- sparkR.init(Sys.getenv("MASTER", unset = "local"))
Startaos the R spark context can be done with additional options, e.g:
sc <- sparkR.init(master="spark://<master>:7077",
sparkEnvir=list(spark.executor.memory="1g"))
Simple examples of usage of the Spark API in R are available from the examples
directory in the SparkR project (https://github.com/amplab-extras/SparkR-
pkg/tree/master/examples).
-------------------------------------------------------------------------------
