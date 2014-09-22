package io.aos.parser.ebnf.spl.cache

import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.junit.JUnitSuite
import org.junit.Test
import io.aos.parser.ebnf.spl.parser.SplParser
import io.aos.parser.ebnf.spl.ast.RelativeHistogramQuery
import io.aos.parser.ebnf.spl.ast.UntypedField
import io.aos.parser.ebnf.spl.ast.WhereClause
import io.aos.parser.ebnf.spl.ast.Condition
import io.aos.parser.ebnf.spl.ast.Operator
import io.aos.parser.ebnf.spl.ast.DataSource
import io.aos.parser.ebnf.spl.driver.es.ElasticSearchCodeGen
import org.skyscreamer.jsonassert.JSONAssert
import io.aos.parser.ebnf.spl.Spl
import io.aos.parser.ebnf.spl.ast.DayOfMonth
import io.aos.parser.ebnf.spl.ast.ComparisonOperator
import org.junit.Before
import io.aos.parser.ebnf.spl.semantic.DataSourceMetadata
import io.aos.parser.ebnf.spl.semantic.OfflineDataSourceMetadata

class CacheTest extends JUnitSuite with ShouldMatchersForJUnit {
  @Before def setup() {
    DataSourceMetadata.registerUpdater(OfflineDataSourceMetadata.load)
  }

  @Test def parserCacheTest() {
    val ast1 = SplParser.buildAbstractSyntaxTree("""histogram /*+ "key_field_script":"doc['f'].value / 2" */ of urls.url by day of month of timestamp where conversionflag = true using stylistpick.visitors""")
    val ast2 = SplParser.buildAbstractSyntaxTree("""histogram /*+ "key_field_script":"doc['f'].value / 2" */ of urls.url by day of month of timestamp where conversionflag = true using stylistpick.visitors""")

    ast1.isRight should be(true)
    ast2.isRight should be(true)
    (ast1.right.get eq ast2.right.get) should be(true)

    ast1.right.get should equal(
      RelativeHistogramQuery(
        Some(""""key_field_script":"doc['f'].value / 2""""),
        UntypedField("urls.url"),
        DayOfMonth(),
        UntypedField("timestamp"),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))

    ast2.right.get should equal(
      RelativeHistogramQuery(
        Some(""""key_field_script":"doc['f'].value / 2""""),
        UntypedField("urls.url"),
        DayOfMonth(),
        UntypedField("timestamp"),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }

  @Test def codegenCacheTest() {
    val ast1 = Spl.parse("WHERE conversionflag = true AND (conversionvisits.visitnum = 1 OR NOT (lastreferrer = 'google')) USING customer2.visitors")
    val ast2 = Spl.parse("WHERE conversionflag = true AND (conversionvisits.visitnum = 1 OR NOT (lastreferrer = 'bing')) USING customer2.visitors")

    var json1: String = null
    var json2: String = null
    var json3: String = null

    ElasticSearchCodeGen.generate(ast1.right.get)(esq => { json1 = esq.asString; None; })
    ElasticSearchCodeGen.generate(ast2.right.get)(esq => { json2 = esq.asString; None; })
    ElasticSearchCodeGen.generate(ast1.right.get)(esq => { json3 = esq.asString; None; })

    JSONAssert.assertEquals("""{"size":10000,"filter":{"and":{"filters":[{"term":{"conversionflag":true}},{"or":{"filters":[{"term":{"conversionvisits.visitnum":1}},{"not":{"filter":{"term":{"lastreferrer":"google"}}}}]}}]}}}""", json1, true)
    JSONAssert.assertEquals("""{"size":10000,"filter":{"and":{"filters":[{"term":{"conversionflag":true}},{"or":{"filters":[{"term":{"conversionvisits.visitnum":1}},{"not":{"filter":{"term":{"lastreferrer":"bing"}}}}]}}]}}}""", json2, true)
    json1 should equal(json3)
  }

}