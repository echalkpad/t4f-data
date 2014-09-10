package io.aos.spark.mllib.lasso

import scala.Array.canBuildFrom
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.regression.LassoWithSGD
import org.apache.spark.rdd.RDD
import io.aos.spark.mllib.util.CsvUtil

object GlobalScalingContext {
  val sparkConf = new SparkConf().setAppName("Lasso")
  implicit val sc = new SparkContext(sparkConf)
}

object Lasso {

  import GlobalScalingContext._

  def predictionError(predictions: Seq[Double], input: Seq[LabeledPoint]) = {
    predictions.zip(input).map {
      case (prediction, expected) =>
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
    val data = CsvUtil.readLabeledPoints(datafile, targetFeature, featlist)

    println("##########################################################")
    println("Features list=" + featlist)
    println("Data Count=" + data.count)
    println("##########################################################")

    val lassoReg = new LassoWithSGD()
    lassoReg.optimizer.setNumIterations(iterations)
      .setRegParam(reg)
      .setStepSize(1.0)
    var lassoModel = lassoReg.setIntercept(true).run(data)

    //    for( i <- 1 to 10) {
    //      lassoModel = lassoReg.run(data, lassoModel.weights)
    //      println("================= Model Weights=" + lassoModel.weights)
    //    }

    // Compute raw scores on the test set. 
    val scoreAndLabels = data.map { point =>
      val score = lassoModel.predict(point.features)
      // println(score + "\t" + point.label)
      (score, point.label)
    }

    // Get evaluation metrics.
    val metrics = new BinaryClassificationMetrics(scoreAndLabels)
    val auROC = metrics.areaUnderROC()

    println("##########################################################")
    println("Area under ROC=" + auROC)
    println("##########################################################")
    println("Intercept=" + lassoModel.intercept)
    println("##########################################################")
    println("Model Weights=" + lassoModel.weights)
    println("##########################################################")

  }

}
