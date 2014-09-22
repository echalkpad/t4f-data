package io.aos.parser.ebnf.order

import scala.util.parsing.combinator.syntactical.StandardTokenParsers

object OrderParser extends StandardTokenParsers {

  lexical.delimiters ++= List("(", ")", ",")
  lexical.reserved += ("buy", "sell", "shares", "at", "max", "min", "for", "trading", "account")

  def instr: Parser[ClientOrder] =
    trans ~ account_spec ^^ { case t ~ a => new ClientOrder(scala2JavaList(t), a) }

  def trans: Parser[List[LineItem]] =
    "(" ~> repsep(trans_spec, ",") <~ ")" ^^ { (ts: List[LineItem]) => ts }

  def trans_spec: Parser[LineItem] =
    buy_sell ~ buy_sell_instr ^^ { case bs ~ bsi => new LineItem(bsi._1._2, bsi._1._1, bs, bsi._2) }

  def account_spec =
    "for" ~> "trading" ~> "account" ~> stringLit ^^ {case s => s}

  def buy_sell: Parser[ClientOrder.BuySell] =
    ("buy" | "sell") ^^ { case "buy" => ClientOrder.BuySell.BUY
                          case "sell" => ClientOrder.BuySell.SELL }

  def buy_sell_instr: Parser[((Int, String), Int)] =
    security_spec ~ price_spec ^^ { case s ~ p => (s, p) }

  def security_spec: Parser[(Int, String)] =
    numericLit ~ ident ~ "shares" ^^ { case n ~ a ~ "shares" => (n.toInt, a) }

  def price_spec: Parser[Int] =
    "at" ~ ("min" | "max") ~ numericLit ^^ { case "at" ~ s ~ n => n.toInt }

  def scala2JavaList(sl: List[LineItem]): java.util.List[LineItem] = {
    var jl = new java.util.ArrayList[LineItem]()
    sl.foreach(jl.add(_))
    jl
  }

}
