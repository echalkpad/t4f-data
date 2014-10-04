package io.aos.ebnf.spl.driver

import scala.Predef._

import io.aos.ebnf.spl.ast.QueryAnnotation._
import io.aos.ebnf.spl.ast.SplQuery
import io.aos.ebnf.spl.backend.BackendRegistry
import io.aos.ebnf.spl.backend.BackendType
import io.aos.ebnf.spl.driver.es.ElasticSearchCodeGen
import io.aos.ebnf.spl.driver.es.ElasticSearchQuery
import io.aos.ebnf.spl.driver.es.ElasticSearchQueryResult
import io.aos.ebnf.spl.driver.es.ElasticSearchQueryResult
import io.aos.ebnf.spl.protocol.QueryResult

trait CodeGen {
  type A <: GeneratedQuery
  type B <: QueryResult

  def generate(ast: SplQuery)(execute: A => Option[B]): Either[String, B]
}

object CodeGen {

  def generate(ast: SplQuery, authCheck: String => Unit): Either[String, QueryResult] = {
    ast match {
      case q: QueryWithDataSource => {
        q.src.backend match {
          case BackendType.ElasticSearch => {
            val executor = BackendRegistry.getQueryExecutor[ElasticSearchQuery, ElasticSearchQueryResult](BackendType.ElasticSearch, authCheck)
            ElasticSearchCodeGen.generate(ast)(executor)
          }

//          case BackendType.Redshift => {
//            val executor = BackendRegistry.getQueryExecutor[RedshiftQuery, RedshiftQueryResult](BackendType.Redshift, authCheck)
//            RedshiftCodeGen.generate(ast)(executor)
//          }

          case _ => Left("Backend not yet implemented")
        }

      }
      case _ => throw new RuntimeException("Unknown query backend")
    }
  }
}