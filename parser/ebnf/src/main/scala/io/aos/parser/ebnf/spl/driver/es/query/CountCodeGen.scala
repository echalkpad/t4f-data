package io.aos.parser.ebnf.spl.driver.es.query

import io.aos.parser.ebnf.spl.ast.CountQuery
import io.aos.parser.ebnf.spl.driver.es.ElasticSearchQuery
import io.aos.parser.ebnf.spl.driver.es.ElasticSearchQueryResult
import org.elasticsearch.index.query.FilterBuilders._
import org.elasticsearch.index.query.QueryBuilders._
import org.elasticsearch.search.facet.FacetBuilders._
import org.elasticsearch.search.builder.SearchSourceBuilder

object CountCodeGen extends EsCodeGen {
  type T = CountQuery

  val FacetName = "count"

  override def generate(ast: CountQuery)(execute: ElasticSearchQuery => Option[ElasticSearchQueryResult]): Either[String, ElasticSearchQueryResult] = {
    logger.debug("Generating count code")

    val searchSourceBuilder = createFilterSourceBuilder()

    val filter = ast.filter match {
      case Some(f) => buildFilterFromWhereClause(f)

      case None => matchAllFilter()
    }

    searchSourceBuilder.facet(filterFacet(FacetName, filter))
    val result = execute(ElasticSearchQuery(searchSourceBuilder, ast.src.src, Seq("count"), ast))
    result match {
      case Some(r) => Right(r)

      case None => Left("Query execution failed")
    }

  }
}