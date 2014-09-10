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
package io.aos.spark.core.rdd

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import scala.Array.canBuildFrom

object GlobalScalingContext {
  val sparkConf = new SparkConf().setAppName("File").setMaster("local")
  implicit val sc = new SparkContext(sparkConf)
}

object CsvUtil {

  def read(file: String, target: String, featList: List[String])(implicit sc: SparkContext): RDD[String] = {

    val lines = sc.textFile(file)

    // Extract column names -> index
    val colNames = lines.first.split(",").map(_.replaceAll("\"", ""))
      .zipWithIndex.map(tup => (tup._1 -> tup._2)).toMap
    println("colNames=" + colNames)

    //Features is the list of column names excluding target
    val features = colNames.filter(elt => elt._1 != target && featList.contains(elt._1)).map(_._1)
    println("features=" + features)

//    lines.zipWithIndex.filter(elt => elt._2 != 0).map(elt => elt._1).map(line => {
    lines.zipWithIndex.filter(elt => elt._2 != 0).map(elt => elt._1).map(line => {
      // the row with all features
      val fullRow = line.split(",")
      // extract the feature from this row
      val featVals = features.map(feature => {
        val idx = colNames(feature)
        if (feature == "zzz") (1.0 - fullRow(idx).toDouble) else fullRow(idx).toDouble
      })
//      fullRow(colNames(target)).toDouble, Vectors.dense(-1.0 +: featVals.toList.toArray))
      featVals.toList.toString
    })
  }

}

object RddFile {

 import GlobalScalingContext._

  def main(args: Array[String]) {
    val data = CsvUtil.read("/dataset/donut/donut-2.csv", "color", List("x", "y", "c"))
    data.toArray().foreach(line => println(line))
    data.take(0).foreach(println)
    data.collect().foreach(println)
  }

}
