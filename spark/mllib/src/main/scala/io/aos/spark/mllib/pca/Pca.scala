package io.aos.spark.mllib.pca

import scala.Array.canBuildFrom
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.mllib.optimization.SquaredL2Updater
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.Matrix
import io.aos.spark.mllib.util.CsvUtil
import org.apache.spark.mllib.linalg.Matrices

object GlobalScalingContext {
  val sparkConf = new SparkConf().setAppName("PCA").setMaster("local");
  implicit val sc = new SparkContext(sparkConf)
}

object Pca {

  import GlobalScalingContext._

  def main(args: Array[String]) {

    val datafile = "/dataset/donut/donut-2.csv"

    val rows = CsvUtil.readMatrix(datafile)

    val matrix = new RowMatrix(rows)
    
    println("Matrix number of rows=" + matrix.numRows)
    println("Matrix number of columns=" + matrix.numCols)

    // Compute principal components.
    val pc = matrix.computePrincipalComponents(matrix.numCols().toInt)

    println("##########################################################")
    println("Principal components number of rows=" + pc.numRows)
    println("Principal components number of columns=" + pc.numCols)
    println("Principal components are:\n" + pc)
    println("##########################################################")

  }

}
