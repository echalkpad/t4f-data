import breeze.linalg._
import breeze.stats.distributions._
val x = DenseMatrix.fill(10,10)(Gaussian(0,1).draw())

