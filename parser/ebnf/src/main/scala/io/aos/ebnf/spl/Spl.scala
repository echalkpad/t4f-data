package io.aos.ebnf.spl

import io.aos.ebnf.spl.ast.SplQuery
import io.aos.ebnf.spl.driver.CodeGen
import io.aos.ebnf.spl.driver.es.{ ElasticSearchQuery, ElasticSearchQueryResult }
import io.aos.ebnf.spl.parser.SplParser
import io.aos.ebnf.spl.semantic.SplSemanticAnalyzer
import grizzled.slf4j.Logging
import io.aos.ebnf.spl.driver.GeneratedQuery
import io.aos.ebnf.spl.protocol.QueryResult

/**
 * Main entry point to the Spl parser library
 */
object Spl extends Logging {
  /**
   * Parses a Spl query string and returns a type annotated syntax tree.
   */
  def parse(query: String): Either[String, SplQuery] = {
    // parse the query and build the abstract syntax tree
    val abstractSyntaxTree = SplParser.buildAbstractSyntaxTree(query)
    if (abstractSyntaxTree.isLeft) {
      return Left(abstractSyntaxTree.left.get)
    }

    // annotate the AST with type data
    SplSemanticAnalyzer.annotate(abstractSyntaxTree.right.get)

  }

  /**
   * Parse, annotate and execute a Spl query
   *
   */
  def execute(query: String, authCheck: String => Unit): Either[String, QueryResult] = {
    val ast = parse(query)

    ast.fold(
      fail => Left(fail),
      succ => CodeGen.generate(succ, authCheck))
  }

}
