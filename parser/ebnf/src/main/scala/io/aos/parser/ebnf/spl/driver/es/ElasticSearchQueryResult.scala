package io.aos.parser.ebnf.spl.driver.es

import io.aos.parser.ebnf.spl.driver.QueryType
import scala.collection.immutable.List
import io.aos.parser.ebnf.spl.protocol.QueryResult

case class ElasticSearchQueryResult(
  fieldNames: Seq[String],
  keys: Map[String, Seq[Any]] = Map.empty,
  data: Map[String, Map[String, Any]] = Map.empty,
  stats: Map[String, Map[String, Double]] = Map.empty) extends QueryResult
