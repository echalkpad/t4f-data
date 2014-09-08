package io.aos.spark.mllib.scaling

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext._ 
import org.apache.spark.rdd.RDD

// used for computing meand and standard deviation
import org.apache.spark.rdd.DoubleRDDFunctions

import Scaling._

object SelectVariables {

	import GlobalScalingContext._

	def main(args: Array[String]) {


		if (args.length < 3) {
			println("""
				Missing arguments...
				Usage:
				SelectVariables <infile> <outfile> <feature1> ...

				""")
			System.exit(-1)
		}

		val infile = args(0)
		val outfile = args(1)
		val features = args.drop(2).toList

		// load the csv file...
		val file = sc.textFile(infile)
		// extract column names
		val colNames = file.first.split("\t").map( _.replaceAll("\"","") )
			.zipWithIndex.map(tup => (tup._1 -> tup._2)).toMap
		println(colNames)

		/* this method extracts the list of features for each record */
		def getDataForFeatures(lines: RDD[String]): RDD[List[Double]] = {
			val feats = features
			val names = colNames
			lines.zipWithIndex.filter(elt => elt._2 != 0).map(elt => elt._1).map( line => {
				// the row with all features
				val fullRow = line.split("\t")
				// extract the featute from this row
				feats.map( feature => {	
					if (! colNames.contains(feature)) {
						println(110)
						println("TEST")
						println(s"###### FAILED WITH KEY: $feature " + colNames)
					}
					val idx = colNames(feature)
					if (fullRow.size < idx + 1) println(fullRow)
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


		// now, build a filterd version of the dataset
		// each row will be an Array of Double, ordered as features
		val filtered = getDataForFeatures(file)
		println(filtered.count)

		println("saving data in " + outfile)
		// create the header entry
		val hlst = Array[String](features.mkString(","))
		println(hlst)
		val hdr: RDD[String] = sc.parallelize(hlst)
		println(hdr.count)
		println("header ready, cat with data...")
		val txtrdd = (hdr ++ filtered.map(elt => elt.mkString(",")))
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
