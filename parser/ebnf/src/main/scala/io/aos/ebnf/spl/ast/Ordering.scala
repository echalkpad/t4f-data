package io.aos.ebnf.spl.ast

sealed abstract class Ordering

case class ByTerm() extends Ordering

case class ByCount() extends Ordering

case class ByMin() extends Ordering

case class ByMax() extends Ordering

case class ByMean() extends Ordering

case class ByTotal() extends Ordering

object Ordering {
  def isNonNumericOrdering(ordering: Ordering): Boolean = {
    ordering match {
      case ByTerm() | ByCount() => true
      case _ => false
    }
  }
}