package io.aos.ebnf.spl.ast

object QueryAnnotation {
  type QueryWithDataSource = { val src: DataSource }
}

sealed abstract class SplQuery

trait TopTermsQuery extends SplQuery

case class DescQuery(src: String, field: Option[Field] = None) extends SplQuery

case class SelectQuery(fieldList: Option[List[Field]], filter: WhereClause, src: DataSource) extends SplQuery

case class TopNQuery(param: Option[String], count: Int, field: Field, ordering: Option[Ordering], filter: Option[WhereClause], src: DataSource) extends SplQuery with TopTermsQuery

case class BottomNQuery(param: Option[String], count: Int, field: Field, ordering: Option[Ordering], filter: Option[WhereClause], src: DataSource) extends SplQuery with TopTermsQuery

case class RangeQuery(param: Option[String], rangeList: List[RangeDef], dimensionOne: Field, dimensionTwo: Option[Field], filter: Option[WhereClause], src: DataSource) extends SplQuery

case class PivotQuery(param: Option[String], dimLimit: Option[DimensionLimit], dimensionOne: Field, dimensionTwo: Field, filter: Option[WhereClause], src: DataSource) extends SplQuery

case class RelativeHistogramQuery(param: Option[String], dimensionOne: Field, relativeInterval: RelativeTimeInterval, timeDimension: Field, filter: Option[WhereClause], src: DataSource) extends SplQuery

case class AbsoluteHistogramQuery(param: Option[String], dimensionOne: Field, dimensionTwo: Option[Field], interval: Interval, filter: Option[WhereClause], src: DataSource) extends SplQuery

case class StatsQuery(param: Option[String], fieldList: List[Field], filter: Option[WhereClause], src: DataSource) extends SplQuery

case class CountQuery(filter: Option[WhereClause], src: DataSource) extends SplQuery