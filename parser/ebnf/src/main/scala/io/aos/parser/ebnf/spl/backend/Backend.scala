package io.aos.parser.ebnf.spl.backend

import io.aos.parser.ebnf.spl.driver.GeneratedQuery
import io.aos.parser.ebnf.spl.protocol.QueryResult

trait Backend {
  type A <: GeneratedQuery
  type B <: QueryResult
  def executeQuery(authCheck: String => Unit)(query: A): Option[B]
}

object BackendType extends Enumeration {
  val ElasticSearch = Value("ElasticSearch")
}
