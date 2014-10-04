package io.aos.ebnf.spl.parser

import io.aos.ebnf.spl.parser.SplParser;
import io.aos.ebnf.spl.ast._

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class SplParserTest extends FlatSpec with ShouldMatchers {

  /* =========================== Desc =========================== */

  "A desc query" should "parse correctly in the short form" in {
    val ast = SplParser.generateTree("desc stylistpick")
    ast.isDefined should be(true)
    ast.get should equal(DescQuery("stylistpick"))
  }
  it should "parse correctly in the long form" in {
    val ast = SplParser.generateTree("describe stylistpick")
    ast.isDefined should be(true)
    ast.get should equal(DescQuery("stylistpick"))
  }
  it should "parse correctly with field name" in {
    val ast = SplParser.generateTree("describe stylistpick#urls")
    ast.isDefined should be(true)
    ast.get should equal(DescQuery("stylistpick", Some(UntypedField("urls"))))
  }
  it should "parse upper case keywords" in {
    val ast = SplParser.generateTree("DESC locats.dog")
    ast.isDefined should be(true)
    ast.get should equal(DescQuery("locats.dog"))
  }
  it should "parse mixed case keywords" in {
    val ast = SplParser.generateTree("dEsC loLcatS")
    print(ast)
    ast.isDefined should be(true)
    ast.get should equal(DescQuery("loLcatS"))
  }
  it should "ignore leading and trailing spaces" in {
    val ast = SplParser.generateTree(" desc loLcatS ")
    ast.isDefined should be(true)
    ast.get should equal(DescQuery("loLcatS"))
  }
  it should "not parse a transposed word" in {
    val ast = SplParser.generateTree("decs")
    ast.isDefined should be(false)
  }
  it should "not parse when source is missing" in {
    val ast = SplParser.generateTree("desc")
    print(ast)
    ast.isDefined should be(false)
  }

  /* =========================== Select =========================== */
  "A select query" should "parse correctly with just the filter" in {
    val ast = SplParser.generateTree("where field = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      SelectQuery(
        None,
        WhereClause(Condition(UntypedField("field"), ComparisonOperator("=", "true"))),
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with the field list" in {
    val ast = SplParser.generateTree("select f1,f2 where field = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      SelectQuery(
        Some(List(UntypedField("f1"), UntypedField("f2"))),
        WhereClause(Condition(UntypedField("field"), ComparisonOperator("=", "true"))),
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with mixed where" in {
    val ast = SplParser.generateTree("WHERE conversionflag = true AND (conversionvisits.timestamp >= '2012-07-01' OR (lastreferrer IN('google','bing','yahoo'))) USING customer2.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      SelectQuery(
        None,
        WhereClause(AndOperator(
          Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")),
          OrOperator(
            Condition(UntypedField("conversionvisits.timestamp"), ComparisonOperator(">=", "2012-07-01")),
            Condition(UntypedField("lastreferrer"), InOperator(Seq("google", "bing", "yahoo"), false))))),
        DataSource("customer2.visitors")))
  }
  it should "not parse when source is missing" in {
    val ast = SplParser.generateTree("select f1,f2 where field = true")
    ast.isDefined should be(false)
  }

  /* =========================== TopN/BottomN =========================== */

  "A topN query" should "parse correctly without a filter" in {
    val ast = SplParser.generateTree("top 20 urls.url using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(TopNQuery(None, 20, UntypedField("urls.url"), None, None, DataSource("stylistpick.visitors")))
  }
  it should "fail if there is more than one field" in {
    val ast = SplParser.generateTree("top 20 urls.url,conversion using stylistpick.visitors")
    ast.isDefined should be(false)
  }
  it should "parse data source with dashes in the name" in {
    val ast = SplParser.generateTree("top 20 urls.url using customer3.visitors")
    ast.isDefined should be(true)
  }
  it should "parse regardless of letter case" in {
    val ast = SplParser.generateTree("TOP 20 urls.url uSINg stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(TopNQuery(None, 20, UntypedField("urls.url"), None, None, DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with a filter" in {
    val ast = SplParser.generateTree("top 20 urls.url where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(TopNQuery(None, 20, UntypedField("urls.url"), None, Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))), DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with bottomN keyword" in {
    val ast = SplParser.generateTree("bottom 20 urls.url where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(BottomNQuery(None, 20, UntypedField("urls.url"), None, Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))), DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with an ordering" in {
    val ast = SplParser.generateTree("top 20 urls.url by mean using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(TopNQuery(None, 20, UntypedField("urls.url"), Some(ByMean()), None, DataSource("stylistpick.visitors")))
  }
  it should "parse correctly regardless of the case of the ordering" in {
    val ast = SplParser.generateTree("top 20 urls.url by MEAN using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(TopNQuery(None, 20, UntypedField("urls.url"), Some(ByMean()), None, DataSource("stylistpick.visitors")))
  }
  it should "fail when ordering is unknown" in {
    val ast = SplParser.generateTree("top 20 urls.url by median using stylistpick.visitors")
    ast.isDefined should be(false)
  }
  it should "parse correctly with option list" in {
    val ast = SplParser.generateTree("""top /*+ "key_field_script":"doc['f'].value / 2" */ 20 urls.url where conversionflag = true using stylistpick.visitors""")
    ast.isDefined should be(true)
    ast.get should equal(TopNQuery(Some(""""key_field_script":"doc['f'].value / 2""""), 20, UntypedField("urls.url"), None, Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))), DataSource("stylistpick.visitors")))
  }
  it should "parse when converted to bottomN" in {
    val ast = SplParser.generateTree("""bottom /*+ "key_field_script":"doc['f'].value / 2" */ 20 urls.url where conversionflag = true using stylistpick.visitors""")
    ast.isDefined should be(true)
    ast.get should equal(BottomNQuery(Some(""""key_field_script":"doc['f'].value / 2""""), 20, UntypedField("urls.url"), None, Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))), DataSource("stylistpick.visitors")))
  }
  it should "fail to parse floating point field limits" in {
    val ast = SplParser.generateTree("top 20.5 urls.url by mean using stylistpick.visitors")
    ast.isDefined should be(false)
  }

  /* =========================== Range =========================== */

  "A range query" should "parse correctly with a single value field" in {
    val ast = SplParser.generateTree("range(..10,10..20,20..30,30) over conversionvalue using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      RangeQuery(
        None,
        List(RangeDef(None, Some(10)), RangeDef(Some(10), Some(20)), RangeDef(Some(20), Some(30)), RangeDef(Some(30), None)),
        UntypedField("conversionvalue"),
        None,
        None,
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with a key and value field" in {
    val ast = SplParser.generateTree("range(..10,10..20,20..30,30) over conversionvalue of urls.url using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      RangeQuery(
        None,
        List(RangeDef(None, Some(10)), RangeDef(Some(10), Some(20)), RangeDef(Some(20), Some(30)), RangeDef(Some(30), None)),
        UntypedField("conversionvalue"),
        Some(UntypedField("urls.url")),
        None,
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with a filter" in {
    val ast = SplParser.generateTree("range(..10,10..20,20..30,30) over conversionvalue where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      RangeQuery(
        None,
        List(RangeDef(None, Some(10)), RangeDef(Some(10), Some(20)), RangeDef(Some(20), Some(30)), RangeDef(Some(30), None)),
        UntypedField("conversionvalue"),
        None,
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with an option list" in {
    val ast = SplParser.generateTree("""range /*+ "key_field_script":"doc['f'].value / 2" */ (..10,10..20,20..30,30) over conversionvalue where conversionflag = true using stylistpick.visitors""")
    ast.isDefined should be(true)
    ast.get should equal(
      RangeQuery(
        Some(""""key_field_script":"doc['f'].value / 2""""),
        List(RangeDef(None, Some(10)), RangeDef(Some(10), Some(20)), RangeDef(Some(20), Some(30)), RangeDef(Some(30), None)),
        UntypedField("conversionvalue"),
        None,
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "not parse correctly without a source" in {
    val ast = SplParser.generateTree("range(..10,10..20,20..30,30) over conversionvalue where conversionflag = true using")
    ast.isDefined should be(false)
  }
  it should "not parse correctly with erroneous range" in {
    val ast = SplParser.generateTree("range(.10,10.20,20..30,30) over conversionvalue where conversionflag = true using stylistpickuk.visitors")
    ast.isDefined should be(false)
  }
  it should "not parse correctly without a range" in {
    val ast = SplParser.generateTree("range() over conversionvalue where conversionflag = true using stylistpickuk.visitors")
    ast.isDefined should be(false)
  }

  /* =========================== Pivot =========================== */

  "A pivot query" should "parse correctly without a filter" in {
    val ast = SplParser.generateTree("pivot urls.url ON conversionflag using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      PivotQuery(
        None,
        None,
        UntypedField("urls.url"),
        UntypedField("conversionflag"),
        None,
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with a filter" in {
    val ast = SplParser.generateTree("pivot urls.url ON conversionflag where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      PivotQuery(
        None,
        None,
        UntypedField("urls.url"),
        UntypedField("conversionflag"),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with a top n dimenstion limit" in {
    val ast = SplParser.generateTree("pivot top 50 urls.url ON conversionflag where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      PivotQuery(
        None,
        Some(TopLimit(50)),
        UntypedField("urls.url"),
        UntypedField("conversionflag"),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with a bottom n dimenstion limit" in {
    val ast = SplParser.generateTree("pivot bottom 50 urls.url ON conversionflag where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      PivotQuery(
        None,
        Some(BottomLimit(50)),
        UntypedField("urls.url"),
        UntypedField("conversionflag"),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with an option string" in {
    val ast = SplParser.generateTree("""pivot  /*+ "key_field_script":"doc['f'].value / 2" */ top 50 urls.url ON conversionflag where conversionflag = true using stylistpick.visitors""")
    ast.isDefined should be(true)
    ast.get should equal(
      PivotQuery(
        Some(""""key_field_script":"doc['f'].value / 2""""),
        Some(TopLimit(50)),
        UntypedField("urls.url"),
        UntypedField("conversionflag"),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "fail parsing when no source is defined" in {
    val ast = SplParser.generateTree("""pivot  /*+ "key_field_script":"doc['f'].value / 2" */ top 50 urls.url ON conversionflag where conversionflag = true using """)
    ast.isDefined should be(false)
  }
  it should "fail parsing when limit is missing" in {
    val ast = SplParser.generateTree("""pivot  /*+ "key_field_script":"doc['f'].value / 2" */ top urls.url ON conversionflag where conversionflag = true using stylistpick.visitors""")
    ast.isDefined should be(false)
  }

  /* =========================== Histogram =========================== */

  "A relative histogram query" should "parse correctly without a filter" in {
    val ast = SplParser.generateTree("histogram of urls.url by day of week of timestamp using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      RelativeHistogramQuery(
        None,
        UntypedField("urls.url"),
        DayOfWeek(),
        UntypedField("timestamp"),
        None,
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with a filter" in {
    val ast = SplParser.generateTree("histogram of urls.url by day of week of timestamp where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      RelativeHistogramQuery(
        None,
        UntypedField("urls.url"),
        DayOfWeek(),
        UntypedField("timestamp"),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with an option string" in {
    val ast = SplParser.generateTree("""histogram /*+ "key_field_script":"doc['f'].value / 2" */ of urls.url by day of month of timestamp where conversionflag = true using stylistpick.visitors""")
    ast.isDefined should be(true)
    ast.get should equal(
      RelativeHistogramQuery(
        Some(""""key_field_script":"doc['f'].value / 2""""),
        UntypedField("urls.url"),
        DayOfMonth(),
        UntypedField("timestamp"),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "fail when the interval is wrong" in {
    val ast = SplParser.generateTree("histogram of urls.url by week of month of timestamp using stylistpick.visitors")
    ast.isDefined should be(false)
  }
  it should "fail when the time dimension is missing" in {
    val ast = SplParser.generateTree("histogram of urls.url by day of month of  using stylistpick.visitors")
    ast.isDefined should be(false)
  }
  it should "fail when the first field is missing" in {
    val ast = SplParser.generateTree("histogram of by day of month of timestamp using stylistpick.visitors")
    ast.isDefined should be(false)
  }
  it should "fail when the source is missing" in {
    val ast = SplParser.generateTree("histogram of urls.url by day of month of timestamp urls.referrer using ")
    ast.isDefined should be(false)
  }

  "An absolute histogram query" should "parse correctly without a filter" in {
    val ast = SplParser.generateTree("histogram of conversiondate interval 2 days using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      AbsoluteHistogramQuery(
        None,
        UntypedField("conversiondate"),
        None,
        Day(2),
        None,
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with a filter" in {
    val ast = SplParser.generateTree("histogram of conversiondate interval 2 days where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      AbsoluteHistogramQuery(
        None,
        UntypedField("conversiondate"),
        None,
        Day(2),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with a second dimension and without a filter" in {
    val ast = SplParser.generateTree("histogram of conversionvalue over conversiondate interval 2 days using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      AbsoluteHistogramQuery(
        None,
        UntypedField("conversionvalue"),
        Some(UntypedField("conversiondate")),
        Day(2),
        None,
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with a second dimension and with a filter" in {
    val ast = SplParser.generateTree("histogram of conversionvalue over conversiondate interval 2 days where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      AbsoluteHistogramQuery(
        None,
        UntypedField("conversionvalue"),
        Some(UntypedField("conversiondate")),
        Day(2),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with an otion string" in {
    val ast = SplParser.generateTree("""histogram /*+ "key_field_script":"doc['f'].value / 2" */ of conversionvalue over conversiondate interval 2 days where conversionflag = true using stylistpick.visitors""")
    ast.isDefined should be(true)
    ast.get should equal(
      AbsoluteHistogramQuery(
        Some(""""key_field_script":"doc['f'].value / 2""""),
        UntypedField("conversionvalue"),
        Some(UntypedField("conversiondate")),
        Day(2),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with the singular form of interval" in {
    val ast = SplParser.generateTree("histogram of conversionvalue over conversiondate interval 1 week where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      AbsoluteHistogramQuery(
        None,
        UntypedField("conversionvalue"),
        Some(UntypedField("conversiondate")),
        Week(1),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with floating point interval" in {
    val ast = SplParser.generateTree("histogram of conversionvalue over conversiondate interval 0.6 months where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      AbsoluteHistogramQuery(
        None,
        UntypedField("conversionvalue"),
        Some(UntypedField("conversiondate")),
        Month(0.6f),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with a numeric interval" in {
    val ast = SplParser.generateTree("histogram of conversionvalue over visits interval 6 where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      AbsoluteHistogramQuery(
        None,
        UntypedField("conversionvalue"),
        Some(UntypedField("visits")),
        Numeric(6),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "fail to parse when the interval keyword is missing" in {
    val ast = SplParser.generateTree("histogram of conversionvalue over visits 6 days where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(false)
  }
  it should "fail to parse when the source is missing" in {
    val ast = SplParser.generateTree("histogram of conversionvalue over visits interval 6 days where conversionflag = true")
    ast.isDefined should be(false)
  }
  it should "fail to parse when the interval is non numeric" in {
    val ast = SplParser.generateTree("histogram of conversionvalue over visits interval ten days where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(false)
  }

  /* =========================== Stats =========================== */
  "A stats query" should "parse correctly without a filter" in {
    val ast = SplParser.generateTree("stats of conversionvalue using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      StatsQuery(
        None,
        List(UntypedField("conversionvalue")),
        None,
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with a filter" in {
    val ast = SplParser.generateTree("stats of conversionvalue where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      StatsQuery(
        None,
        List(UntypedField("conversionvalue")),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with multiple fields" in {
    val ast = SplParser.generateTree("stats of conversionvalue,firstsearch,lastreferrer where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      StatsQuery(
        None,
        List(UntypedField("conversionvalue"), UntypedField("firstsearch"), UntypedField("lastreferrer")),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with an option string" in {
    val ast = SplParser.generateTree("""stats /*+ "key_field_script":"doc['f'].value / 2" */ of conversionvalue,firstsearch,lastreferrer where conversionflag = true using stylistpick.visitors""")
    ast.isDefined should be(true)
    ast.get should equal(
      StatsQuery(
        Some(""""key_field_script":"doc['f'].value / 2""""),
        List(UntypedField("conversionvalue"), UntypedField("firstsearch"), UntypedField("lastreferrer")),
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }
  it should "fail to parse when no fields are defined" in {
    val ast = SplParser.generateTree("stats of where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(false)
  }
  it should "fail to parse when no source is defined" in {
    val ast = SplParser.generateTree("stats of urls.url where conversionflag = true using ")
    ast.isDefined should be(false)
  }

  /* =========================== Count =========================== */
  "A count query" should "parse correctly without a filter" in {
    val ast = SplParser.generateTree("count records using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      CountQuery(
        None,
        DataSource("stylistpick.visitors")))
  }
  it should "parse correctly with a filter" in {
    val ast = SplParser.generateTree("count records where conversionflag = true using stylistpick.visitors")
    ast.isDefined should be(true)
    ast.get should equal(
      CountQuery(
        Some(WhereClause(Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")))),
        DataSource("stylistpick.visitors")))
  }

}