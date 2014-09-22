package io.aos.parser.ebnf.spl.protocol

sealed abstract class FieldMetadata

sealed case class EsFieldMetadata(dataType: String, nestedPath: Option[String], group: Option[String], name: Option[String]) extends FieldMetadata

sealed case class RsQueryParamMetadata(name: String, dataType: String, required: Boolean) extends FieldMetadata

sealed case class RsQueryMetadata(queryParams: Seq[RsQueryParamMetadata], query_id: Int, table: String, last_modified_by: String) extends FieldMetadata

