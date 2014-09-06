package io.datalayer.apa.spark

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext._ 
import org.apache.spark.rdd.RDD

// used for computing meand and standard deviation
import org.apache.spark.rdd.DoubleRDDFunctions

object Main {

	// create a spark config, option ae set from
	// environment variables or from options of spark-submit
	val sparkConf = new SparkConf().setAppName("My app")
// or, if no environment provides the config...
//		.setMaster("local")
//		.setMaster("spark://my.address.in.world:7077")
//		.setSparkHome("INSERT YOUR SPARK INSTALL DIR")
//		.setJars(List("SOMEDIRECTORY/spark-logit/target/scala-2.10/spark-logit_2.10-0.1-SNAPSHOT.jar"))

	// the spark context
	val sc = new SparkContext(sparkConf)

	def main(args: Array[String]) {

		// load the csv file...
		val infile = System.getenv().get("APA_REPO") + "/algorithm/logistic-regression/data/donut/donut.csv"
		val file = sc.textFile(infile)
		// extract column names
		val colNames = file.first.split(",").map( _.replaceAll("\"","") )
		colNames.map(println)
		// extract the first column and convert to Double, with a filter to remove first line...
		val x: RDD[Double] = file.zipWithIndex.filter(elt => elt._2 != 0).map(elt => elt._1).map( line => {
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
