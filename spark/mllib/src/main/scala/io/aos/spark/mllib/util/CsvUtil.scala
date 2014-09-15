package io.aos.spark.mllib.util

import scala.Array.canBuildFrom

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.linalg.DenseVector
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Matrix
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

object CsvUtil {
  
  def SEPARATOR = ","

  def readLabeledPoints(file: String, target: String)(implicit sc: SparkContext): RDD[LabeledPoint] = {
    val lines = sc.textFile(file)
    // extract column names -> index
    val colNames = lines.first.split(SEPARATOR).map(_.replaceAll("\"", ""))
      .zipWithIndex.map(tup => (tup._1 -> tup._2)).toMap
    //features is the list of column names excluding target
    val features = colNames.filter(_._1 != target).map(_._1)
    lines.zipWithIndex.filter(elt => elt._2 != 0).map(elt => elt._1).map(line => {
      // the row with all features
      val fullRow = line.split(SEPARATOR)
      // extract the feature from this row
      val featVals = features.map(feature => {
        val idx = colNames(feature)
        fullRow(idx).toDouble
      })
      LabeledPoint(fullRow(colNames(target)).toDouble, Vectors.dense(1.0 +: featVals.toList.toArray))
    })
  }

  def readLabeledPoints(file: String, target: String, featList: List[String])(implicit sc: SparkContext): RDD[LabeledPoint] = {

    val lines = sc.textFile(file)

    // Extract column names -> index
    val colNames = lines.first.split(SEPARATOR).map(_.replaceAll("\"", ""))
      .zipWithIndex.map(tup => (tup._1 -> tup._2)).toMap

    // Features is the list of column names excluding target
    val features = colNames.filter(elt => elt._1 != target && featList.contains(elt._1)).map(_._1)

    lines.zipWithIndex.filter(elt => elt._2 != 0).map(elt => elt._1).map(line => {
      // The row with all features
      val fullRow = line.split(SEPARATOR)
      // Extract the feature from this row
      val featVals = features.map(feature => {
        val idx = colNames(feature)
        if (feature == "zzz") (1.0 - fullRow(idx).toDouble) else fullRow(idx).toDouble
      })
      LabeledPoint(fullRow(colNames(target)).toDouble, Vectors.dense(1.0 +: featVals.toList.toArray))
    })

  }

  def readMatrix(file: String)(implicit sc: SparkContext): RDD[Vector] = {

    val lines = sc.textFile(file)

    // Extract column names -> index
    val colNames = lines.first.split(SEPARATOR).map(_.replaceAll("\"", ""))
      .zipWithIndex.map(tup => (tup._1 -> tup._2)).toMap

    // Features is the list of column names excluding target
    val features = colNames.map(_._1)

    lines.zipWithIndex.filter(elt => elt._2 != 0).map(elt => elt._1).map(line => {
      // The row with all features
      val fullRow = line.split(SEPARATOR)
      // Extract the feature from this row
      val featVals = features.map(feature => {
        val idx = colNames(feature)
        fullRow(idx).toDouble
      })
      Vectors.dense(featVals.toList.toArray)
    })

  }

}
