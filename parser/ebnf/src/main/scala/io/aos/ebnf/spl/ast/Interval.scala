package io.aos.ebnf.spl.ast

sealed abstract class Interval(val duration: Float)

case class Year(override val duration: Float) extends Interval(duration)

case class Month(override val duration: Float) extends Interval(duration)

case class Week(override val duration: Float) extends Interval(duration)

case class Day(override val duration: Float) extends Interval(duration)

case class Hour(override val duration: Float) extends Interval(duration)

case class Minute(override val duration: Float) extends Interval(duration)

case class Numeric(override val duration: Float) extends Interval(duration)

object Interval {
  def isNumeric(interval: Interval): Boolean = {
    interval match {
      case Numeric(_) => true
      case _ => false
    }
  }
}