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
+ Machine learning algorithms are implemented in the MLLib project, an exten-
sion running on spark. Logistic regression is implemented with Stochastic
gradient descent. A simple scala project to run this has been implemented
in $APA_REPO/algorithm/logistic-regression/spark/logistic-regression. Here
is the code:
-------------------------------------------------------------------------------
Running the code in the spark cluster on the redonut data yields:

cd $APA_REPO
cd algorithm/logistic-regression/spark/logistic-regression

export LOGISTIC_JAR=‘pwd‘/target/scala-2.10/logistic-regression_2.10-0.1-SNAPSHOT.jar

$SPARK_HOME/bin/spark-submit --class "be.ing.apa.spark.logistic.Main" \
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
