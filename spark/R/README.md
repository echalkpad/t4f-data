-------------------------------------------------------------------------------
```
 _____ ___ _____ 
|_   _| | |   __|
  | | |_  |   __|
  |_|   |_|__|   
                                                           
 #t4f-data-spark-R
```
-------------------------------------------------------------------------------
| SPARK-R                                                                     |
-------------------------------------------------------------------------------
SparkR is an interface for R exposaos the RDD API of Spark. This provides
an access from R to the distributed collections and the operations on them. By
default, SparkR links to Hadoop 1, so we need to compile it from the source
-------------------------------------------------------------------------------
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
-------------------------------------------------------------------------------
Startaos the R spark context can be done with additional options, e.g:
sc <- sparkR.init(master="spark://<master>:7077",
sparkEnvir=list(spark.executor.memory="1g"))
Simple examples of usage of the Spark API in R are available from the examples
directory in the SparkR project (https://github.com/amplab-extras/SparkR-
pkg/tree/master/examples).
-------------------------------------------------------------------------------
