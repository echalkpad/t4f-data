package io.aos.ebnf.spl.driver.es

import io.aos.ebnf.spl.driver.es.ElasticSearchCodeGen;
import io.aos.ebnf.spl.Spl

import org.elasticsearch.search.builder.SearchSourceBuilder
import org.skyscreamer.jsonassert.JSONAssert
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit.Test

import io.aos.ebnf.spl.driver.QueryType

import org.junit.Before

import io.aos.ebnf.spl.semantic.DataSourceMetadata
import io.aos.ebnf.spl.semantic.OfflineDataSourceMetadata

class ElasticSearchCodeGenTest extends JUnitSuite with ShouldMatchersForJUnit {

  @Before def setup() {
    DataSourceMetadata.registerUpdater(OfflineDataSourceMetadata.load)
  }

  /* ####################  Select Queries #################### */
  @Test def selectQueryWithoutFieldList() {
    val ast = Spl.parse("WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":10000,"filter":{"term":{"conversionflag":true}}}""", json, true)
  }

  @Test def selectQueryWithFieldList() {
    val ast = Spl.parse("SELECT urls.URL, recordts WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":10000,"filter":{"term":{"conversionflag":true}},"fields":["urls.URL","recordts"]}""", json, true)
  }

  @Test def selectQueryWithNestedFieldInWhere() {
    val ast = Spl.parse("SELECT urls.URL, recordts WHERE urls.URL='customer2' USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":10000,"filter":{"term":{"urls.URL":"customer2"}},"fields":["urls.URL","recordts"]}""", json, true)
  }

  @Test def selectQueryWithComplexWhereClause() {
    val ast = Spl.parse("SELECT urls.URL, recordts WHERE conversionflag = true AND (conversionvisits.visitnum = 1 OR NOT (lastreferrer = 'google')) USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":10000,"filter":{"and":{"filters":[{"term":{"conversionflag":true}},{"or":{"filters":[{"term":{"conversionvisits.visitnum":1}},{"not":{"filter":{"term":{"lastreferrer":"google"}}}}]}}]}},"fields":["urls.URL","recordts"]}""", json, true)
  }

  @Test def selectQueryWithDateInWhereClause() {
    val ast = Spl.parse("SELECT urls.URL, recordts WHERE conversionflag = true AND (conversionvisits.timestamp < '2012-07-01' OR NOT (lastreferrer = 'google')) USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":10000,"filter":{"and":{"filters":[{"term":{"conversionflag":true}},{"or":{"filters":[{"range":{"conversionvisits.timestamp":{"from":null,"to":1341100800000,"include_lower":true,"include_upper":false}}},{"not":{"filter":{"term":{"lastreferrer":"google"}}}}]}}]}},"fields":["urls.URL","recordts"]}""", json, true)
  }

  @Test def selectQueryWithDifferentOperatorsInWhereClause() {
    val ast = Spl.parse("WHERE conversionflag != true AND (conversionvisits.timestamp >= '2012-07-01' OR (lastreferrer = 'google')) USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":10000,"filter":{"and":{"filters":[{"not":{"filter":{"term":{"conversionflag":true}}}},{"or":{"filters":[{"range":{"conversionvisits.timestamp":{"from":1341100800000,"to":null,"include_lower":true,"include_upper":true}}},{"term":{"lastreferrer":"google"}}]}}]}}}""", json, true)
  }

  @Test def selectQueryWithDifferentOperatorsInWhereClause2() {
    val ast = Spl.parse("WHERE conversionflag exists AND (conversionvisits.timestamp >= '2012-07-01' OR (lastreferrer IN('google','bing','yahoo'))) USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":10000,"filter":{"and":{"filters":[{"exists":{"field":"conversionflag"}},{"or":{"filters":[{"range":{"conversionvisits.timestamp":{"from":1341100800000,"to":null,"include_lower":true,"include_upper":true}}},{"terms":{"lastreferrer":["google","bing","yahoo"],"execution":"bool"}}]}}]}}}""", json, true)
  }

  @Test def selectQueryWithDifferentOperatorsInWhereClause3() {
    val ast = Spl.parse("WHERE conversionflag not exists AND (conversionvisits.timestamp >= '2012-07-01' OR (lastreferrer not IN('google','bing','yahoo'))) USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":10000,"filter":{"and":{"filters":[{"missing":{"field":"conversionflag"}},{"or":{"filters":[{"range":{"conversionvisits.timestamp":{"from":1341100800000,"to":null,"include_lower":true,"include_upper":true}}},{"not":{"filter":{"terms":{"lastreferrer":["google","bing","yahoo"],"execution":"bool"}}}}]}}]}}}""", json, true)
  }

  /* ####################  TopN Queries #################### */
  @Test def topNQueryWithoutWhereClauseOrOrdering() {
    val ast = Spl.parse("TOP 10 urls.URL USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"urls.URL":{"terms":{"field":"urls.URL","size":10,"order":"count"}}}}""", json, true)
  }

  @Test def topNQueryWithWhereClauseNoOrdering() {
    val ast = Spl.parse("TOP 10 maxvisitnum WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"maxvisitnum":{"terms":{"field":"maxvisitnum","size":10,"order":"count"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  @Test def topNQueryWithNestedDimension() {
    val ast = Spl.parse("TOP 10 urls.URL WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"urls.URL":{"terms":{"field":"urls.URL","size":10,"order":"count"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  @Test def topNQueryWithNestedDimensionAndNestedWhere() {
    val ast = Spl.parse("TOP 10 urls.URL WHERE conversionflag = true AND (conversionvisits.visitnum = 1 OR NOT (lastreferrer = 'google')) USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"urls.URL":{"terms":{"field":"urls.URL","size":10,"order":"count"},"facet_filter":{"and":{"filters":[{"term":{"conversionflag":true}},{"or":{"filters":[{"term":{"conversionvisits.visitnum":1}},{"not":{"filter":{"term":{"lastreferrer":"google"}}}}]}}]}}}}}""", json, true)
  }

  @Test def topNQueryWithWhereClauseAndOrdering() {
    val ast = Spl.parse("TOP 10 urls.URL BY TERM WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"urls.URL":{"terms":{"field":"urls.URL","size":10,"order":"term"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  @Test def topNQueryWithNonNestedField() {
    val ast = Spl.parse("TOP 10 totallifetimeconversionvalue BY COUNT WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"totallifetimeconversionvalue":{"terms":{"field":"totallifetimeconversionvalue","size":10,"order":"count"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  /*
  @Test def topNQueryOnNumericFieldWithWhereClauseAndOrdering() {
    val ast = Spl.parse("TOP 10 maxvisitnum BY MEAN WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"maxvisitnum":{"terms_stats":{"key_field":"maxvisitnum","value_field":"maxvisitnum","order":"mean","size":10},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }
  */

  /* ####################  BottomN Queries #################### */
  @Test def bottomNQueryWithoutWhereClauseOrOrdering() {
    val ast = Spl.parse("BOTTOM 10 urls.URL USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"urls.URL":{"terms":{"field":"urls.URL","size":10,"order":"reverse_count"}}}}""", json, true)
  }

  @Test def bottomNQueryWithNestedDimension() {
    val ast = Spl.parse("BOTTOM 10 urls.URL WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"urls.URL":{"terms":{"field":"urls.URL","size":10,"order":"reverse_count"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  @Test def bottomNQueryWithWhereClauseNoOrdering() {
    val ast = Spl.parse("BOTTOM 10 maxvisitnum WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"maxvisitnum":{"terms":{"field":"maxvisitnum","size":10,"order":"reverse_count"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  @Test def bottomNQueryWithWhereClauseAndOrdering() {
    val ast = Spl.parse("BOTTOM 10 urls.URL BY TERM WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"urls.URL":{"terms":{"field":"urls.URL","size":10,"order":"reverse_term"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  /*
  @Test def bottomNQueryOnNumericFieldWithWhereClauseAndOrdering() {
    val ast = Spl.parse("BOTTOM 10 maxvisitnum BY MEAN WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"maxvisitnum":{"terms_stats":{"key_field":"maxvisitnum","value_field":"maxvisitnum","order":"reverse_mean","size":10},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }*/

  /* ####################  Range Queries #################### */
  @Test def rangeQueryWithoutWhereClauseAndSecondDimension() {
    val ast = Spl.parse("RANGE(..10, 15..30, 30) OVER totallifetimeconversionvalue USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"totallifetimeconversionvalue":{"range":{"field":"totallifetimeconversionvalue","ranges":[{"to":10.0},{"from":15.0,"to":30.0},{"from":30.0}]}}}}""", json, true)
  }

  @Test def rangeQueryWithoutWhereClause() {
    val ast = Spl.parse("RANGE(..10, 15..30, 30) OVER totallifetimeconversionvalue OF maxvisitnum USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"totallifetimeconversionvalue":{"range":{"key_field":"totallifetimeconversionvalue","value_field":"maxvisitnum","ranges":[{"to":10.0},{"from":15.0,"to":30.0},{"from":30.0}]}}}}""", json, true)
  }

  @Test def rangeQueryWithWhereClause() {
    val ast = Spl.parse("RANGE(..10, 15..30, 30) OVER totallifetimeconversionvalue OF maxvisitnum WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"totallifetimeconversionvalue":{"range":{"key_field":"totallifetimeconversionvalue","value_field":"maxvisitnum","ranges":[{"to":10.0},{"from":15.0,"to":30.0},{"from":30.0}]},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  @Test def rangeQueryOnNestedField() {
    val ast = Spl.parse("RANGE(..10, 15..30, 30) OVER urls.pagenum OF maxvisitnum WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"query":{"term":{"conversionflag":true}},"facets":{"urls.pagenum":{"range":{"key_field":"urls.pagenum","value_field":"maxvisitnum","ranges":[{"to":10.0},{"from":15.0,"to":30.0},{"from":30.0}]}}}}""", json, true)
  }

  @Test def rangeQueryWithWhereClauseNoSecondDimension() {
    val ast = Spl.parse("RANGE(..10, 15..30, 30) OVER totallifetimeconversionvalue WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"totallifetimeconversionvalue":{"range":{"field":"totallifetimeconversionvalue","ranges":[{"to":10.0},{"from":15.0,"to":30.0},{"from":30.0}]},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  /* ####################  Absolute Histogram Queries #################### */
  @Test def histogramQueryWithoutWhereClauseOrSecondDimension() {
    val ast = Spl.parse("HISTOGRAM OF numconvertingvisits INTERVAL 1 USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"numconvertingvisits":{"histogram":{"key_field":"numconvertingvisits","value_field":"numconvertingvisits","interval":1}}}}""", json, true)
  }

  @Test def histogramQueryWithWhereClauseNoSecondDimension() {
    val ast = Spl.parse("HISTOGRAM OF numconvertingvisits INTERVAL 1 WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"numconvertingvisits":{"histogram":{"key_field":"numconvertingvisits","value_field":"numconvertingvisits","interval":1},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  @Test def histogramQueryWithWhereClauseAndSecondDimension() {
    val ast = Spl.parse("HISTOGRAM OF numconvertingvisits OVER totalpagenum INTERVAL 1 WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"numconvertingvisits":{"histogram":{"key_field":"totalpagenum","value_field":"numconvertingvisits","interval":1},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  @Test def histogramQueryWithSingleDateDimension() {
    val ast = Spl.parse("HISTOGRAM OF pingtimeunix INTERVAL 1 DAY USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"pingtimeunix":{"date_histogram":{"key_field":"pingtimeunix","value_field":"pingtimeunix","interval":"day"}}}}""", json, true)
  }

  @Test def histogramQueryWithDateDimension() {
    val ast = Spl.parse("HISTOGRAM OF totalpagenum OVER pingtimeunix INTERVAL 7 DAYS USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"totalpagenum":{"date_histogram":{"key_field":"pingtimeunix","value_field":"totalpagenum","interval":"7.0d"}}}}""", json, true)
  }

  @Test def histogramQueryOnNestedField() {
    val ast = Spl.parse("HISTOGRAM OF numconvertingvisits OVER urls.pagenum INTERVAL 1 WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"query":{"term":{"conversionflag":true}},"facets":{"numconvertingvisits":{"histogram":{"key_field":"urls.pagenum","value_field":"numconvertingvisits","interval":1}}}}""", json, true)
  }

  @Test def histogramQueryVisitorParam() {
    val ast = Spl.parse("HISTOGRAM /*+__visitors__*/ OF pingtimeunix INTERVAL 7 DAYS USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"pingtimeunix":{"distinct_date_histogram":{"key_field":"pingtimeunix","value_field":"visitorid","interval":"7.0d"}}}}""", json, true)
  }

  @Test def histogramQueryVisitorFilter() {
    val ast = Spl.parse("HISTOGRAM /*+__visitors__*/ OF pingtimeunix INTERVAL 7 DAYS WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"pingtimeunix":{"distinct_date_histogram":{"key_field":"pingtimeunix","value_field":"visitorid","interval":"7.0d"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }


  /* ####################  Relative Histogram Queries #################### */
  @Test def relativeHistogramQueryWithoutWhereClause() {
    val ast = Spl.parse("HISTOGRAM OF numconvertingvisits BY DAY OF WEEK OF timestamp USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"dayOfWeek":{"histogram":{"key_script":"doc['timestamp'].date.dayOfWeek","value_script":"doc['numconvertingvisits'].value"}}}}""", json, true)
  }

  @Test def relativeHistogramQueryWithWhereClause() {
    val ast = Spl.parse("HISTOGRAM OF numconvertingvisits BY DAY OF WEEK OF timestamp WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"dayOfWeek":{"histogram":{"key_script":"doc['timestamp'].date.dayOfWeek","value_script":"doc['numconvertingvisits'].value"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  @Test def relativeHistogramQueryOnDayOfMonth() {
    val ast = Spl.parse("HISTOGRAM OF numconvertingvisits BY DAY OF MONTH OF timestamp WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"dayOfMonth":{"histogram":{"key_script":"doc['timestamp'].date.dayOfMonth","value_script":"doc['numconvertingvisits'].value"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  @Test def relativeHistogramQueryOnMonthOfYear() {
    val ast = Spl.parse("HISTOGRAM OF numconvertingvisits BY MONTH OF YEAR OF timestamp WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"monthOfYear":{"histogram":{"key_script":"doc['timestamp'].date.monthOfYear","value_script":"doc['numconvertingvisits'].value"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  @Test def relativeHistogramQueryOnHourOfDay() {
    val ast = Spl.parse("HISTOGRAM OF numconvertingvisits BY HOUR OF DAY OF timestamp WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"hourOfDay":{"histogram":{"key_script":"doc['timestamp'].date.hourOfDay","value_script":"doc['numconvertingvisits'].value"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  @Test def relativeHistogramQueryOnMinuteOfDay() {
    val ast = Spl.parse("HISTOGRAM OF numconvertingvisits BY MINUTE OF DAY OF timestamp WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"minuteOfDay":{"histogram":{"key_script":"doc['timestamp'].date.minuteOfDay","value_script":"doc['numconvertingvisits'].value"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  @Test def relativeHistogramQueryOnMinuteOfHour() {
    val ast = Spl.parse("HISTOGRAM OF numconvertingvisits BY MINUTE OF HOUR OF timestamp WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"minuteOfHour":{"histogram":{"key_script":"doc['timestamp'].date.minuteOfHour","value_script":"doc['numconvertingvisits'].value"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  /* ####################  Stats Queries #################### */
  @Test def statsQueryWithoutWhereClause() {
    val ast = Spl.parse("STATS OF totalpagenum USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"stats":{"statistical":{"field":"totalpagenum"}}}}""", json, true)
  }

  @Test def statsQueryWithWhereClause() {
    val ast = Spl.parse("STATS OF totalpagenum WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"stats":{"statistical":{"field":"totalpagenum"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  @Test def statsQueryWithDashedDataSourceName() {
    val ast = Spl.parse("STATS OF totalpagenum WHERE conversionflag = true USING customer3.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"stats":{"statistical":{"field":"totalpagenum"},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  @Test def statsQueryWithMultipleFields() {
    val ast = Spl.parse("STATS OF totalpagenum,totallifetimeconversionvalue WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"stats":{"statistical":{"fields":["totalpagenum","totallifetimeconversionvalue"]},"facet_filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  /* ####################  Pivot Queries #################### */
  @Test def pivotQueryWithoutWhereClause() {
    val ast = Spl.parse("PIVOT firstsearch ON geolocation USING customer2.visitors")
    var json: String = null
    var result = ElasticSearchQueryResult(Seq("firstsearch", "geolocation"), Map.empty, Map("dimOneFacet" -> Map("ak" -> "av", "bk" -> "bv", "ck" -> "cv"), "dimTwoFacet" -> Map("dk" -> "dv", "ek" -> "ev", "fk" -> "fv")))

    ElasticSearchCodeGen.generate(ast.right.get)(esq => {
      json = esq.asString
      Some(result)
    })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"ak|dk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ak"}},{"term":{"geolocation":"dk"}}]}}},"ak|ek":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ak"}},{"term":{"geolocation":"ek"}}]}}},"ak|fk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ak"}},{"term":{"geolocation":"fk"}}]}}},"bk|dk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"bk"}},{"term":{"geolocation":"dk"}}]}}},"bk|ek":{"filter":{"and":{"filters":[{"term":{"firstsearch":"bk"}},{"term":{"geolocation":"ek"}}]}}},"bk|fk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"bk"}},{"term":{"geolocation":"fk"}}]}}},"ck|dk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ck"}},{"term":{"geolocation":"dk"}}]}}},"ck|ek":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ck"}},{"term":{"geolocation":"ek"}}]}}},"ck|fk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ck"}},{"term":{"geolocation":"fk"}}]}}}}}""", json, true)
  }

  @Test def pivotQueryWithWhereClause() {
    val ast = Spl.parse("PIVOT firstsearch ON geolocation WHERE conversionflag=true USING customer2.visitors")
    var json: String = null
    var result = ElasticSearchQueryResult(Seq("firstsearch", "geolocation"), Map.empty, Map("dimOneFacet" -> Map("ak" -> "av", "bk" -> "bv", "ck" -> "cv"), "dimTwoFacet" -> Map("dk" -> "dv", "ek" -> "ev", "fk" -> "fv")))

    ElasticSearchCodeGen.generate(ast.right.get)(esq => {
      json = esq.asString
      Some(result)
    })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"query":{"term":{"conversionflag":true}},"facets":{"ak|dk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ak"}},{"term":{"geolocation":"dk"}}]}}},"ak|ek":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ak"}},{"term":{"geolocation":"ek"}}]}}},"ak|fk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ak"}},{"term":{"geolocation":"fk"}}]}}},"bk|dk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"bk"}},{"term":{"geolocation":"dk"}}]}}},"bk|ek":{"filter":{"and":{"filters":[{"term":{"firstsearch":"bk"}},{"term":{"geolocation":"ek"}}]}}},"bk|fk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"bk"}},{"term":{"geolocation":"fk"}}]}}},"ck|dk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ck"}},{"term":{"geolocation":"dk"}}]}}},"ck|ek":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ck"}},{"term":{"geolocation":"ek"}}]}}},"ck|fk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ck"}},{"term":{"geolocation":"fk"}}]}}}}}""", json, true)
  }

  @Test def pivotQueryWithTop() {
    val ast = Spl.parse("PIVOT TOP 5 firstsearch ON geolocation WHERE conversionflag=true USING customer2.visitors")
    var json: String = null
    var result = ElasticSearchQueryResult(Seq("firstsearch", "geolocation"), Map.empty, Map("dimOneFacet" -> Map("ak" -> "av", "bk" -> "bv", "ck" -> "cv"), "dimTwoFacet" -> Map("dk" -> "dv", "ek" -> "ev", "fk" -> "fv")))

    ElasticSearchCodeGen.generate(ast.right.get)(esq => {
      json = esq.asString
      Some(result)
    })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"query":{"term":{"conversionflag":true}},"facets":{"ak|dk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ak"}},{"term":{"geolocation":"dk"}}]}}},"ak|ek":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ak"}},{"term":{"geolocation":"ek"}}]}}},"ak|fk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ak"}},{"term":{"geolocation":"fk"}}]}}},"bk|dk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"bk"}},{"term":{"geolocation":"dk"}}]}}},"bk|ek":{"filter":{"and":{"filters":[{"term":{"firstsearch":"bk"}},{"term":{"geolocation":"ek"}}]}}},"bk|fk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"bk"}},{"term":{"geolocation":"fk"}}]}}},"ck|dk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ck"}},{"term":{"geolocation":"dk"}}]}}},"ck|ek":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ck"}},{"term":{"geolocation":"ek"}}]}}},"ck|fk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ck"}},{"term":{"geolocation":"fk"}}]}}}}}""", json, true)
  }

  @Test def pivotQueryWithBottom() {
    val ast = Spl.parse("PIVOT TOP 5 firstsearch ON geolocation WHERE conversionflag=true USING customer2.visitors")
    var json: String = null
    var result = ElasticSearchQueryResult(Seq("firstsearch", "geolocation"), Map.empty, Map("dimOneFacet" -> Map("ak" -> "av", "bk" -> "bv", "ck" -> "cv"), "dimTwoFacet" -> Map("dk" -> "dv", "ek" -> "ev", "fk" -> "fv")))

    ElasticSearchCodeGen.generate(ast.right.get)(esq => {
      json = esq.asString
      Some(result)
    })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"query":{"term":{"conversionflag":true}},"facets":{"ak|dk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ak"}},{"term":{"geolocation":"dk"}}]}}},"ak|ek":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ak"}},{"term":{"geolocation":"ek"}}]}}},"ak|fk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ak"}},{"term":{"geolocation":"fk"}}]}}},"bk|dk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"bk"}},{"term":{"geolocation":"dk"}}]}}},"bk|ek":{"filter":{"and":{"filters":[{"term":{"firstsearch":"bk"}},{"term":{"geolocation":"ek"}}]}}},"bk|fk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"bk"}},{"term":{"geolocation":"fk"}}]}}},"ck|dk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ck"}},{"term":{"geolocation":"dk"}}]}}},"ck|ek":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ck"}},{"term":{"geolocation":"ek"}}]}}},"ck|fk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ck"}},{"term":{"geolocation":"fk"}}]}}}}}""", json, true)
  }

  @Test def pivotQueryWithEmptyFieldValue() {
    val ast = Spl.parse("PIVOT TOP 5 firstsearch ON geolocation WHERE conversionflag=true USING customer2.visitors")
    var json: String = null
    var result = ElasticSearchQueryResult(Seq("firstsearch", "geolocation"), Map.empty, Map("dimOneFacet" -> Map("ak" -> "av", "bk" -> "bv", "" -> "cv"), "dimTwoFacet" -> Map("dk" -> "dv", "ek" -> "ev", "fk" -> "fv")))

    ElasticSearchCodeGen.generate(ast.right.get)(esq => {
      json = esq.asString
      Some(result)
    })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"query":{"term":{"conversionflag":true}},"facets":{"ak|dk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ak"}},{"term":{"geolocation":"dk"}}]}}},"ak|ek":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ak"}},{"term":{"geolocation":"ek"}}]}}},"ak|fk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"ak"}},{"term":{"geolocation":"fk"}}]}}},"bk|dk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"bk"}},{"term":{"geolocation":"dk"}}]}}},"bk|ek":{"filter":{"and":{"filters":[{"term":{"firstsearch":"bk"}},{"term":{"geolocation":"ek"}}]}}},"bk|fk":{"filter":{"and":{"filters":[{"term":{"firstsearch":"bk"}},{"term":{"geolocation":"fk"}}]}}},"|dk":{"filter":{"and":{"filters":[{"term":{"firstsearch":""}},{"term":{"geolocation":"dk"}}]}}},"|ek":{"filter":{"and":{"filters":[{"term":{"firstsearch":""}},{"term":{"geolocation":"ek"}}]}}},"|fk":{"filter":{"and":{"filters":[{"term":{"firstsearch":""}},{"term":{"geolocation":"fk"}}]}}}}}""", json, true)
  }

  @Test def pivotQueryWithNumericDimension() {
    val ast = Spl.parse("PIVOT firstsearch ON maxvisitnum WHERE conversionflag=true USING customer2.visitors")
    var json: String = null
    var result = ElasticSearchQueryResult(Seq("firstsearch"), Map.empty, Map("dimOneFacet" -> Map("ak" -> "av", "bk" -> "bv", "ck" -> "cv")))

    ElasticSearchCodeGen.generate(ast.right.get)(esq => {
      json = esq.asString
      Some(result)
    })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"query":{"term":{"conversionflag":true}},"facets":{"ak":{"statistical":{"field":"maxvisitnum"},"facet_filter":{"term":{"firstsearch":"ak"}}},"bk":{"statistical":{"field":"maxvisitnum"},"facet_filter":{"term":{"firstsearch":"bk"}}},"ck":{"statistical":{"field":"maxvisitnum"},"facet_filter":{"term":{"firstsearch":"ck"}}}}}""", json, true)
  }

  @Test def pivotQueryWithNumericDimensionSwitched() {
    val ast = Spl.parse("PIVOT maxvisitnum ON firstsearch WHERE conversionflag=true USING customer2.visitors")
    var json: String = null
    var result = ElasticSearchQueryResult(Seq("firstsearch"), Map.empty, Map("dimOneFacet" -> Map("1" -> "1", "2" -> "2", "3" -> "3"), "dimTwoFacet" -> Map("dk" -> "dv", "ek" -> "ev", "fk" -> "fv")))

    ElasticSearchCodeGen.generate(ast.right.get)(esq => {
      json = esq.asString
      Some(result)
    })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"query":{"term":{"conversionflag":true}},"facets":{"1|dk":{"filter":{"and":{"filters":[{"term":{"maxvisitnum":1}},{"term":{"firstsearch":"dk"}}]}}},"1|ek":{"filter":{"and":{"filters":[{"term":{"maxvisitnum":1}},{"term":{"firstsearch":"ek"}}]}}},"1|fk":{"filter":{"and":{"filters":[{"term":{"maxvisitnum":1}},{"term":{"firstsearch":"fk"}}]}}},"2|dk":{"filter":{"and":{"filters":[{"term":{"maxvisitnum":2}},{"term":{"firstsearch":"dk"}}]}}},"2|ek":{"filter":{"and":{"filters":[{"term":{"maxvisitnum":2}},{"term":{"firstsearch":"ek"}}]}}},"2|fk":{"filter":{"and":{"filters":[{"term":{"maxvisitnum":2}},{"term":{"firstsearch":"fk"}}]}}},"3|dk":{"filter":{"and":{"filters":[{"term":{"maxvisitnum":3}},{"term":{"firstsearch":"dk"}}]}}},"3|ek":{"filter":{"and":{"filters":[{"term":{"maxvisitnum":3}},{"term":{"firstsearch":"ek"}}]}}},"3|fk":{"filter":{"and":{"filters":[{"term":{"maxvisitnum":3}},{"term":{"firstsearch":"fk"}}]}}}}}""", json, true)
  }

  @Test def pivotQueryWithTwoNumericDimensions() {
    val ast = Spl.parse("PIVOT maxvisitnum ON numconvertingvisits WHERE conversionflag=true USING customer2.visitors")
    var json: String = null
    var result = ElasticSearchQueryResult(Seq("maxvisitnum"), Map.empty, Map("dimOneFacet" -> Map("1" -> "1", "2" -> "2", "3" -> "3")))

    ElasticSearchCodeGen.generate(ast.right.get)(esq => {
      json = esq.asString
      Some(result)
    })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"query":{"term":{"conversionflag":true}},"facets":{"1":{"statistical":{"field":"numconvertingvisits"},"facet_filter":{"term":{"maxvisitnum":1}}},"2":{"statistical":{"field":"numconvertingvisits"},"facet_filter":{"term":{"maxvisitnum":2}}},"3":{"statistical":{"field":"numconvertingvisits"},"facet_filter":{"term":{"maxvisitnum":3}}}}}""", json, true)
  }

  @Test def pivotQueryWithNestedDimension() {
    val ast = Spl.parse("PIVOT urls.URL ON firstsearch WHERE conversionflag=true USING customer2.visitors")
    var json: String = null
    var result = ElasticSearchQueryResult(Seq("firstsearch"), Map.empty, Map("dimOneFacet" -> Map("ak" -> "av", "bk" -> "bv", "ck" -> "cv"), "dimTwoFacet" -> Map("dk" -> "dv", "ek" -> "ev", "fk" -> "fv")))

    ElasticSearchCodeGen.generate(ast.right.get)(esq => {
      json = esq.asString
      Some(result)
    })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"query":{"term":{"conversionflag":true}},"facets":{"ak|dk":{"query":{"bool":{"must":[{"term":{"urls.URL":"ak"}},{"term":{"firstsearch":"dk"}}]}}},"ak|ek":{"query":{"bool":{"must":[{"term":{"urls.URL":"ak"}},{"term":{"firstsearch":"ek"}}]}}},"ak|fk":{"query":{"bool":{"must":[{"term":{"urls.URL":"ak"}},{"term":{"firstsearch":"fk"}}]}}},"bk|dk":{"query":{"bool":{"must":[{"term":{"urls.URL":"bk"}},{"term":{"firstsearch":"dk"}}]}}},"bk|ek":{"query":{"bool":{"must":[{"term":{"urls.URL":"bk"}},{"term":{"firstsearch":"ek"}}]}}},"bk|fk":{"query":{"bool":{"must":[{"term":{"urls.URL":"bk"}},{"term":{"firstsearch":"fk"}}]}}},"ck|dk":{"query":{"bool":{"must":[{"term":{"urls.URL":"ck"}},{"term":{"firstsearch":"dk"}}]}}},"ck|ek":{"query":{"bool":{"must":[{"term":{"urls.URL":"ck"}},{"term":{"firstsearch":"ek"}}]}}},"ck|fk":{"query":{"bool":{"must":[{"term":{"urls.URL":"ck"}},{"term":{"firstsearch":"fk"}}]}}}}}""", json, true)
  }

  @Test def pivotQueryWithConversionsFlag() {
    val ast = Spl.parse("PIVOT /*+__conversions__*/ urls.URL ON firstsearch WHERE conversionflag=true USING customer2.visitors")
    var json: String = null
    var result = ElasticSearchQueryResult(Seq("firstsearch"), Map.empty, Map("dimOneFacet" -> Map("ak" -> "av", "bk" -> "bv", "ck" -> "cv"), "dimTwoFacet" -> Map("dk" -> "dv", "ek" -> "ev", "fk" -> "fv")))

    ElasticSearchCodeGen.generate(ast.right.get)(esq => {
      json = esq.asString
      Some(result)
    })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"query":{"term":{"conversionflag":true}},"facets":{"ak|dk":{"query":{"bool":{"must":[{"term":{"urls.URL":"ak"}},{"term":{"firstsearch":"dk"}}]}}},"ak|dk:__conversions__":{"query":{"bool":{"must":[{"term":{"conversionflag":true}},{"bool":{"must":[{"term":{"urls.URL":"ak"}},{"term":{"firstsearch":"dk"}}]}}]}}},"ak|ek":{"query":{"bool":{"must":[{"term":{"urls.URL":"ak"}},{"term":{"firstsearch":"ek"}}]}}},"ak|ek:__conversions__":{"query":{"bool":{"must":[{"term":{"conversionflag":true}},{"bool":{"must":[{"term":{"urls.URL":"ak"}},{"term":{"firstsearch":"ek"}}]}}]}}},"ak|fk":{"query":{"bool":{"must":[{"term":{"urls.URL":"ak"}},{"term":{"firstsearch":"fk"}}]}}},"ak|fk:__conversions__":{"query":{"bool":{"must":[{"term":{"conversionflag":true}},{"bool":{"must":[{"term":{"urls.URL":"ak"}},{"term":{"firstsearch":"fk"}}]}}]}}},"bk|dk":{"query":{"bool":{"must":[{"term":{"urls.URL":"bk"}},{"term":{"firstsearch":"dk"}}]}}},"bk|dk:__conversions__":{"query":{"bool":{"must":[{"term":{"conversionflag":true}},{"bool":{"must":[{"term":{"urls.URL":"bk"}},{"term":{"firstsearch":"dk"}}]}}]}}},"bk|ek":{"query":{"bool":{"must":[{"term":{"urls.URL":"bk"}},{"term":{"firstsearch":"ek"}}]}}},"bk|ek:__conversions__":{"query":{"bool":{"must":[{"term":{"conversionflag":true}},{"bool":{"must":[{"term":{"urls.URL":"bk"}},{"term":{"firstsearch":"ek"}}]}}]}}},"bk|fk":{"query":{"bool":{"must":[{"term":{"urls.URL":"bk"}},{"term":{"firstsearch":"fk"}}]}}},"bk|fk:__conversions__":{"query":{"bool":{"must":[{"term":{"conversionflag":true}},{"bool":{"must":[{"term":{"urls.URL":"bk"}},{"term":{"firstsearch":"fk"}}]}}]}}},"ck|dk":{"query":{"bool":{"must":[{"term":{"urls.URL":"ck"}},{"term":{"firstsearch":"dk"}}]}}},"ck|dk:__conversions__":{"query":{"bool":{"must":[{"term":{"conversionflag":true}},{"bool":{"must":[{"term":{"urls.URL":"ck"}},{"term":{"firstsearch":"dk"}}]}}]}}},"ck|ek":{"query":{"bool":{"must":[{"term":{"urls.URL":"ck"}},{"term":{"firstsearch":"ek"}}]}}},"ck|ek:__conversions__":{"query":{"bool":{"must":[{"term":{"conversionflag":true}},{"bool":{"must":[{"term":{"urls.URL":"ck"}},{"term":{"firstsearch":"ek"}}]}}]}}},"ck|fk":{"query":{"bool":{"must":[{"term":{"urls.URL":"ck"}},{"term":{"firstsearch":"fk"}}]}}},"ck|fk:__conversions__":{"query":{"bool":{"must":[{"term":{"conversionflag":true}},{"bool":{"must":[{"term":{"urls.URL":"ck"}},{"term":{"firstsearch":"fk"}}]}}]}}}}}""", json, true)
  }

  /* ####################  Count Queries #################### */
  @Test def countQueryWithoutWhereClause() {
    val ast = Spl.parse("COUNT RECORDS USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"count":{"filter":{"match_all":{}}}}}""", json, true)
  }

  @Test def countQueryWithWhereClause() {
    val ast = Spl.parse("COUNT RECORDS WHERE conversionflag = true USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"count":{"filter":{"term":{"conversionflag":true}}}}}""", json, true)
  }

  /* ####################  Bug Reports #################### */
  @Test def pivotDateDimension() {
    val ast = Spl.parse("PIVOT urls.URL ON urls.timestamp WHERE conversionflag=true USING customer2.visitors")
    var json: String = null
    var result = ElasticSearchQueryResult(Seq("firstsearch"), Map.empty, Map("dimOneFacet" -> Map("ak" -> "av", "bk" -> "bv", "ck" -> "cv"), "dimTwoFacet" -> Map("1346141148675" -> 1346141148675L, "1346189382004" -> 1346189382004L, "1352577966802" -> 1352577966802L)))

    ElasticSearchCodeGen.generate(ast.right.get)(esq => {
      json = esq.asString
      Some(result)
    })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"query":{"term":{"conversionflag":true}},"facets":{"ak|1346141148675":{"query":{"nested":{"query":{"bool":{"must":[{"term":{"urls.URL":"ak"}},{"term":{"urls.timestamp":1346141148675}}]}},"path":"urls"}}},"ak|1346189382004":{"query":{"nested":{"query":{"bool":{"must":[{"term":{"urls.URL":"ak"}},{"term":{"urls.timestamp":1346189382004}}]}},"path":"urls"}}},"ak|1352577966802":{"query":{"nested":{"query":{"bool":{"must":[{"term":{"urls.URL":"ak"}},{"term":{"urls.timestamp":1352577966802}}]}},"path":"urls"}}},"bk|1346141148675":{"query":{"nested":{"query":{"bool":{"must":[{"term":{"urls.URL":"bk"}},{"term":{"urls.timestamp":1346141148675}}]}},"path":"urls"}}},"bk|1346189382004":{"query":{"nested":{"query":{"bool":{"must":[{"term":{"urls.URL":"bk"}},{"term":{"urls.timestamp":1346189382004}}]}},"path":"urls"}}},"bk|1352577966802":{"query":{"nested":{"query":{"bool":{"must":[{"term":{"urls.URL":"bk"}},{"term":{"urls.timestamp":1352577966802}}]}},"path":"urls"}}},"ck|1346141148675":{"query":{"nested":{"query":{"bool":{"must":[{"term":{"urls.URL":"ck"}},{"term":{"urls.timestamp":1346141148675}}]}},"path":"urls"}}},"ck|1346189382004":{"query":{"nested":{"query":{"bool":{"must":[{"term":{"urls.URL":"ck"}},{"term":{"urls.timestamp":1346189382004}}]}},"path":"urls"}}},"ck|1352577966802":{"query":{"nested":{"query":{"bool":{"must":[{"term":{"urls.URL":"ck"}},{"term":{"urls.timestamp":1352577966802}}]}},"path":"urls"}}}}}""", json, true)
  }

  @Test def inClause() {
    val ast = Spl.parse("TOP 10 urls.referrercategory  WHERE urls.referrercategory IN ('email','sem') USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"urls.referrercategory":{"terms":{"field":"urls.referrercategory","size":10,"order":"count"},"facet_filter":{"terms":{"urls.referrercategory":["email","sem"],"execution":"bool"}}}}}""", json, true)
  }

  @Test def dateParsing() {
    val ast = Spl.parse("TOP 10 urls.referrercategory  WHERE urls.timestamp > '2012-12-01' USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"urls.referrercategory":{"terms":{"field":"urls.referrercategory","size":10,"order":"count"},"facet_filter":{"range":{"urls.timestamp":{"from":1354320000000,"to":null,"include_lower":false,"include_upper":true}}}}}}""", json, true)
  }

  @Test def dateAndTimeParsing() {
    val ast = Spl.parse("TOP 10 urls.referrercategory  WHERE urls.timestamp > '2012-12-12T12:12:12Z' USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"urls.referrercategory":{"terms":{"field":"urls.referrercategory","size":10,"order":"count"},"facet_filter":{"range":{"urls.timestamp":{"from":1355314332000,"to":null,"include_lower":false,"include_upper":true}}}}}}""", json, true)
  }

  @Test def dateAndTimeWithTimeZoneParsing() {
    val ast = Spl.parse("TOP 10 urls.referrercategory  WHERE urls.timestamp > '2012-12-12T12:12:12+06' USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"urls.referrercategory":{"terms":{"field":"urls.referrercategory","size":10,"order":"count"},"facet_filter":{"range":{"urls.timestamp":{"from":1355292732000,"to":null,"include_lower":false,"include_upper":true}}}}}}""", json, true)
  }

  @Test def whereClauseOnFieldsOfNestedPath() {
    val ast = Spl.parse("TOP 10 urls.referrercategory  WHERE (urls.timestamp > '2012-12-12T12:12:12+06') AND (urls.urlcategory = 'x') AND (urls.urlsubcategory = 'y') USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"urls.referrercategory":{"terms":{"field":"urls.referrercategory","size":10,"order":"count"},"facet_filter":{"query":{"nested":{"query":{"bool":{"must":[{"bool":{"must":[{"range":{"urls.timestamp":{"from":1355292732000,"to":null,"include_lower":false,"include_upper":true}}},{"term":{"urls.urlcategory":"x"}}]}},{"term":{"urls.urlsubcategory":"y"}}]}},"path":"urls"}}}}}}""", json, true)
  }

  @Test def whereClauseOnFieldsOfDifferentNestedPath() {
    val ast = Spl.parse("TOP 10 urls.referrercategory  WHERE (conversionvisits.timestamp > '2012-12-12T12:12:12+06') AND (urls.urlcategory = 'x') AND (urls.urlsubcategory = 'y') USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"urls.referrercategory":{"terms":{"field":"urls.referrercategory","size":10,"order":"count"},"facet_filter":{"and":{"filters":[{"and":{"filters":[{"range":{"conversionvisits.timestamp":{"from":1355292732000,"to":null,"include_lower":false,"include_upper":true}}},{"term":{"urls.urlcategory":"x"}}]}},{"term":{"urls.urlsubcategory":"y"}}]}}}}}""", json, true)
  }

  @Test def whereClauseOnFieldsOfDifferentNestedPath_priorityChanged() {
    val ast = Spl.parse("TOP 10 urls.referrercategory  WHERE (conversionvisits.timestamp > '2012-12-12T12:12:12+06') AND ((urls.urlcategory = 'x') AND (urls.urlsubcategory = 'y')) USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"urls.referrercategory":{"terms":{"field":"urls.referrercategory","size":10,"order":"count"},"facet_filter":{"and":{"filters":[{"range":{"conversionvisits.timestamp":{"from":1355292732000,"to":null,"include_lower":false,"include_upper":true}}},{"query":{"nested":{"query":{"bool":{"must":[{"term":{"urls.urlcategory":"x"}},{"term":{"urls.urlsubcategory":"y"}}]}},"path":"urls"}}}]}}}}}""", json, true)
  }

  @Test def whereClauseOnNonNested() {
    val ast = Spl.parse("TOP 10 urls.referrercategory  WHERE exitfeedbacklastdate = '2012-12-12' or geolocation = 'gb' and lastreferrercategory = 'x' USING customer2.visitors")
    var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { json = esq.asString; None; })

    println(json)
    JSONAssert.assertEquals("""{"size":0,"facets":{"urls.referrercategory":{"terms":{"field":"urls.referrercategory","size":10,"order":"count"},"facet_filter":{"and":{"filters":[{"or":{"filters":[{"term":{"exitfeedbacklastdate":1355270400000}},{"term":{"geolocation":"gb"}}]}},{"term":{"lastreferrercategory":"x"}}]}}}}}""", json, true)
  }

  @Test def invalidDateString() {
    val ast = Spl.parse("TOP 10 urls.referrercategory  WHERE exitfeedbacklastdate = '0NaN-NaN-NaN' USING customer2.visitors")
    //var json: String = null
    ElasticSearchCodeGen.generate(ast.right.get)(esq => { None; })

    //println(json)
    //JSONAssert.assertEquals("""{"size":0,"facets":{"urls.referrercategory":{"terms":{"field":"urls.referrercategory","size":10,"order":"count"},"facet_filter":{"and":{"filters":[{"or":{"filters":[{"term":{"exitfeedbacklastdate":1355270400000}},{"term":{"geolocation":"gb"}}]}},{"term":{"lastreferrercategory":"x"}}]}}}}}""", json, true)
  }

}