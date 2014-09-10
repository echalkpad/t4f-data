/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.mllib.linalg.distributed

import org.scalatest.FunSuite

import breeze.linalg.{DenseVector => BDV, DenseMatrix => BDM, norm => brzNorm, svd => brzSvd}

import org.apache.spark.mllib.util.LocalSparkContext
import org.apache.spark.mllib.linalg.{Matrices, Vectors, Vector}

class RowMatrixSuite extends FunSuite with LocalSparkContext {

  val m = 4
  val n = 3
  val arr = Array(0.0, 3.0, 6.0, 9.0, 1.0, 4.0, 7.0, 0.0, 2.0, 5.0, 8.0, 1.0)
  val denseData = Seq(
    Vectors.dense(0.0, 1.0, 2.0),
    Vectors.dense(3.0, 4.0, 5.0),
    Vectors.dense(6.0, 7.0, 8.0),
    Vectors.dense(9.0, 0.0, 1.0)
  )
  val sparseData = Seq(
    Vectors.sparse(3, Seq((1, 1.0), (2, 2.0))),
    Vectors.sparse(3, Seq((0, 3.0), (1, 4.0), (2, 5.0))),
    Vectors.sparse(3, Seq((0, 6.0), (1, 7.0), (2, 8.0))),
    Vectors.sparse(3, Seq((0, 9.0), (2, 1.0)))
  )

  val principalComponents = BDM(
    (0.0, 1.0, 0.0),
    (math.sqrt(2.0) / 2.0, 0.0, math.sqrt(2.0) / 2.0),
    (math.sqrt(2.0) / 2.0, 0.0, - math.sqrt(2.0) / 2.0))

  var denseMat: RowMatrix = _
  var sparseMat: RowMatrix = _

  override def beforeAll() {
    super.beforeAll()
    denseMat = new RowMatrix(sc.parallelize(denseData, 2))
    sparseMat = new RowMatrix(sc.parallelize(sparseData, 2))
  }

  def closeToZero(G: BDM[Double]): Boolean = {
    G.valuesIterator.map(math.abs).sum < 1e-6
  }

  def closeToZero(v: BDV[Double]): Boolean = {
    brzNorm(v, 1.0) < 1e-6
  }

  def assertColumnEqualUpToSign(A: BDM[Double], B: BDM[Double], k: Int) {
    assert(A.rows === B.rows)
    for (j <- 0 until k) {
      val aj = A(::, j)
      val bj = B(::, j)
      assert(closeToZero(aj - bj) || closeToZero(aj + bj),
        s"The $j-th columns mismatch: $aj and $bj")
    }
  }

  test("pca") {
    for (mat <- Seq(denseMat, sparseMat); k <- 1 to n) {
      val pc = denseMat.computePrincipalComponents(k)
      assert(pc.numRows === n)
      assert(pc.numCols === k)
      assertColumnEqualUpToSign(pc.toBreeze.asInstanceOf[BDM[Double]], principalComponents, k)
    }
  }


}
