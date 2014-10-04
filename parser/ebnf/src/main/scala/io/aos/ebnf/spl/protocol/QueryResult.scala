package io.aos.ebnf.spl.protocol

trait QueryResult {
  def keys: Map[String, Seq[Any]]
  def data: Map[String, Map[String, Any]]
  def stats: Map[String, Map[String, Double]]
  def fieldNames: Seq[String]
}
