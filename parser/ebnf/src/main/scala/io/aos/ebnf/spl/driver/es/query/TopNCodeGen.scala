package io.aos.ebnf.spl.driver.es.query

import io.aos.ebnf.spl.ast._
import org.elasticsearch.search.facet.FacetBuilders._
import io.aos.ebnf.spl.driver.es.ElasticSearchQuery
import org.elasticsearch.search.facet.FacetBuilder
import org.elasticsearch.search.facet.termsstats.TermsStatsFacet
import org.elasticsearch.search.facet.terms.TermsFacet
import io.aos.ebnf.spl.driver.es.ElasticSearchQueryResult

/**
 * Elastic Search query generator for TopN and BottomN querilasses
 */
object TopNCodeGen extends EsCodeGen {
  type T = TopTermsQuery

  override def generate(ast: TopTermsQuery)(execute: ElasticSearchQuery => Option[ElasticSearchQueryResult]): Either[String, ElasticSearchQueryResult] = {
    logger.debug("Generating TopN code")

    var concreteAst: SplQuery = null
    var index: String = null
    var fieldName: String = null

    var fieldRef: Field = null
    var filterRef: Option[WhereClause] = null

    // decide on the type of facet to build. Numeric fields get a TermStats facet while others get a Terms facet
    val facetBuilder = ast match {

      case q @ TopNQuery(param, count, field, ordering, filter, DataSource(ds, _)) => {
        logger.debug("Query is a TopN on a non-numeric field")
        concreteAst = q
        index = ds
        fieldName = field.name
        fieldRef = field
        filterRef = filter
        buildTermsFacet(count, field, ordering, filter, reverse = false, fieldName)
      }

      case q @ BottomNQuery(param, count, field, ordering, filter, DataSource(ds, _)) => {
        logger.debug("Query is a BottomN on a non-numeric field")
        concreteAst = q
        index = ds
        fieldName = field.name
        fieldRef = field
        filterRef = filter
        buildTermsFacet(count, field, ordering, filter, reverse = true, fieldName)
      }

      case _ => null
    }

    val filterSourceBuilder = createFilterSourceBuilder()

    // build the where clause
    if ((filterRef != null) && (filterRef.isDefined)) {
      /*if (areAnyDimensionsNested(fieldRef)) {
        val nestedPath = getPathIfNestedField(fieldRef)
        val query = buildQueryFromWhereClause(filterRef.get)
        facetBuilder.facetFilter(nestedFilter(nestedPath.get, query))
        //filterSourceBuilder.query(query)
      }
      else {*/
      val filter = buildFilterFromWhereClause(filterRef.get)
      facetBuilder.facetFilter(filter)
      //}
    }

    filterSourceBuilder.facet(facetBuilder)

    // Execute the query
    val query = execute(ElasticSearchQuery(filterSourceBuilder, index, Seq(fieldName), concreteAst))
    query match {
      case Some(r) => Right(r)

      case None    => Left("Query execution failed")
    }

  }

  private[query] def buildTermStatsFacet(count: Int, field: Field, ordering: Option[Ordering], filter: Option[WhereClause], reverse: Boolean, facetName: String): FacetBuilder = {
    val facetBuilder = termsStatsFacet(facetName)
    facetBuilder.keyField(field.name)
    facetBuilder.valueField(field.name)
    facetBuilder.size(count)

    // Not required because include_in_root is true for all nested objects
    //getPathIfNestedField(field) map { nestedPath => facetBuilder.nested(nestedPath) }

    //determine the ordering
    val orderClause = if (ordering.isDefined) {
      ordering.get match {
        case ByTerm()  => if (reverse) { TermsStatsFacet.ComparatorType.REVERSE_TERM } else { TermsStatsFacet.ComparatorType.TERM }

        case ByCount() => if (reverse) { TermsStatsFacet.ComparatorType.REVERSE_COUNT } else { TermsStatsFacet.ComparatorType.COUNT }

        case ByMin()   => if (reverse) { TermsStatsFacet.ComparatorType.REVERSE_MIN } else { TermsStatsFacet.ComparatorType.MIN }

        case ByMax()   => if (reverse) { TermsStatsFacet.ComparatorType.REVERSE_MAX } else { TermsStatsFacet.ComparatorType.MAX }

        case ByMean()  => if (reverse) { TermsStatsFacet.ComparatorType.REVERSE_MEAN } else { TermsStatsFacet.ComparatorType.MEAN }

        case ByTotal() => if (reverse) { TermsStatsFacet.ComparatorType.REVERSE_TOTAL } else { TermsStatsFacet.ComparatorType.TOTAL }
      }
    }
    else { // default ordering is by count
      if (reverse) { TermsStatsFacet.ComparatorType.REVERSE_COUNT } else { TermsStatsFacet.ComparatorType.COUNT }
    }

    facetBuilder.order(orderClause)

    // build the filter if defined
    if ((filter.isDefined) && (!areAnyDimensionsNested(field))) {
      val filterDef = buildFilterFromWhereClause(filter.get)
      facetBuilder.facetFilter(filterDef)
    }

    return facetBuilder
  }

  private[query] def buildTermsFacet(count: Int, field: Field, ordering: Option[Ordering], filter: Option[WhereClause], reverse: Boolean, facetName: String): FacetBuilder = {
    val facetBuilder = termsFacet(facetName)
    facetBuilder.field(field.name)
    facetBuilder.size(count)

    // Not required because include_in_root is true for all nested objects
    //getPathIfNestedField(field) map { nestedPath => facetBuilder.nested(nestedPath) }

    //determine the ordering
    val orderClause = if (ordering.isDefined) {
      ordering.get match {
        case ByTerm()  => if (reverse) { TermsFacet.ComparatorType.REVERSE_TERM } else { TermsFacet.ComparatorType.TERM }

        case ByCount() => if (reverse) { TermsFacet.ComparatorType.REVERSE_COUNT } else { TermsFacet.ComparatorType.COUNT }

        case _         => throw new RuntimeException("Invalid ordering for field type")
      }
    }
    else { // default ordering is by term
      if (reverse) { TermsFacet.ComparatorType.REVERSE_COUNT } else { TermsFacet.ComparatorType.COUNT }
    }

    facetBuilder.order(orderClause)

    // build the filter if defined
    if ((filter.isDefined) && (!areAnyDimensionsNested(field))) {
      val filterDef = buildFilterFromWhereClause(filter.get)
      facetBuilder.facetFilter(filterDef)
    }

    return facetBuilder
  }
}
