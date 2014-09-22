package io.aos.parser.ebnf.spl.backend

import scala.collection.mutable
import io.aos.parser.ebnf.spl.driver.GeneratedQuery
import io.aos.parser.ebnf.spl.protocol.QueryResult

object BackendRegistry {
  val registry: mutable.Map[BackendType.Value, Backend] = new mutable.HashMap()

  def register(backendType: BackendType.Value, backend: Backend) {
    registry.put(backendType, backend)
  }

  def getBackend(backendType: BackendType.Value): Backend = {
    if (registry.contains(backendType)) {
      registry(backendType)
    }
    else {
      throw new RuntimeException(s"No backend implementation registered for $backendType")
    }
  }

  /*
   * Use the indexname to try and determine the backend this query is attempting to target
   */
  def detectBackend(dataSourceName: String): Option[BackendType.Value] = {
    dataSourceName.substring(dataSourceName.lastIndexOf("-") + 1) match {
      case "visitors" => Some(BackendType.ElasticSearch)
      case _          => None
    }
  }

  def getQueryExecutor[A <: GeneratedQuery, B <: QueryResult](backendType: BackendType.Value, authCheck: String => Unit): A => Option[B] = {
    val backend = getBackend(backendType)
    val executor = backend.executeQuery(authCheck) _
    return executor.asInstanceOf[A => Option[B]]
  }

}
