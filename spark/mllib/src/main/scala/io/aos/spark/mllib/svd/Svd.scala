package io.aos.spark.mllib.svd

import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.mllib.linalg._
import org.apache.spark.{ SparkConf, SparkContext }

object GlobalScalingContext {
  val sparkConf = new SparkConf().setAppName("PCA").setMaster("local");
  implicit val sc = new SparkContext(sparkConf)
}

/**
 * Input tsv with 3 fields: rowIndex(Long), columnIndex(Long), weight(Double), indices start with 0.
 * 
 * Assume the number of rows is larger than the number of columns, and the number of columns is
 * smaller than Int.MaxValue
 * 
 */
object Svd {

  import GlobalScalingContext._

  def main(args: Array[String]) {

    val inputData = sc.textFile("hdfs://...").map { line =>
      val parts = line.split("\t")
      (parts(0).toLong, parts(1).toInt, parts(2).toDouble)
    }

    // Number of columns
    val nCol = inputData.map(_._2).distinct().count().toInt

    // Construct rows of the RowMatrix
    val dataRows = inputData.groupBy(_._1).map[(Long, Vector)] { row =>
      val (indices, values) = row._2.map(e => (e._2, e._3)).unzip
      (row._1, new SparseVector(nCol, indices.toArray, values.toArray))
    }

    // Compute 20 largest singular values and corresponding singular vectors
    val svd = new RowMatrix(dataRows.map(_._2).persist()).computeSVD(20, computeU = true)

    // Write results to hdfs
    val V = svd.V.toArray.grouped(svd.V.numRows).toList.transpose
    sc.makeRDD(V, 1).zipWithIndex()
      .map(line => line._2 + "\t" + line._1.mkString("\t")) // make tsv line starting with column index
      .saveAsTextFile("hdfs://...output/right_singular_vectors")

    svd.U.rows.map(row => row.toArray).zip(dataRows.map(_._1))
      .map(line => line._2 + "\t" + line._1.mkString("\t")) // make tsv line starting with row index
      .saveAsTextFile("hdfs://...output/left_singular_vectors")

    sc.makeRDD(svd.s.toArray, 1)
      .saveAsTextFile("hdfs://...output/singular_values")

  }
  
  
}
