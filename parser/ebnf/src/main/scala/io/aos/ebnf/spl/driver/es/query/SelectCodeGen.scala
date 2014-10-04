package io.aos.ebnf.spl.driver.es.query

import io.aos.ebnf.spl.ast.SelectQuery
import io.aos.ebnf.spl.driver.es.ElasticSearchQuery
import org.elasticsearch.search.builder.SearchSourceBuilder
import io.aos.ebnf.spl.driver.es.ElasticSearchQueryResult

/**
 * Elastic Search query generator for Select queries
 */
object SelectCodeGen extends EsCodeGen {
  type T = SelectQuery

  val MaxResultSize = 10000

  override def generate(ast: SelectQuery)(execute: ElasticSearchQuery => Option[ElasticSearchQueryResult]): Either[String, ElasticSearchQueryResult] = {
    logger.debug("Generating Select code")

    val filterSourceBuilder = createFilterSourceBuilder()
    // TODO: Get this from config
    filterSourceBuilder.size(MaxResultSize)

    // Build the filter definition
    val filterDef = buildFilterFromWhereClause(ast.filter)
    // TODO(ECH) fix this...
//    filterSourceBuilder.filter(filterDef)

    // Add field restrictions
    if (ast.fieldList.isDefined) {
      val fieldList = for (f <- ast.fieldList.get) yield f.name
      filterSourceBuilder.fields(fieldList: _*)
    }

    //val fields = ast.fieldList.getOrElse(Nil) map { f => f.name }
    val fields = Seq("visitorId")
    val result = execute(ElasticSearchQuery(filterSourceBuilder, ast.src.src, fields, ast))
    result match {
      case Some(r) => Right(r)

      case None => Left("Query execution failed")
    }

  }
}