package io.aos.parser.ebnf.exp

class Expr

case class Number(value: Int) extends Expr
case class Operator(symbol: String, left: Expr, right: Expr) extends Expr
