package com.zinnia.ml.spark.clustering

import org.apache.spark.SparkContext
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.Vectors

/**
 * CustomKmeans class which has the methods to some of the key operations involved in K-means algorithm like
 * running K-means algorithm on given data set, pre processing of data set, finding the centroids etc..
 */
class Kmeans {
  /**
   *
   * @param inputRawRDD raw input which is fed from the source
   * @return processed data set which to be used for running k-means
   */
  def getInputDataSet(inputRawRDD: RDD[String]): RDD[Array[Double]]={
     val inputRDD =  inputRawRDD.map(line=>  {
     val inputPoints= line.split(' ').map(ele=>if(ele!=" "&&ele!="")ele.toDouble else 0)
     inputPoints
     })
    inputRDD
  }

  /**
   *
   * @param inputRDD - Input data set which will be used to run K-means
   * @param noOfCluster - Number of clusters which will be set for the data set
   * @return - Returns the cost of the algorithm
   */
  def runKmeans(inputRDD :RDD[Array[Double]],noOfCluster:Int) :Double= {
    val customKmeans = new KMeans()
    customKmeans.setK(noOfCluster).setEpsilon(0.001)
    
    //TODO(ECH)
    val data = new SparkContext("local", "test").parallelize(Array(
      Vectors.dense(1.0, 2.0, 6.0),
      Vectors.dense(1.0, 3.0, 0.0),
      Vectors.dense(1.0, 4.0, 6.0)
    ))
    
    val model = customKmeans.run(data)
//    val model = customKmeans.run(inputRDD)
    val clusterCenters = model.clusterCenters.map(line=>{
      line.map(l=>l )})
    println("centroids are")
    clusterCenters.map(line=>{
        println(line.mkString("\t"))
    })
    println("cost is "+model.computeCost(inputRDD))
    model.computeCost(inputRDD)
  }

  /**
   *
   * @param centers  - Centroids for that cluster
   * @param points - Input data set
   * @return  - returns the RDD which which has the centroids mapped to each data set
   */
  def mapCentroidForEachCluster(centers: Array[Array[Double]], points: RDD[Array[Double]]):  RDD[String]= {
     var index = 0
     val regeneratedPixelRDD = points.map(point =>{
    var bestDistance = Double.PositiveInfinity
    for (i <- 0 until centers.length) {
      val distance = MLUtils.squaredDistance(point, centers(i))
      if (distance < bestDistance) {
        bestDistance = distance
        index = i
      }
    }
     centers(index).mkString("\t")
     } )
  regeneratedPixelRDD
  }

  /**
   * @param inputRDD - Input data set which will be used to run K-means
   * @param noOfCluster - Number of clusters which will be set for the data set
   * @return - returns all the centroids for data set
   */
  def getCentroids(inputRDD :RDD[Array[Double]],noOfCluster:Int) :Array[Array[Double]]= {
    val customKmeans = new KMeans()
    customKmeans.setK(noOfCluster).setEpsilon(0.001)
    val model = customKmeans.run(inputRDD)
   model.clusterCenters
  }
}
