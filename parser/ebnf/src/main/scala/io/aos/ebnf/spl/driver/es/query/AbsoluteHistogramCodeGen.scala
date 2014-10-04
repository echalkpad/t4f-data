package io.aos.ebnf.spl.driver.es.query

import io.aos.ebnf.spl.ast._
import org.elasticsearch.search.facet.FacetBuilders._
import io.aos.ebnf.spl.driver.es.{DistinctDateHistogramFacetBuilder, ElasticSearchQuery, ElasticSearchQueryResult}
import org.elasticsearch.search.facet.FacetBuilder

trait HistogramCodeGen {
  def generateDateHistogramInterval(interval: Interval): String = {
    val NumWeeksPerYear = 52
    val NumWeeksPerMonth = 4

    interval match {
      case Year(1) => "year"

      case Year(d) => (d * NumWeeksPerYear) + "w"

      case Month(1) => "month"

      case Month(d) => (d * NumWeeksPerMonth) + "w"

      case Week(1) => "week"

      case Week(d) => d + "w"

      case Day(1) => "day"

      case Day(d) => d + "d"

      case Hour(1) => "hour"

      case Hour(d) => d + "h"

      case Minute(1) => "minute"

      case Minute(d) => d + "m"

      case _ => throw new RuntimeException("Invalid date interval")
    }
  }
}

object AbsoluteHistogramCodeGen extends EsCodeGen with HistogramCodeGen {
  type T = AbsoluteHistogramQuery

  override def generate(ast: AbsoluteHistogramQuery)(execute: ElasticSearchQuery => Option[ElasticSearchQueryResult]): Either[String, ElasticSearchQueryResult] = {
    val fieldName = ast.dimensionOne.name
    var dimensions: Seq[Field] = Seq.empty

    val facetBuilder: FacetBuilder = if (ast.dimensionTwo.isDefined) {
      dimensions = Seq(ast.dimensionOne, ast.dimensionTwo.get)
      buildHistogramFacet(ast.dimensionOne, ast.dimensionTwo.get, ast.interval, ast.param)
    }
    else {
      dimensions = Seq(ast.dimensionOne)
      buildHistogramFacet(ast.dimensionOne, ast.dimensionOne, ast.interval, ast.param)
    }

    val filterSourceBuilder = createFilterSourceBuilder()

    // build the filter
    if (ast.filter.isDefined) {
      if (areAnyDimensionsNested(dimensions: _*)) {
        val queryDef = buildQueryFromWhereClause(ast.filter.get)
        filterSourceBuilder.query(queryDef)
      }
      else {
        val filterDef = buildFilterFromWhereClause(ast.filter.get)
        facetBuilder.facetFilter(filterDef)
      }
    }

    filterSourceBuilder.facet(facetBuilder)

    val result = execute(ElasticSearchQuery(filterSourceBuilder, ast.src.src, Seq(fieldName), ast))
    result match {
      case Some(r) => Right(r)

      case None => Left("Query execution failed")
    }
  }

  private def buildHistogramFacet(dimensionOne: Field, dimensionTwo: Field, interval: Interval, param: Option[String]): FacetBuilder = {

    def standardFacet(): FacetBuilder = {
      if (isNumericField(dimensionTwo)) {
        val facetBuilder = histogramFacet(dimensionOne.name)
        facetBuilder.keyField(dimensionTwo.name)
        facetBuilder.valueField(dimensionOne.name)
        facetBuilder.interval(interval.duration.toLong)

        // Not required because include_in_root is true for all nested objects
        //getPathIfNestedField(dimensionTwo) map { nestedPath => facetBuilder.nested(nestedPath) }
        return facetBuilder
      }
      else {
        val facetBuilder = dateHistogramFacet(dimensionOne.name)
        facetBuilder.keyField(dimensionTwo.name)
        facetBuilder.valueField(dimensionOne.name)
        facetBuilder.interval(generateDateHistogramInterval(interval))

        // Not required because include_in_root is true for all nested objects
        //getPathIfNestedField(dimensionTwo) map { nestedPath => facetBuilder.nested(nestedPath) }
        return facetBuilder
      }
    }

    def distinctVisitorsHistoFacet(): FacetBuilder = {
      val facetBuilder = new DistinctDateHistogramFacetBuilder(dimensionOne.name)
      facetBuilder.keyField(dimensionOne.name)
      //ignore second dimension(!)
      val valField = "visitorid"
      facetBuilder.valueField(valField)
      facetBuilder.interval(generateDateHistogramInterval(interval))
      facetBuilder
    }

    param match {
      case Some("__visitors__") => distinctVisitorsHistoFacet()
      case _ => standardFacet()
    }
  }

}
