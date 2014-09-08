package io.aos.spark.mllib.resampling

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import scala.Array.canBuildFrom

/* Used to store a context common to all uses of the Scaling */

object GlobalResamplingContext {
    // create a spark config, option ae set from
    // environment variables or from options of spark-submit
    val sparkConf = new SparkConf().setAppName("Case Controle Resampling")

    // the spark context
    implicit val sc = new SparkContext(sparkConf)

}


object Main2 {

import GlobalResamplingContext._

    def main(args: Array[String]) {


        if (args.length < 4) {
            println("""
                Missing arguments...
                Usage:
                Resample <infile> <outfile> <target> <case_label> <target_rate>
                """)
            System.exit(-1)
        }

        val infile = args(0)
        val outfile = args(1)
        val target = args(2)
        val caseLabel = args(3).toDouble
        val targetRate = args(4).toDouble

        //val features = args.drop(3).toList

        // load the csv file...
        val file = sc.textFile(infile)
        // extract column names
        val colNames = file.first.split(",").map( _.replaceAll("\"","") )
            .zipWithIndex.map(tup => (tup._1 -> tup._2)).toMap

        // remove the first line (headers)
        val lines = file.zipWithIndex.filter(elt => elt._2 != 0).map(elt => elt._1)
        val headerLine = file.first

        /* the method extract a named feature from a line */
        def getLineFeature(feature: String, line: String): Double = {
            val names = colNames
            val fullRow = line.split(",")
            fullRow(colNames(feature)).toDouble
        }

        // scan the file an extract the cases and controls
        val caseLines = lines.filter(elt => getLineFeature(target, elt) == caseLabel)
        val controlLines = lines.filter(elt => getLineFeature(target, elt) != caseLabel)

        val caseNum = caseLines.count
        val controlNum = controlLines.count
        val controlToCase = (1.0*controlNum)/caseNum
        val multiplier = (targetRate*(caseNum+controlNum))/caseNum
        println("Cases = " + caseNum)
        println("Controls = " + controlNum)

        println("Sample prevalence = " + (1.0*caseNum)/(caseNum+controlNum))
        println("Mult = " + multiplier)
        println("co/ca = " + controlToCase)
        
        // repeat the cases multiplier times
        val multInt = ((multiplier*controlToCase) / (1.0 + controlToCase - multiplier)).ceil.intValue
        println("mult = " + multInt)
        //val multInt = multiplier.ceil.intValue
        val multFrac = multiplier - multInt

        val repeatedCases = caseLines.flatMap(elt => Seq.fill(multInt)(elt))
        val repeatedCaseNum = repeatedCases.count
        println("New prevalence = " + (1.0*repeatedCaseNum)/(repeatedCaseNum+controlNum))

        // concat cases and controls, reshuffle
        val newLines = (repeatedCases ++ controlLines).sample(false, 1.0)
        println(newLines.count)
/*
        // save the scales
        saveScales(scales, outscalefile)

        // now, build a scaled version of the dataset
        // each row will be an Array of Double, ordered as features
        val filtered = getDataForFeatures(file)
        //println(filtered.count)

        // scale the data with computed scales
        val scaled = scaleFeatures(filtered, scales)
//      println(scaled.count)
*/
        println("saving data in " + outfile)
        println(headerLine)
        val txtrdd = (sc.parallelize(List[String](headerLine)) ++ newLines)
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
