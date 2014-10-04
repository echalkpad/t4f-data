package io.aos.ebnf.spl.ast

sealed abstract class PartialHistogramDef

case class PartialRelativeHistogram(relativeInterval: RelativeTimeInterval, timeDim: Field, filter: Option[WhereClause], src: DataSource) extends PartialHistogramDef

case class PartialAbsoluteHistogram(fieldTwo: Option[Field], interval: Interval, filter: Option[WhereClause], src: DataSource) extends PartialHistogramDef
