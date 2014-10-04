package io.aos.ebnf.spl.driver.es.query

import io.aos.ebnf.spl.ast.DescQuery
import io.aos.ebnf.spl.driver.es.ElasticSearchQuery
import org.elasticsearch.search.builder.SearchSourceBuilder
import io.aos.ebnf.spl.driver.es.ElasticSearchQueryResult

object DescribeCodeGen extends EsCodeGen {
  type T = DescQuery

  def generate(ast: DescQuery)(execute: ElasticSearchQuery => Option[ElasticSearchQueryResult]): Either[String, ElasticSearchQueryResult] = {
    val fields = ast.field map (f => Seq(f.name))
    val result = execute(ElasticSearchQuery(SearchSourceBuilder.searchSource(), ast.src, fields.getOrElse(Nil), ast))
    result match {
      case Some(r) => Right(r)

      case None => Left("Query execution failed")
    }
  }

}