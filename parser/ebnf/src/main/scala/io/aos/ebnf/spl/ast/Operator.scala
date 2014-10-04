package io.aos.ebnf.spl.ast

sealed abstract class Operator

case class ComparisonOperator(operator: String, value: String) extends Operator

case class InOperator(valueList: Seq[String], negate: Boolean = false) extends Operator

case class ExistsOperator(negate: Boolean = false) extends Operator

