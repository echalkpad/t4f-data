package io.aos.parser.ebnf.spl.driver.es.query

import io.aos.parser.ebnf.spl.driver.es.ElasticSearchQuery
import io.aos.parser.ebnf.spl.driver.es.ElasticSearchQueryResult
import org.elasticsearch.index.query.FilterBuilders._
import org.elasticsearch.index.query.QueryBuilders._
import org.elasticsearch.search.facet.FacetBuilders._
import io.aos.parser.ebnf.spl.ast._

object RelativeHistogramCodeGen extends EsCodeGen {
  type T = RelativeHistogramQuery

  override def generate(ast: RelativeHistogramQuery)(execute: ElasticSearchQuery => Option[ElasticSearchQueryResult]): Either[String, ElasticSearchQueryResult] = {
    val fieldName = ast.dimensionOne.name
    val facetName = ast.relativeInterval.name

    val facetBuilder = histogramScriptFacet(facetName)
    facetBuilder.valueScript("doc['%s'].value".format(fieldName))
    facetBuilder.keyScript(buildKeyScript(ast.timeDimension, ast.relativeInterval))

    val filterSourceBuilder = createFilterSourceBuilder()

    // build the filter
    if (ast.filter.isDefined) {
      if (areAnyDimensionsNested(ast.dimensionOne)) {
        val queryDef = buildQueryFromWhereClause(ast.filter.get)
        filterSourceBuilder.query(queryDef)
      }
      else {
        val filterDef = buildFilterFromWhereClause(ast.filter.get)
        facetBuilder.facetFilter(filterDef)
      }
    }

    filterSourceBuilder.facet(facetBuilder)

    val result = execute(ElasticSearchQuery(filterSourceBuilder, ast.src.src, Seq(facetName), ast))
    result match {
      case Some(r) => Right(r)

      case None => Left("Query execution failed")
    }
  }

  private def buildKeyScript(dimension: Field, interval: RelativeTimeInterval): String = "doc['%s'].date.%s".format(dimension.name, interval.name)

}