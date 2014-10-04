package io.aos.ebnf.spl.protocol

import scala.collection.mutable

sealed abstract class ServerResponse extends SplProtocolMessage

case class DataSourceListResponse(dataSources: Seq[String]) extends ServerResponse

case class DataSourceMetadataResponse(fieldMeta: Map[String, FieldMetadata]) extends ServerResponse

//abstract case class FullDataSourceMetadataResponse extends ServerResponse with BaseDataSourceMetadataResponse 
//case class FullDataSourceMetadataResponse(dataSources: Map[String, Seq[String]], fieldMeta: Map[String, mutable.Map[String, GenericFieldMetadata]]) extends ServerResponse

case class HelpResponse(help: Map[String, HelpTopic]) extends ServerResponse

case class HelpTopicResponse(help: HelpTopic) extends ServerResponse

case class QueryResponse(resultSet: QueryResult) extends ServerResponse

case class ErrorResponse(message: String) extends ServerResponse

case class UnauthorizedResponse(message: String = "Access most vehemently denied.") extends ServerResponse


/*
 * Metadata Responses (defined here so we can keep server responses sealed)
 */

class EsDataSourceMetadataResponse(override val dataSources: Map[String, Seq[String]], override val fieldMeta: Map[String, mutable.Map[String, EsFieldMetadata]]) extends ServerResponse with BaseDataSourceMetadataResponse {
  type T = EsFieldMetadata
}

  
