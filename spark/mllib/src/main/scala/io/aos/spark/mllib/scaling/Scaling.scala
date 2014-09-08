package io.aos.spark.mllib.scaling

import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext


object Scaling {
	

	type Scale = (String, Double, Double)

	/* Store the scales in csv format
	*	scales is a list of Scales
	* 
	*
	 */
	def saveScales(scales: List[Scale], file: String)(implicit sc: SparkContext) = file.startsWith("hdfs://") match {
		case false => saveScalesLocal(scales, file)
		case true => saveScalesHdfs(scales, file)(sc)
	}

	def saveScalesLocal(scales: List[Scale], file: String) = {
		val pw = new java.io.PrintWriter(file)
		pw.println("feature,min,max")
		scales.map( elt => pw.println(elt._1 + "," + elt._2 + "," + elt._3) )
		pw.close

	}

	def saveScalesHdfs(scales: List[Scale], file: String)(implicit sc: SparkContext) = {
		val rdd  = sc.parallelize( "feature,min,max" :: scales.map(elt => elt._1 + "," + elt._2 + "," + elt._3) )
		rdd.saveAsTextFile(file)
	}

	/* read the scales from a csv file
	*
	*
	*/
	def readScales(infile: String)(implicit sc: SparkContext): List[Scale] = {
		val lines = sc.textFile(infile)

		val scalesRDD: RDD[Scale] = lines.zipWithIndex.filter(elt => elt._2 != 0).map(elt => elt._1).map( line => {
			val tup = line.split(",")
			(tup(0), tup(1).toDouble, tup(2).toDouble)
			})
		scalesRDD.toLocalIterator.toList
	}

	/* This method scales an RDD[Double], given a min and max */
	def scale(x: RDD[Double], min: Double, max: Double): RDD[Double] = {
		x.map(elt => (elt - min)/ (max - min))
	} 


	/* This method scales all features of an RDD
	* each item of the RDD is a list of Double
	* the scales is the list of scales for the features
	*  (same order a in the RDD item)
	*/
	def scaleFeatures(x: RDD[List[Double]], scales: List[Scale]): RDD[List[Double]] = {
		x.map(elt => {
			elt.zip(scales).map( tup => {
				// compute scale = (x - xmin)/(xmax - xmin)
				(tup._1 - tup._2._2)/ (tup._2._3 - tup._2._2)
				})
			})
	}

}


		
