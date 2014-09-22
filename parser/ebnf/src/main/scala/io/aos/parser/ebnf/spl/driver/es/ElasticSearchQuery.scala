package io.aos.parser.ebnf.spl.driver.es

import org.elasticsearch.common.xcontent.XContentFactory
import org.elasticsearch.search.builder.SearchSourceBuilder

import io.aos.parser.ebnf.spl.ast.SplQuery
import io.aos.parser.ebnf.spl.driver.GeneratedQuery

/**
 * Implementation of Elastic Search query object
 */
trait ElasticSearchQuery extends GeneratedQuery {

  def searchSourceBuilder: SearchSourceBuilder

  def indexName: String

  def setResultLimit(limit: Int): Unit

  def fields: Seq[String]
}

object ElasticSearchQuery {
  val OtherTerm = "__other__"
  val NullTerm = "__null__"
  val TotalsTerm = "__totals__"
  val ConversionsTerm = "__conversions__"

  def apply(
    searchSource: SearchSourceBuilder,
    index: String,
    fields: Seq[String],
    ast: SplQuery,
    returnRawResults: Boolean = false): ElasticSearchQuery = new ElasticSearchQueryImpl(searchSource, index, fields, ast, returnRawResults)

  private class ElasticSearchQueryImpl(searchSource: SearchSourceBuilder, index: String, fieldList: Seq[String], ast: SplQuery, returnRaw: Boolean) extends ElasticSearchQuery {
    override val searchSourceBuilder: SearchSourceBuilder = searchSource

    override def setResultLimit(limit: Int): Unit = searchSource.size(limit)

    override def queryAst: SplQuery = ast

    override val indexName = index

    override val fields = fieldList

    override val returnRawResults = returnRaw

    override def asString(): String = {
      val jsonFactory = XContentFactory.jsonBuilder()
      searchSource.toXContent(jsonFactory, null).string()
    }
  }
}

