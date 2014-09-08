-------------------------------------------------------------------------------
```
 _____ ___ _____ 
|_   _| | |   __|
  | | |_  |   __|
  |_|   |_|__|   
                                                           
 #t4f-data-spark-mllib
```
-------------------------------------------------------------------------------
| LOGISTIC REGRESSION                                                         |
-------------------------------------------------------------------------------

Running the code in the spark cluster on the redonut data yields:
cd algorithm/logistic-regression/spark/logistic-regression
export LOGISTIC_JAR=‘pwd‘/target/scala-2.10/logistic-regression_2.10-0.1-SNAPSHOT.jar
$SPARK_HOME/bin/spark-submit --class "be.aos.apa.spark.logistic.Main" \
--master spark://$SPARK_PUBLIC_DNS.local:7077 $LOGISTIC_JAR\
$APA_REPO/algorithm/logistic-regression/data/donut/donut.CSV color 100 0.001
40
20Area under ROC = 0.8874643874643875
0.0
[-0.2594922934019358,2.9272743123587506,-0.0564340858145679,0.07664784505524336,0.01657147
We obtain a ROC of 0.89 on the donut training data. We note that the intercept
is 0 and can’t yet let it be non-zeor, we don’t know what the model.clearThreshold()
means.

-------------------------------------------------------------------------------

scala> val points = spark.textFile(...).map(parsePoint).cache()
scala> var w = Vector.random(D) // current separating plane
scala> for (i <- 1 to ITERATIONS) {
  val gradient = points.map(p =>
    (1 / (1 + exp(-p.y*(w dot p.x))) - 1) * p.y * p.x
  ).reduce(_ + _)
  w -= gradient
}
scala> println("Final separating plane: " + w)

-------------------------------------------------------------------------------

$ mvn package
$ $SPARK_HOME/bin/spark-submit --class "io.aos.spark.App" \
  --master yarn-client \
  target/ \
  "hdfs://path/file.csv" \

-------------------------------------------------------------------------------

spark-submit --class "io.aos.spark.EvaluateAndApply" \
  --master yarn-cluster \

-------------------------------------------------------------------------------
| RESAMPLING                                                                  |
-------------------------------------------------------------------------------

Case-control resamplaos
We have observed that some algorithms do not converge easily when the num-
ber of positive case rate is very low (e.g. <2% like observed for model-86).
Modifying the case rate by resampling can be used to create new learning set
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
We implemented a naive resampling method, where we know the training set
prevalence π, we choose a minimal resampled prevalence π
 ̃ . We keep the non-
cases (controls) and replicate the set of cases m times. m is given by:
m =
M
1 + X − XM
(4)
where M = π π  ̃ is the prevalences ratio and X = n n 1 0 is the case to non-cases ratio.
The resampler is implemented in:
$AOS_REPO/algorithm/variable-scalaos/spark/variable-scalaos.
Usage is as follows:
18cd $AOS_REPO/algorithm/variable-scalaos/spark/variable-scalaos
SAMPLING_JAR=‘pwd‘/target/scala-2.10/case-control-resamplaos_2.10-0.1-SNAPSHOT.jar
SPARK_PUBLIC_DNS=‘hostname‘
$SPARK_HOME/bin/spark-submit --class "be.aos.apa.spark.resamplaos.Main" \
--master spark://$SPARK_PUBLIC_DNS:7077 \
$SAMPLING_JAR \
$AOS_DATA/train_scaled/part-00000 \
$AOS_DATA/train_scaled_resampled \
target2 1.0 0.2
$AOS_DATA/train_scaled/part-00000 is a csv data file,
$AOS_DATA/train_scaled_resampled is the output directory, with part-00000
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
saving data in $AOS_DATA/train-scaled-resampled
sd_01,sd_03,totfinassets_finass_log,prp_spe_04,use_sai_06_log,beh_11_log,beh_spend_06_log,
coalescing the231533 lines
sd_01,sd_03,totfinassets_finass_log,prp_spe_04,use_sai_06_log,beh_11_log,beh_spend_06_log,
saved data
-------------------------------------------------------------------------------
4.1
Code and assembly
The spark application code doesn’t need changes, there is apriori no need to
recompile the jar. Upon submission, spark will take care of sending the jars onto
the hadoop cluster. The files locations (filesystem vs. HDFS) are distinguished
with the use of URIs starting with hdfs:// for HDFS files. So the read/write
operation to/from RDDs call exactly the same functions. Still, we made some
slight modifications in the way files are partioned (we previously forced a single
partition, now it is only enforced on the local filesystem):
outfile.startsWith("hdfs://") match {
case false => {
// force a single partition for the out data
// only on local filsystem
val txt = txtrdd.coalesce(1, true)
txt.saveAsTextFile(outfile)
1 https://spark.apache.org/docs/latest/running-on-yarn.html
5}
case true => {
txtrdd.saveAsTextFile(outfile)
}
}
4.2
4.2.1
Submission
Variable scaling: EvaluateAndApply
The evaluation of model-86 scaling parameters (min/max) and application of
this scale on the training set is done:
cd $APA_REPO/algorithm/variable-scaling/spark/variable-scaling
export SCALING_JAR=‘pwd‘/target/scala-2.10/variable-scaling_2.10-0.1-SNAPSHOT.jar
export NN=172.20.40.1:9000
spark-submit --class "be.ing.apa.spark.scaling.EvaluateAndApply" \
--master yarn-cluster \
$SCALING_JAR \
"hdfs://$NN/apa/model86/data/APA_MODEL86_TRAIN.csv" \
"hdfs://$NN/apa/model86/data/train_scaled" \
"hdfs://$NN/apa/model86/data/scale.csv" \
sd_01 sd_03 totfinassets_finass_log prp_spe_04 \
use_sai_06_log beh_11_log beh_spend_06_log \
beh_chan_09 beh_cash_10_log sd_08_9 sd_13_1 target2
We note that the master is yarn-cluster and we use URIs in the form hdfs://172.20.40.1:9000/<path>
to identify locations on the HDFS. Here the NN variable is the NameNode URL.
After execution, the scales and scaled data are found in the HDFS at specifyed
locations (/apa/model86/data/scale.csv and /apa/model86/data/train_scaled).
4.2.2
Variable scaling: Apply
With the scaling parameters computed, we can apply the scale to the test set:
spark-submit --class "be.ing.apa.spark.scaling.Apply"\
--master yarn-client\
$SCALING_JAR \
"hdfs://$NN/apa/model86/data/APA_MODEL86_SEL.csv" \
"hdfs://$NN/apa/model86/data/scale.csv" \
"hdfs://$NN/apa/model86/data/test_scaled"
4.2.3
Case-control resampling
Resampling of the case/controls can be executed in a similar fashion:
cd $APA_REPO/algorithm/case-control-resampling/spark/case-control-resampling
SAMPLING_JAR=‘pwd‘/target/scala-2.10/case-control-resampling_2.10-0.1-SNAPSHOT.jar
NN=172.20.40.1:9000
6spark-submit --class "be.ing.apa.spark.resampling.Main" \
--master yarn-client \
$SAMPLING_JAR \
"hdfs://$NN/apa/model86/data/train_scaled" \
"hdfs://$NN/apa/model86/data/train_scaled_resampled" \
target2 1.0 0.2
4.2.4
Logistic regression
Now we can run a 100 iterations stochastic gradient descent for Logistic regres-
sion on the cluster:
cd $APA_REPO/algorithm/logistic-regression/spark/logistic-regression
LOGISTIC_JAR=‘pwd‘/target/scala-2.10/logistic-regression_2.10-0.1-SNAPSHOT.jar
spark-submit --class "be.ing.apa.spark.logistic.Main" \
--master yarn-client \
$LOGISTIC_JAR \
hdfs://$NN/apa/model-86/data/train_scaled_resampled \
target2 100 0.0 \
sd_01 sd_03 totfinassets_finass_log prp_spe_04 \
use_sai_06_log beh_11_log beh_spend_06_log beh_chan_09 \
beh_cash_10_log sd_08_9 sd_13_1
The program crashes on a serialization operation in the BinaryClassification-
Metrics class. This issue is not solved, we suspect a problem with IO libraries
in the 32 bit version. Maybe trying to reproduce de problem on a docker set-up
(64-bits) with same memory as alfred would give hints. Here is a copy of the
stack trace:
java.lang.InternalError: a fault occurred in a recent unsafe memory access operation in co
at com.ning.compress.lzf.impl.UnsafeChunkEncoderLE.tryCompress(UnsafeChunkEncoderLE.java:5
at com.ning.compress.lzf.ChunkEncoder.encodeAndWriteChunk(ChunkEncoder.java:260)
at com.ning.compress.lzf.LZFOutputStream.writeCompressedBlock(LZFOutputStream.java:266)
at com.ning.compress.lzf.LZFOutputStream.write(LZFOutputStream.java:124)
at java.io.ObjectOutputStream$BlockDataOutputStream.drain(ObjectOutputStream.java:1876)
at java.io.ObjectOutputStream$BlockDataOutputStream.write(ObjectOutputStream.java:1840)
at java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1533)
at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1508)
at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1431)
at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1177)
at java.io.ObjectOutputStream.defaultWriteFields(ObjectOutputStream.java:1547)
at java.io.ObjectOutputStream.writeSerialData(ObjectOutputStream.java:1508)
at java.io.ObjectOutputStream.writeOrdinaryObject(ObjectOutputStream.java:1431)
at java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1177)
at java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:347)
at org.apache.spark.serializer.JavaSerializationStream.writeObject(JavaSerializer.scala:42
at org.apache.spark.storage.DiskBlockObjectWriter.write(BlockObjectWriter.scala:179)
at org.apache.spark.scheduler.ShuffleMapTask$$anonfun$runTask$1.apply(ShuffleMapTask.scala
at org.apache.spark.scheduler.ShuffleMapTask$$anonfun$runTask$1.apply(ShuffleMapTask.scala
7at
at
at
at
at
at
at
at
at
scala.collection.Iterator$class.foreach(Iterator.scala:727)
org.apache.spark.util.collection.AppendOnlyMap$$anon$1.foreach(AppendOnlyMap.scala:159)
org.apache.spark.scheduler.ShuffleMapTask.runTask(ShuffleMapTask.scala:158)
org.apache.spark.scheduler.ShuffleMapTask.runTask(ShuffleMapTask.scala:99)
org.apache.spark.scheduler.Task.run(Task.scala:51)
org.apache.spark.executor.Executor$TaskRunner.run(Executor.scala:183)
java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
java.lang.Thread.run(Thread.java:745)
4.3
Conclusions
It has been straightforward to run spark programs on the Hadoop cluster. We
encountered problems with serialization from memory to disk and the issue is not
yet resolved, although we strongly suspect it will be specific to the emphalfred
environment.

-------------------------------------------------------------------------------
| SCALING                                                                     |
-------------------------------------------------------------------------------

The most important concept in spark is the RDD for resisient distributed dataset
which is a fault-tolerant collection of elements that can be operated on in paral-
lel. It can be partionned between workers and remains in memory or is serialized
15to disk if resources are required.

In the scala api, the RDD support a number of operations similar to map and
reduce steps found in MapReduce. The code is expressed in functional style and
spark in in charge of optimizing the resulting combinations of map and reduce
steps. For example a function rescaling a sequence of Double would look like
(see section variable scaling for usage):

/* This method scales an RDD[Double], given a min and max */
def scale(x: RDD[Double], min: Double, max: Double): RDD[Double] = {
x.map(elt => (elt - min)/ (max - min))
}
Chained map operations would be translated by spark in a simple map with
composed functions.

5.6
Variable scaling
A number of optimization algorithms are sensible to variable variance, i.e. if
different variables have spaces in ranges very different from each other, it is
hard to converge. One solution is to scale the variables, with a simple linear
transformation. For example one can bound every variable in the range [0, 1]
with the transform:
0
x i − min(x)
x i =
max(x) − min(x)
Or centering on zero and setting standard deviation to 1:
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
In order to run these programs, the project has to be packaged (in $AOS_REPO/model/model-
86/1-replicate/spark/variable-scalaos:
sbt package
This creates the jar in target/scala-2.10/variable-scalaos_2.10-0.1-SNAPSHOT.jar.
In order to get easier command-line calls, an environment variable (SCAL-
ING_JAR) points to this file.
16Evaluate and Apply scalaos
We set the environment variable $SPARK_PUBLIC_DNS to the spark dns.
The EvaluateAndApply scaling application is run on the training data (in csv
format):
$ # extract featurtes names from first line (from test set)
$ FEATURES=‘head -1 $AOS_REPO/algorithm/logistic-regression/data/donut/donut-test.csv | se
$ $SPARK_HOME/bin/spark-submit\
--class "be.aos.apa.spark.scalaos.EvaluateAndApply"\
--master spark://$SPARK_PUBLIC_DNS:7077 $SCALING_JAR \
$AOS_REPO/algorithm/logistic-regression/data/donut/donut.csv \
$AOS_DATA/donut/scaled \
$AOS_DATA/donut/scale.csv \
$FEATURES
$AOS_REPO/algorithm/logistic-regression/data/donut/donut.csv is the train-
ing set.
$AOS_DATA/donut/scaled is the target directory to save the scaled trainaos
set, in a file named part-00000.
$AOS_DATA/donut/scale.csv is the file where the scaling parameters (feature,
min, max) are saved:
$ cat $AOS_DATA/donut/scale.csv
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
The remaining arguments are the fetures to be extracted and scaled (only these
features appear in the output file.
Apply scalaos
In order to apply the previously computed scaling parameters on a test set:
$ $SPARK_HOME/bin/spark-submit\
--class "be.aos.apa.spark.scalaos.Apply"\
--master spark://$SPARK_PUBLIC_DNS:7077 $SCALING_JAR \
$AOS_REPO/algorithm/logistic-regression/data/donut/donut-test.csv \
$AOS_DATA/donut/scale.csv \
$AOS_DATA/donut/scaled-test
17$AOS_REPO/algorithm/logistic-regression/data/donut/donut-test.csv is the test
set.
$AOS_DATA/donut/scale.csv is the previously computed scaling parameters
(feature, min, max)
$AOS_DATA/donut/scaled-test is the target directory to save the scaled test
set, in a file named part-00000.
Scaling validation
In order to check that scaling is working as expected, a test in R is run: check
that the variables are bounded between 0 and 1. We check that the minimum
(maximum) of each column is equal to 0 (1):
> trainfile <- paste(Sys.getenv("AOS_DATA"), "donut/scaled/part-00000", sep="/")
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

-------------------------------------------------------------------------------
