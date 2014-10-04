package io.aos.ebnf.spl.semantic

import io.aos.ebnf.spl.ast._
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.StringBuilder
import io.aos.ebnf.spl.semantic.impl.SplSemanticAnalyzerImpl
import scala.util.Try
import scala.util.Success
import scala.util.Failure

trait SplSemanticAnalyzer {
  def annotate(ast: SplQuery): Either[String, SplQuery]
}

object SplSemanticAnalyzer extends SplSemanticAnalyzer {

  private val analyzer = new SplSemanticAnalyzerImpl()

  override def annotate(ast: SplQuery): Either[String, SplQuery] = analyzer.annotate(ast)

  private[spl] def buildAnnotatedSyntaxTree(ast: SplQuery): Either[String, SplQuery] = analyzer.annotate(ast)

  private[spl] def annotateBooleanExpression(dataSrc: String, expr: BooleanExpression): Either[String, TypedBooleanExpression] = {
    Try(analyzer.annotateBooleanExpression(dataSrc, expr)) match {
      case Success(exp) => Right(exp)
      case Failure(e)   => Left(e.getMessage())
    }
  }

}
