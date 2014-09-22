package io.aos.parser.ebnf.spl.ast

sealed abstract class DimensionLimit

case class TopLimit(num: Int) extends DimensionLimit

case class BottomLimit(num: Int) extends DimensionLimit