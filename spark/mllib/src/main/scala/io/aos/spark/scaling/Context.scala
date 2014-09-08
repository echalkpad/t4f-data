package be.ing.apa.spark.scaling

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

/* Used to store a context common to all uses of the Scaling */

object GlobalScalingContext {
	// create a spark config, option ae set from
	// environment variables or from options of spark-submit
	val sparkConf = new SparkConf().setAppName("Variable Scaling")

	// the spark context
	implicit val sc = new SparkContext(sparkConf)

}
