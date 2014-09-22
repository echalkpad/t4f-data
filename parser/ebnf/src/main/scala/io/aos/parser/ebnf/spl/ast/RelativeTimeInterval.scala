package io.aos.parser.ebnf.spl.ast

sealed abstract class RelativeTimeInterval(val name: String)

case class DayOfWeek() extends RelativeTimeInterval("dayOfWeek")

case class DayOfMonth() extends RelativeTimeInterval("dayOfMonth")

case class MonthOfYear() extends RelativeTimeInterval("monthOfYear")

case class MinuteOfDay() extends RelativeTimeInterval("minuteOfDay")

case class HourOfDay() extends RelativeTimeInterval("hourOfDay")

case class MinuteOfHour() extends RelativeTimeInterval("minuteOfHour")

