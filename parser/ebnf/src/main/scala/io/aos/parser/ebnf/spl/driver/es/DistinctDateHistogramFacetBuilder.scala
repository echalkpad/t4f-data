package io.aos.parser.ebnf.spl.driver.es

import org.elasticsearch.search.facet.FacetBuilder
import org.elasticsearch.common.xcontent.XContentBuilder
import org.elasticsearch.common.xcontent.ToXContent.Params
import org.elasticsearch.search.builder.SearchSourceBuilderException

class DistinctDateHistogramFacetBuilder(name: String) extends FacetBuilder(name) {
  final val FacetType = "distinct_date_histogram"

  private var keyFieldName: String = null
  private var valueFieldName: String = null
  private var interval: String = null

  def keyField(keyField: String): DistinctDateHistogramFacetBuilder = {
    this.keyFieldName = keyField
    return this
  }

  def valueField(valueField: String): DistinctDateHistogramFacetBuilder = {
    this.valueFieldName = valueField
    return this
  }


    def interval(interval: String): DistinctDateHistogramFacetBuilder = {
    this.interval = interval
    return this
  }

  def toXContent(builder: XContentBuilder, params: Params): XContentBuilder = {
    if (keyFieldName == null || valueFieldName == null) {
      throw new SearchSourceBuilderException("field name and value must be set on date histogram facet for facet [" + name + "]")
    }
    if (interval == null) {
      throw new SearchSourceBuilderException("interval must be set on date histogram facet for facet [" + name + "]")
    }

    builder.startObject(name)
    builder.startObject(FacetType)
    builder.field("key_field", keyFieldName)
    builder.field("value_field", valueFieldName)
    builder.field("interval", interval)

    //    if (comparatorType != null) {
    //      builder.field("comparator", comparatorType.description)
    //    }

    builder.endObject
    addFilterFacetAndGlobal(builder, params)
    builder.endObject
    return builder
  }
}
