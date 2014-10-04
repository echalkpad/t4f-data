package io.aos.ebnf.spl.driver.es

import org.elasticsearch.search.builder.SearchSourceBuilder
import org.junit.Before
import org.junit.Test
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.skyscreamer.jsonassert.JSONAssert

import io.aos.ebnf.spl.driver.es.DistinctDateHistogramFacetBuilder;
import io.aos.ebnf.spl.semantic.DataSourceMetadata
import io.aos.ebnf.spl.semantic.OfflineDataSourceMetadata

class DistinctDateHistogramFacetBuilderTest extends JUnitSuite with ShouldMatchersForJUnit {

  @Before def setup() {
    DataSourceMetadata.registerUpdater(OfflineDataSourceMetadata.load)
  }

  @Test def simpleDistinctFacet() {
    val fb = new DistinctDateHistogramFacetBuilder("testFacet")
    fb.interval("1 day")
    fb.keyField("keyField")
    fb.valueField("valField")
    val builder = SearchSourceBuilder.searchSource()
    builder.size(0)
    builder.facet(fb)

    val json = builder.toString
    JSONAssert.assertEquals("""{"size":0,"facets":{"testFacet":{"distinct_date_histogram":{"key_field":"keyField","value_field":"valField","interval":"1 day"}}}}""", json, true)
  }

}
