package io.aos.ebnf.spl.protocol

import spray.json._
import io.aos.ebnf.util.JsonUtil

object JsonProtocol extends DefaultJsonProtocol {

  implicit val queryRequest = jsonFormat2(QueryRequest)

  implicit val metadataRequest = jsonFormat2(MetadataRequest)

  implicit val helpRequest = jsonFormat1(HelpRequest)

  implicit object QueryResultFormat extends RootJsonFormat[QueryResult] {

    /*
     * Spray-Json is quite strict and makes it difficult to serialize maps of unknown nesting levels. Jerkson will
     * happily deal with them but unfortunately it alone can't be used in a Spray HTTP pipeline. Hence this abomination
     * has been brought to life. I am sorry.
     */
    case class QueryResultWrapper(
      fieldNames: Seq[String],
      keys: Map[String, Seq[Any]] = Map.empty,
      data: Map[String, Map[String, Any]] = Map.empty,
      stats: Map[String, Map[String, Double]] = Map.empty) extends QueryResult

    def write(r: QueryResult): JsValue = {
      val jsonSource: String = JsonUtil.toJson(r)
      jsonSource.asJson
    }

    def read(jsonSource: JsValue): QueryResult = {
      JsonUtil.fromString[QueryResultWrapper](jsonSource.toString)
    }

  }

}
