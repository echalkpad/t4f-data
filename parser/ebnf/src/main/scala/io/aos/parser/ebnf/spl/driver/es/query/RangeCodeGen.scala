package io.aos.parser.ebnf.spl.driver.es.query

import io.aos.parser.ebnf.spl.ast._
import org.elasticsearch.index.query.FilterBuilders._
import org.elasticsearch.index.query.QueryBuilders._
import org.elasticsearch.search.facet.FacetBuilders._
import io.aos.parser.ebnf.spl.driver.es.ElasticSearchQuery
import org.elasticsearch.search.builder.SearchSourceBuilder
import io.aos.parser.ebnf.spl.driver.es.ElasticSearchQuery
import io.aos.parser.ebnf.spl.driver.es.ElasticSearchQueryResult

/**
 * Elastic Search query generator for Range queries
 */
object RangeCodeGen extends EsCodeGen {
  type T = RangeQuery

  override def generate(ast: RangeQuery)(execute: ElasticSearchQuery => Option[ElasticSearchQueryResult]): Either[String, ElasticSearchQueryResult] = {
    logger.debug("Generating Range code")
    val fieldName = ast.dimensionOne.name
    val rangeFacetBuilder = rangeFacet(fieldName)
    var dimensions: Seq[Field] = Seq.empty

    // build the range list
    for (rangeDef <- ast.rangeList) {
      rangeDef match {
        case RangeDef(None, Some(to)) => rangeFacetBuilder.addUnboundedFrom(to)

        case RangeDef(Some(from), None) => rangeFacetBuilder.addUnboundedTo(from)

        case RangeDef(Some(from), Some(to)) => rangeFacetBuilder.addRange(from, to)

        case _ => throw new RuntimeException("Illegal range definition")
      }
    }

    // add the dimensions
    rangeFacetBuilder.keyField(ast.dimensionOne.name)
    if (ast.dimensionTwo.isDefined) {
      dimensions = Seq(ast.dimensionOne, ast.dimensionTwo.get)
      rangeFacetBuilder.valueField(ast.dimensionTwo.get.name)
    }
    else {
      dimensions = Seq(ast.dimensionOne)
      rangeFacetBuilder.valueField(ast.dimensionOne.name)
    }

    // Not required because include_in_root is true for all nested objects
    //getPathIfNestedField(ast.dimensionOne) map { nestedPath => rangeFacetBuilder.nested(nestedPath) }

    val filterSourceBuilder = createFilterSourceBuilder()

    // build the filter
    if (ast.filter.isDefined) {
      if (areAnyDimensionsNested(dimensions: _*)) {
        val queryDef = buildQueryFromWhereClause(ast.filter.get)
        filterSourceBuilder.query(queryDef)
      }
      else {
        val filterDef = buildFilterFromWhereClause(ast.filter.get)
        rangeFacetBuilder.facetFilter(filterDef)
      }
    }

    filterSourceBuilder.facet(rangeFacetBuilder)

    val result = execute(ElasticSearchQuery(filterSourceBuilder, ast.src.src, Seq(ast.dimensionOne.name), ast))
    result match {
      case Some(r) => Right(r)

      case None => Left("Query execution failed")
    }

  }

}