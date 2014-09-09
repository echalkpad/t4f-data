package io.aos.spark.mllib.ridge

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.classification.LogisticRegressionWithSGD
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.optimization.SquaredL2Updater
import scala.Array.canBuildFrom
import org.apache.spark.mllib.regression.RidgeRegressionWithSGD
import org.apache.spark.mllib.regression.LinearRegressionWithSGD

object GlobalScalingContext {
  val sparkConf = new SparkConf().setAppName("Logistic Regression")
  implicit val sc = new SparkContext(sparkConf)
}

object CSVUtil {

  def readLabeledPoints(file: String, target: String)(implicit sc: SparkContext): RDD[LabeledPoint] = {
    val lines = sc.textFile(file)
    // extract column names -> index
    val colNames = lines.first.split(",").map(_.replaceAll("\"", ""))
      .zipWithIndex.map(tup => (tup._1 -> tup._2)).toMap
    //features is the list of column names excluding target
    val features = colNames.filter(_._1 != target).map(_._1)

    lines.zipWithIndex.filter(elt => elt._2 != 0).map(elt => elt._1).map(line => {
      // the row with all features
      val fullRow = line.split(",")
      // extract the feature from this row
      val featVals = features.map(feature => {
        val idx = colNames(feature)
        fullRow(idx).toDouble
      })
      LabeledPoint(fullRow(colNames(target)).toDouble, Vectors.dense(1.0 +: featVals.toList.toArray))
    })
  }

  def readLabeledPoints(file: String, target: String, features: List[String])(implicit sc: SparkContext): RDD[LabeledPoint] = {
    val lines = sc.textFile(file)

    // Extract column names -> index
    val colNames = lines.first.split(",").map(_.replaceAll("\"", ""))
      .zipWithIndex.map(tup => (tup._1 -> tup._2)).toMap

    //Features is the list of column names excluding target
    //val features = colNames.filter(elt => elt._1 != target && featList.contains(elt._1)).map(_._1)

    lines.zipWithIndex.filter(elt => elt._2 != 0).map(elt => elt._1).map(line => {
      // the row with all features
      val fullRow = line.split(",")
      // extract the feature from this row
      val featVals = features.map(feature => {
        val idx = colNames(feature)
        if (feature == "totfinassets_finass_log") 1.0 - fullRow(idx).toDouble else fullRow(idx).toDouble

      })
      LabeledPoint(fullRow(colNames(target)).toDouble, Vectors.dense(-1.0 +: featVals.toList.toArray))
    })
  }

}

object Ridge {

  import GlobalScalingContext._

  def predictionError(predictions: Seq[Double], input: Seq[LabeledPoint]) = {
    predictions.zip(input).map { case (prediction, expected) =>
      (prediction - expected.label) * (prediction - expected.label)
    }.reduceLeft(_ + _) / predictions.size
  }

  def main(args: Array[String]) {

    val datafile = args(0)
    val targetFeature = args(1)
    val iterations = args(2).toInt
    val reg = args(3).toDouble
    val featlist = args.drop(4).toList

    // Read a CSV file
    val data = CSVUtil.readLabeledPoints(datafile, targetFeature, featlist)

    println("##########################################################")
    println("Features list=" + featlist)
    println("Data Count=" + data.count)
    println("##########################################################")

    val ridgeReg = new RidgeRegressionWithSGD()
    ridgeReg.optimizer.setNumIterations(iterations)
                      .setRegParam(reg)
                      .setStepSize(1.0)
    val ridgeModel = ridgeReg.run(data)

    // Compute raw scores on the test set. 
    val scoreAndLabels = data.map { point =>
      val score = ridgeModel.predict(point.features)
      //println(score + "\t" + point.label)
      (score, point.label)
    }

    // Get evaluation metrics.
    val metrics = new BinaryClassificationMetrics(scoreAndLabels)
    val auROC = metrics.areaUnderROC()

    println("##########################################################")
    println("Area under ROC=" + auROC)
    println("##########################################################")
    println("Intercept=" + ridgeModel.intercept)
    println("##########################################################")
    println("Model Weights=" + ridgeModel.weights)
    println("##########################################################")

  }

}
