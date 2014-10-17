/**
 * Created by manuel on 15/10/14.
 */

import io.datalayer.controlchart._
import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers

class ControlChartTest extends FunSuite with ShouldMatchers {
  test("ControlChart should give us 1 outlier") {
    val testArray = Array[Float](5, 5, 5, 5, 5, 5, 5, 5, 9)
    val cc = new ControlChart(testArray)
    cc.summary()
    assert( cc.outliers.length === 1)
  }
}

class ReadCSVTest extends FunSuite with ShouldMatchers {
  test("ReadCSV should read csv containing 101 lines") {
    val testFile = new ReadCSV("src/test/resources/test.csv")
    assert(testFile.getColumn(0).length === 101)
  }
}

