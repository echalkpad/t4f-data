package io.aos.ebnf.spl.driver.es.query

import io.aos.ebnf.spl.ast.StatsQuery
import io.aos.ebnf.spl.driver.es.ElasticSearchQuery
import org.elasticsearch.index.query.FilterBuilders._
import org.elasticsearch.index.query.QueryBuilders._
import org.elasticsearch.search.facet.FacetBuilders._
import org.elasticsearch.search.builder.SearchSourceBuilder
import io.aos.ebnf.spl.driver.es.ElasticSearchQueryResult

/**
 * Elastic Search query generator for Stats queries
 */
object StatsCodeGen extends EsCodeGen {
  type T = StatsQuery

  val FacetName = "stats"

  override def generate(ast: StatsQuery)(execute: ElasticSearchQuery => Option[ElasticSearchQueryResult]): Either[String, ElasticSearchQueryResult] = {
    logger.debug("Generating Stats code")

    val fieldList = for (f <- ast.fieldList) yield f.name

    val facetBuilder = statisticalFacet(FacetName)
    facetBuilder.fields(fieldList: _*)

    val filterSourceBuilder = createFilterSourceBuilder()

    // add the facet filter if the where clause is defined
    if (ast.filter.isDefined) {
      if (areAnyDimensionsNested(ast.fieldList: _*)) {
        val queryDef = buildQueryFromWhereClause(ast.filter.get)
        filterSourceBuilder.query(queryDef)
      }
      else {
        val filterDef = buildFilterFromWhereClause(ast.filter.get)
        facetBuilder.facetFilter(filterDef)
      }
    }

    filterSourceBuilder.facet(facetBuilder)

    val fields = ast.fieldList map { f => f.name }
    val result = execute(ElasticSearchQuery(filterSourceBuilder, ast.src.src, fields, ast))
    result match {
      case Some(r) => Right(r)

      case None => Left("Query execution failed")
    }

  }

}