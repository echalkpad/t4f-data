package io.aos.spark.mllib.logreg

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
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import io.aos.spark.mllib.util.CsvUtil

object GlobalScalingContext {
  val sparkConf = new SparkConf().setAppName("Logistic Regression")
  implicit val sc = new SparkContext(sparkConf)
}

object LogisticRegression {

  import GlobalScalingContext._

  def main(args: Array[String]) {

    val datafile = args(0)
    val targetFeature = args(1)
    val iterations = args(2).toInt
    val reg = args(3).toDouble
    val featlist = args.drop(4).toList

    // Read a CSV file
    val data = CsvUtil.readLabeledPoints(datafile, targetFeature, featlist)

    println("##########################################################")
    println("Features list=" + featlist)
    println("Data Count=" + data.count)
    println("##########################################################")

    // Run training algorithm to build the model
    val logistiReg = new LogisticRegressionWithSGD()

    logistiReg.optimizer
      .setNumIterations(iterations)
      .setRegParam(reg)
      .setMiniBatchFraction(.1)
      .setUpdater(new SquaredL2Updater())

    var logistModel = logistiReg.setIntercept(true).run(data)

    //    for( i <- 1 to 10) {
    //      logistModel = logistiReg.setIntercept(true).run(data, logistModel.weights)
    //      println("================= Model Weights=" + logistModel.weights)
    //    }

    // Clear the default threshold.
    logistModel.clearThreshold()

    // model.setThreshold(0.5)
    // Compute raw scores on the test set. 
    val scoreAndLabels = data.map { point =>
      val score = logistModel.predict(point.features)
      //println(score + "\t" + point.label)
      (score, point.label)
    }

    // Get evaluation metrics.
    val metrics = new BinaryClassificationMetrics(scoreAndLabels)
    val auROC = metrics.areaUnderROC()

    println("##########################################################")
    println("Area under ROC=" + auROC)
    println("##########################################################")
    println("Intercept=" + logistModel.intercept)
    println("##########################################################")
    println("Model Weights=" + logistModel.weights)
    println("##########################################################")

  }

}
