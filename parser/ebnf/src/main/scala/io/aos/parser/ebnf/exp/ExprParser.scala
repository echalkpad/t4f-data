package io.aos.parser.ebnf.exp

import scala.util.parsing.combinator.RegexParsers

class ExprParser extends RegexParsers {
  
  val number = "[1-9][0-9]*".r

  def expr: Parser[Int] = (number ^^ { _.toInt }) ~ opt(operator ~ expr) ^^ {
    case a ~ None => a
    case a ~ Some("*" ~ b) => a * b
    case a ~ Some("/" ~ b) => a / b
    case a ~ Some("+" ~ b) => a + b
    case a ~ Some("-" ~ b) => a - b
  }

  def operator: Parser[String] = "+" | "-" | "*" | "/"

  def term: Parser[Expr] = (factor ~ opt(operator ~ term)) ^^ {
    case a ~ None => a
    case a ~ Some(b ~ c) => Operator(b, a, c)
  }

  def factor: Parser[Expr] = number ^^ (n => Number(n.toInt))

}
