package io.aos.ebnf.spl.protocol

sealed abstract class ClientRequest() extends SplProtocolMessage

case class MetadataRequest(indexName: Option[String], allowedIndexes: Option[Set[String]]) extends ClientRequest()

case class HelpRequest(topic: Option[String]) extends ClientRequest()

case class QueryRequest(query: String, allowedIndexes: Set[String]) extends ClientRequest()
