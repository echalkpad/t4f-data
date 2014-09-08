package io.aos.spark.core.mean

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import org.apache.spark.rdd.DoubleRDDFunctions
import scala.Array.canBuildFrom

object SimpleMean {

  val prefix = "========================================================== "

  val sparkConf = new SparkConf().setAppName("MeanMain")
  //		.setMaster("local").setMaster("spark://my.address.in.world:7077")
  //		.setSparkHome("INSERT YOUR SPARK INSTALL DIR")
  //		.setJars(List("/spark-logit_2.10-0.1-SNAPSHOT.jar"))

  val sc = new SparkContext(sparkConf)

  def main(args: Array[String]) {

    val infile = "/dataset/donut/donut.csv"

    val file = sc.textFile(infile)

    // Extract column names.
    val colNames = file.first.split(",").map(_.replaceAll("\"", ""))
    colNames.map(println)

    // Extract the first column and convert to Double, with a filter to remove first line...
    val x: RDD[Double] = file.zipWithIndex.filter(elt => elt._2 != 0).map(elt => elt._1).map(line => {
      line.split(",")(1).toDouble
    })

    // Object used to compute statistics on first column
    val firstColumn = new DoubleRDDFunctions(x)
    println(prefix + "firstColumn.mean()" + firstColumn.mean())
    println(prefix + "firstColumn.stdev()" + firstColumn.stdev())
    println(prefix + "x.count" + x.count)
    println(prefix + "x.first" + x.first)

    // Stop the spark context
    sc.stop()

  }

}
