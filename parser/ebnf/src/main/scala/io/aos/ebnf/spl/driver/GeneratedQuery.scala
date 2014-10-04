package io.aos.ebnf.spl.driver

import io.aos.ebnf.spl.ast._

trait GeneratedQuery {
  def returnRawResults: Boolean

  def queryAst: SplQuery

  def asString(): String

  def queryType(): QueryType.Value = {
    queryAst match {
      case q: DescQuery              => QueryType.Describe

      case q: SelectQuery            => QueryType.Select

      case q: TopNQuery              => QueryType.TopN

      case q: BottomNQuery           => QueryType.BottomN

      case q: RangeQuery             => QueryType.Range

      case q: PivotQuery             => QueryType.Pivot

      case q: RelativeHistogramQuery => QueryType.Histogram

      case q: AbsoluteHistogramQuery => QueryType.Histogram

      case q: StatsQuery             => QueryType.Stats

      case q: CountQuery             => QueryType.Count

      case _                         => throw new RuntimeException("Unknown query type")
    }
  }
}