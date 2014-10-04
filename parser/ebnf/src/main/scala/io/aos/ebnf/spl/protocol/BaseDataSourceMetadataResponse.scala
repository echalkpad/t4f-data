package io.aos.ebnf.spl.protocol

import scala.collection._

/**
 * Trait implemented by all back-end specific DataSourceMetadataResponses
 */
trait BaseDataSourceMetadataResponse {

  type T <: FieldMetadata

  def dataSources: Map[String, Seq[String]]

  def fieldMeta: Map[String, mutable.Map[String, T]]

}

trait BaseMetadataResponse {
  type T <: FieldMetadata

  def fieldMeta: Map[String, mutable.Map[String, T]]
}
