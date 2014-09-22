package io.aos.parser.ebnf.spl.semantic

import io.aos.parser.ebnf.spl.ast.TypedField
import io.aos.parser.ebnf.spl.ast.DataType

import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit.Test
import org.junit.Before

class DataSourceMetadataTest extends JUnitSuite with ShouldMatchersForJUnit {
  
  @Before def setup() {
    DataSourceMetadata.registerUpdater(OfflineDataSourceMetadata.load)
  }

  @Test def testNonNestedTypeLookup {
    val f = DataSourceMetadata.typeAnnotateField("customer2-visitors", "visitorid")

    f should equal(TypedField("visitorid", DataType.StringType))
  }

  @Test def testNestedTypeLookup {
    val f = DataSourceMetadata.typeAnnotateField("customer2-visitors", "urls.URL")

    f should equal(TypedField("urls.URL", DataType.StringType, Some("urls")))
  }

  @Test def testLongTypeLookup {
    val f = DataSourceMetadata.typeAnnotateField("customer2-visitors", "conversionvisits.visitnum")

    f should equal(TypedField("conversionvisits.visitnum", DataType.LongType, Some("conversionvisits")))
  }

  @Test def testDoubleTypeLookup {
    val f = DataSourceMetadata.typeAnnotateField("customer2-visitors", "conversionvisits.conversionvalue")

    f should equal(TypedField("conversionvisits.conversionvalue", DataType.DoubleType, Some("conversionvisits")))
  }

  @Test def testDateTypeLookup {
    val f = DataSourceMetadata.typeAnnotateField("customer2-visitors", "conversionvisits.timestamp")

    f should equal(TypedField("conversionvisits.timestamp", DataType.DateType, Some("conversionvisits")))
  }

  @Test def testBooleanTypeLookup {
    val f = DataSourceMetadata.typeAnnotateField("customer2-visitors", "conversionflag")

    f should equal(TypedField("conversionflag", DataType.BooleanType))
  }

  @Test def testMixedCaseFieldNameLookup {
    val f = DataSourceMetadata.typeAnnotateField("customer2-visitors", "urls.URL")

    f should equal(TypedField("urls.URL", DataType.StringType, Some("urls")))
  }

  @Test def testMixedCaseDataSourceLookup {
    val f = DataSourceMetadata.typeAnnotateField("customer2-visitors", "conversionvisits.conversionvalue")

    f should equal(TypedField("conversionvisits.conversionvalue", DataType.DoubleType, Some("conversionvisits")))
  }

  @Test(expected = classOf[AnnotationException]) def testPartialNameLookup {
    val f = DataSourceMetadata.typeAnnotateField("customer2-visitors", "urls")
  }

  @Test(expected = classOf[AnnotationException]) def testLookupFromUnknownDataSource {
    val f = DataSourceMetadata.typeAnnotateField("customer2-visitors", "visitorid")
  }

  @Test def testDataSourceExistsForExistingDataSource {
    val exists = DataSourceMetadata.dataSourceExists("customer2-visitors")

    exists should be(true)
  }

  @Test def testDataSourceExistsForNonExistingDataSource {
    val exists = DataSourceMetadata.dataSourceExists("customer2-visitors")

    exists should be(false)
  }

  @Test def testDataSourceExistsWithMixedCase {
    val exists = DataSourceMetadata.dataSourceExists("customer2-visiTORs")

    exists should be(true)
  }

}