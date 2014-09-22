package io.aos.parser.ebnf.spl.driver

object QueryType extends Enumeration {
  val Describe = Value("describe")
  val Select = Value("select")
  val TopN = Value("topn")
  val BottomN = Value("bottomn")
  val Range = Value("range")
  val Pivot = Value("pivot")
  val Histogram = Value("histogram")
  val Stats = Value("stats")
  val Count = Value("count")
}
