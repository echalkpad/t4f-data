package io.aos.parser.ebnf.spl.driver.es

import io.aos.parser.ebnf.spl.exception.UnauthorizedActionException
import io.aos.parser.ebnf.spl.ast.{ AbsoluteHistogramQuery, CountQuery, DescQuery, PivotQuery, SplQuery, RangeQuery, RelativeHistogramQuery, SelectQuery, StatsQuery, TopTermsQuery }
import io.aos.parser.ebnf.spl.cache.Caching
import io.aos.parser.ebnf.spl.driver.CodeGen
import io.aos.parser.ebnf.spl.backend.BackendRegistry
import io.aos.parser.ebnf.spl.driver.es.query.{ AbsoluteHistogramCodeGen, CountCodeGen, DescribeCodeGen, PivotCodeGen, RangeCodeGen, RelativeHistogramCodeGen, SelectCodeGen, StatsCodeGen, TopNCodeGen }
import grizzled.slf4j.Logging
import io.aos.parser.ebnf.spl.backend.BackendType

/**
 * Generates Elastic Search queries from a Spl AST
 */
class ElasticSearchCodeGen extends CodeGen with Logging with Caching {
  type A = ElasticSearchQuery
  type B = ElasticSearchQueryResult

  lazy val cachedGenerate = cached(generateImpl)

  override def generate(ast: SplQuery)(execute: A => Option[B]): Either[String, B] = cachedGenerate(ast)(execute)

  def generateImpl(ast: SplQuery)(execute: A => Option[B]): Either[String, B] = {
    try {
      ast match {
        case a: DescQuery              => DescribeCodeGen.generate(a)(execute)

        case a: SelectQuery            => SelectCodeGen.generate(a)(execute)

        case a: TopTermsQuery          => TopNCodeGen.generate(a)(execute)

        case a: RangeQuery             => RangeCodeGen.generate(a)(execute)

        case a: AbsoluteHistogramQuery => AbsoluteHistogramCodeGen.generate(a)(execute)

        case a: RelativeHistogramQuery => RelativeHistogramCodeGen.generate(a)(execute)

        case a: StatsQuery             => StatsCodeGen.generate(a)(execute)

        case a: PivotQuery             => PivotCodeGen.generate(a)(execute)

        case a: CountQuery             => CountCodeGen.generate(a)(execute)

        case _                         => throw new RuntimeException("Unknown query type")
      }

    }
    catch {
      case e: UnauthorizedActionException => throw e

      case e: Throwable => {
        logger.warn("Caught exception during code generation", e)
        Left(s"Failed to generate Elastic Search query. [${e.getMessage()}]")
      }
    }
  }
}

object ElasticSearchCodeGen {
  private lazy val codegen = new ElasticSearchCodeGen()

  def generate(ast: SplQuery)(execute: ElasticSearchQuery => Option[ElasticSearchQueryResult]): Either[String, ElasticSearchQueryResult] = codegen.generate(ast)(execute)
}