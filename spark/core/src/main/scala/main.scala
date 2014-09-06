package io.aos.spark.scaling

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext._ 
import org.apache.spark.rdd.RDD

// used for computing meand and standard deviation
import org.apache.spark.rdd.DoubleRDDFunctions

import Scaling._

/* Used to store a context common to all uses of the Scaling */

object GlobalScalingContext {
	// create a spark config, option ae set from
	// environment variables or from options of spark-submit
	val sparkConf = new SparkConf().setAppName("Variable Scaling")

	// the spark context
	implicit val sc = new SparkContext(sparkConf)

}


object EvaluateAndApply {

	import GlobalScalingContext._

	def main(args: Array[String]) {


		if (args.length < 4) {
			println("""
				Missing arguments...
				Usage:
				EvaluateAndApply <infile> <outfile> <scalefile> <feature1> ...

				""")
			System.exit(-1)
		}

		val infile = args(0)
		val outfile = args(1)
		val outscalefile = args(2)

		val features = args.drop(3).toList

		// load the csv file...
		val file = sc.textFile(infile)
		// extract column names
		val colNames = file.first.split(",").map( _.replaceAll("\"","") )
			.zipWithIndex.map(tup => (tup._1 -> tup._2)).toMap


		/* this method extracts the list of features for each record */
		def getDataForFeatures(lines: RDD[String]): RDD[List[Double]] = {
			val feats = features
			val names = colNames
			lines.zipWithIndex.filter(elt => elt._2 != 0).map(elt => elt._1).map( line => {
				// the row with all features
				val fullRow = line.split(",")
				// extract the featute from this row
				feats.map( feature => {	
					val idx = colNames(feature)
					fullRow(idx).toDouble
					})
			})
		}

		/* This method returns the column identified by feature number
		*/
		def getDataForFeature(lines: RDD[String], feature: String): RDD[Double] = {
			val idx: Int = colNames(feature)
			lines.zipWithIndex.filter(elt => elt._2 != 0).map(elt => elt._1).map( line => {
				line.split(",")(idx).toDouble
			})
		}


		// for each feature, extract the column and compute scale parameters (min, max)
		// scale the corresponding column in a RDD
		val scales = features.map( feature => {
			val idx = colNames(feature)
			println("DOING FEATURE " + feature + " " + idx)
			val x = getDataForFeature(file, feature)
			val featScale = (feature, x.min, x.max)
			//val scaledX = scale(x, featScale._2, featScale._3)
			featScale
			})

		// save the scales
		saveScales(scales, outscalefile)

		// now, build a scaled version of the dataset
		// each row will be an Array of Double, ordered as features
		val filtered = getDataForFeatures(file)
		//println(filtered.count)

		// scale the data with computed scales
		val scaled = scaleFeatures(filtered, scales)
//		println(scaled.count)

		println("saving data in " + outfile)
		// create the header entry
		val hlst = Array[String](features.mkString(","))
		println(hlst)
		val hdr: RDD[String] = sc.parallelize(hlst)
		println(hdr.count)
		println("header ready, cat with data...")
		val txtrdd = (hdr ++ scaled.map(elt => elt.mkString(",")))
		outfile.startsWith("hdfs://") match {
			case false => {
			    // cat the header with data as strings and save file
			    println("coalescing the" + txtrdd.count + " lines")
			    val txt = txtrdd.coalesce(1, true)
			    println(txt.first)
			    txt.saveAsTextFile(outfile)
			    println("saved data")				
			}
			case true => {
				println("saving the " + txtrdd.count + " lines")
				txtrdd.saveAsTextFile(outfile)
				println("saved data")
			}
		}
		
	}

}


object Apply {

	import GlobalScalingContext._

	def main(args: Array[String]) {

		if (args.length < 3) {
			println("""
				Missing arguments...
				Usage:
				EvaluateAndApply <infile> <scalefile> <outfile>

				""")
			System.exit(-1)
		}
		val infile = args(0)
		val outfile = args(2)
		val scalefile = args(1)

		val scales = readScales(scalefile)
		val features = scales.map(_._1)

		// load the csv file...
		val file = sc.textFile(infile)
		// extract column names
		val colNames = file.first.split(",").map( _.replaceAll("\"","") )
			.zipWithIndex.map(tup => (tup._1 -> tup._2)).toMap
features.map(elt => println(elt + "\t" + colNames(elt)))
		/* this method extracts the list of features for each record */
		def getDataForFeatures(lines: RDD[String]): RDD[List[Double]] = {
			val feats = features
			val names = colNames
			lines.zipWithIndex.filter(elt => elt._2 != 0).map(elt => elt._1).map( line => {
				// the row with all features
				val fullRow = line.split(",")
				// extract the featute from this row
				feats.map( feature => {	
					val idx = colNames(feature)
					fullRow(idx).toDouble
					})
			})
		}
		// now, build a scaled version of the dataset
		// each row will be an Array of Double, ordered as features
		val filtered = getDataForFeatures(file)
		//println(filtered.count)

		// scale the data with computed scales
		val scaled = scaleFeatures(filtered, scales)
		println(scaled.count)

		println("saving data in " + outfile)
		// create the header entry
		val hlst = Array[String](features.mkString(","))
		println(hlst)
		val hdr: RDD[String] = sc.parallelize(hlst)
		println(hdr.count)
		println("header ready, cat with data...")
		val txtrdd = (hdr ++ scaled.map(elt => elt.mkString(",")))
		outfile.startsWith("hdfs://") match {
			case false => {
			    // cat the header with data as strings and save file
			    println("coalescing the" + txtrdd.count + " lines")
			    val txt = txtrdd.coalesce(1, true)
			    println(txt.first)
			    txt.saveAsTextFile(outfile)
			    println("saved data")				
			}
			case true => {
				println("saving the " + txtrdd.count + " lines")
				txtrdd.saveAsTextFile(outfile)
				println("saved data")
			}
		}

	}
}