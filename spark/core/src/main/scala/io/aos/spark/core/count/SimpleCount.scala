/**
 * **************************************************************
 * Licensed to the AOS Community (AOS) under one or more        *
 * contributor license agreements.  See the NOTICE file         *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The AOS licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 * **************************************************************
 */
/**
 * dly-scala -cp /c/spark.git/assembly/target/scala-2.10/spark-assembly-1.0.0-SNAPSHOT-hadoop2.4.0.jar ./SimpleApp1.scala localhost[1]
 */
package io.aos.spark.core.count

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

object SimpleCount {

  def main(args: Array[String]) {

    val prefix = "========================================================== "

    val logFile = "/dataset/gutenberg/pg20417.txt" // Should be some file on your system

    val spark = new SparkContext("local", "Simple App")
    //    val spark = new SparkContext("local", "Simple App", "YOUR_SPARK_HOME", List("target/scala-2.10/simple-project_2.10-1.0.jar"))

    val logData = spark.textFile(logFile, 2).cache()

    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()

    println(prefix + "Lines with a: %s, Lines with b: %s".format(numAs, numBs))

    val counts = logData.flatMap(line => line.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _)

    counts.saveAsTextFile("/out.txt")

    spark.stop()

  } 

}
