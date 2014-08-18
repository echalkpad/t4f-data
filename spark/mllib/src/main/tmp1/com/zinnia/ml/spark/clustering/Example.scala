package com.zinnia.ml.spark.clustering

import org.apache.spark.SparkContext

object ex7_Data2 {

  def main(args: Array[String]) {
    val sparkContext = new SparkContext("local", "kmeans")
    val customKmeans = new Kmeans()
    val inputFileex7 = "src/main/resources/ex7_k_means_clustering/ex7data2.txt"
    val inputRawRDDex7 = sparkContext.textFile(inputFileex7)
    val inputRDDex7 = customKmeans.getInputDataSet(inputRawRDDex7)
    val noOfCluster1 = 3
    val retrievedPoints = customKmeans.runKmeans(inputRDDex7, noOfCluster1)
  }
  
}

object ex7_Bird_small {

  def main(args: Array[String]) {
    val sparkContext = new SparkContext("local", "kmeans")
    val customKmeans = new Kmeans()
    val inputFile = "src/main/resources/ex7_k_means_clustering/bird_data.txt"
    val inputRawRDD = sparkContext.textFile(inputFile)
    val inputRDD = customKmeans.getInputDataSet(inputRawRDD)
    val noOfCluster = 16
    val centroids = customKmeans.getCentroids(inputRDD, noOfCluster)
    val retrievedPoints = customKmeans.mapCentroidForEachCluster(centroids, inputRDD)
    retrievedPoints.saveAsTextFile("src/main/resources/compressedData.txt")
  }

}
