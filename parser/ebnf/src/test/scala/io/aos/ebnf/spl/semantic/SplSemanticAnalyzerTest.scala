package io.aos.ebnf.spl.semantic

import scala.collection.immutable.List

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import io.aos.ebnf.spl.ast.{ DayOfWeek, _ }


import io.aos.ebnf.spl.semantic.DataSourceMetadata;
import io.aos.ebnf.spl.semantic.SplSemanticAnalyzer;
import io.aos.ebnf.spl.ast.QueryAnnotation._
import io.aos.ebnf.spl.parser.SplParser
import io.aos.ebnf.spl.backend.BackendType

class SplSemanticAnalyzerTest extends FlatSpec with ShouldMatchers {

  DataSourceMetadata.registerUpdater(OfflineDataSourceMetadata.load)

  "A desc query AST" should "produce a typed AST if the data source exists" in {
    val aast = SplSemanticAnalyzer.annotate(DescQuery("customer2.visitors"))

    aast.isRight should be(true)
    aast.right.get should equal(DescQuery("customer2-visitors"))
  }
  it should "produce a typed AST with a mixed case data source name" in {
    val aast = SplSemanticAnalyzer.annotate(DescQuery("customer2.viSITOrs"))

    aast.isRight should be(true)
    aast.right.get should equal(DescQuery("customer2-visitors"))
  }
  it should "fail when the data source does not exist" in {
    val aast = SplSemanticAnalyzer.annotate(DescQuery("children.viSITOrs"))

    aast.isRight should be(false)
  }

  "A select query AST" should "produce a typed AST for simple query" in {
    val ast = SplParser.generateTree("WHERE conversionflag = true USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      SelectQuery(None, WhereClause(
        TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "true"), None)), DataSource("customer2-visitors")))
  }
  it should "produce a typed AST for field list and where clause" in {
    val ast = SplParser.generateTree("SELECT visitorid,conversionvisits.conversionvalue,urls.URL WHERE conversionflag = true USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      SelectQuery(
        Some(List(TypedField("visitorid", DataType.StringType), TypedField("conversionvisits.conversionvalue", DataType.DoubleType, Some("conversionvisits")), TypedField("urls.URL", DataType.StringType, Some("urls")))),
        WhereClause(TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "true"), None)),
        DataSource("customer2-visitors")))
  }
  it should "produce a typed AST for dashed datasource name" in {
    val ast = SplParser.generateTree("SELECT visitorid,conversionvisits.conversionvalue,urls.URL WHERE conversionflag = true USING customer3.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      SelectQuery(
        Some(List(TypedField("visitorid", DataType.StringType), TypedField("conversionvisits.conversionvalue", DataType.DoubleType, Some("conversionvisits")), TypedField("urls.URL", DataType.StringType, Some("urls")))),
        WhereClause(TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "true"), None)),
        DataSource("customer3-visitors")))
  }
  it should "produce a typed AST for single element field list and where clause" in {
    val ast = SplParser.generateTree("SELECT visitorid WHERE conversionflag = true USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      SelectQuery(
        Some(List(TypedField("visitorid", DataType.StringType))),
        WhereClause(TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "true"), None)),
        DataSource("customer2-visitors")))
  }
  it should "produce a typed AST for complex where clause" in {
    val ast = SplParser.generateTree("SELECT visitorid,conversionvisits.conversionvalue,urls.URL WHERE (conversionflag NOT EXISTS) AND (urls.URL IN ('url') OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)

    val q = annotatedAst.right.get

    q should equal(
      SelectQuery(
        Some(List(TypedField("visitorid", DataType.StringType), TypedField("conversionvisits.conversionvalue", DataType.DoubleType, Some("conversionvisits")), TypedField("urls.URL", DataType.StringType, Some("urls")))),
        WhereClause(
          TypedAndOperator(
            TypedCondition(TypedField("conversionflag", DataType.BooleanType), ExistsOperator(true), None),
            TypedOrNotOperator(
              TypedCondition(TypedField("urls.URL", DataType.StringType, Some("urls")), InOperator(List("url"), false), None),
              TypedCondition(TypedField("urls.timestamp", DataType.DateType, Some("urls")), ComparisonOperator(">", "2012-02-01"), None),
              Some("urls")),
            None)),
        DataSource("customer2-visitors")))
  }
  it should "fail when data source does not exist" in {
    val ast = SplParser.generateTree("SELECT visitorid,conversionvisits.conversionvalue,urls.URL WHERE conversionflag = true USING children.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when field is unknown in field list" in {
    val ast = SplParser.generateTree("SELECT visitorid,conversion.conversionvalue,urls.URL WHERE conversionflag = true USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when field is unknown in where clause" in {
    val ast = SplParser.generateTree("SELECT visitorid,conversionvisits.conversionvalue,urls.URL WHERE conversion = true USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }

  "A top n query AST" should "produce a typed AST from a simple query" in {
    val ast = SplParser.generateTree("TOP 20 urls.URL USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)
    if (annotatedAst.isLeft) {
      println(annotatedAst.left.get)
    }
    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      TopNQuery(None, 20, TypedField("urls.URL", DataType.StringType, Some("urls")), None, None, DataSource("customer2-visitors")))
  }
  it should "produce a typed AST from an ordered query" in {
    val ast = SplParser.generateTree("TOP 20 visitorid BY COUNT USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)
    if (annotatedAst.isLeft) {
      println(annotatedAst.left.get)
    }
    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      TopNQuery(None, 20, TypedField("visitorid", DataType.StringType), Some(ByCount()), None, DataSource("customer2-visitors")))
  }
  it should "produce a typed AST from a query with complex where clause" in {
    val ast = SplParser.generateTree("TOP 20 visitorid BY COUNT WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)
    if (annotatedAst.isLeft) {
      println(annotatedAst.left.get)
    }
    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      TopNQuery(None, 20, TypedField("visitorid", DataType.StringType), Some(ByCount()),
        Some(WhereClause(
          TypedAndOperator(
            TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "true"), None),
            TypedOrNotOperator(
              TypedCondition(TypedField("urls.URL", DataType.StringType, Some("urls")), ComparisonOperator("=", "url"), None),
              TypedCondition(TypedField("urls.timestamp", DataType.DateType, Some("urls")), ComparisonOperator(">", "2012-02-01"), None),
              Some("urls")),
            None))),
        DataSource("customer2-visitors")))
  }
  it should "produce a typed AST from a BottomN query with complex where clause" in {
    val ast = SplParser.generateTree("BOTTOM 20 visitorid BY COUNT WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)
    if (annotatedAst.isLeft) {
      println(annotatedAst.left.get)
    }
    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      BottomNQuery(
        None,
        20,
        TypedField("visitorid", DataType.StringType),
        Some(ByCount()),
        Some(WhereClause(
          TypedAndOperator(
            TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "true"), None),
            TypedOrNotOperator(
              TypedCondition(TypedField("urls.URL", DataType.StringType, Some("urls")), ComparisonOperator("=", "url"), None),
              TypedCondition(TypedField("urls.timestamp", DataType.DateType, Some("urls")), ComparisonOperator(">", "2012-02-01"), None),
              Some("urls")),
            None))),
        DataSource("customer2-visitors")))
  }
  it should "fail when the data source is unknown" in {
    val ast = SplParser.generateTree("TOP 20 visitorid BY COUNT WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when a field is unknown in the where clause" in {
    val ast = SplParser.generateTree("TOP 20 visitorid BY COUNT WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT url.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when the field is unknown" in {
    val ast = SplParser.generateTree("TOP 20 visitor BY COUNT WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when the ordering is numeric and the field is non numeric" in {
    val ast = SplParser.generateTree("TOP 20 visitorid BY MEAN WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }

  "A range query AST" should "produce a typed AST from simple range query" in {
    val ast = SplParser.generateTree("RANGE(..10,20) OVER maxvisitnum USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      RangeQuery(
        None,
        List(RangeDef(None, Some(10)), RangeDef(Some(20), None)),
        TypedField("maxvisitnum", DataType.LongType),
        None,
        None,
        DataSource("customer2-visitors")))
  }
  it should "produce a typed AST from two dimensional range query" in {
    val ast = SplParser.generateTree("RANGE(..10,20) OVER maxvisitnum OF totallifetimeconversionvalue USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      RangeQuery(
        None,
        List(RangeDef(None, Some(10)), RangeDef(Some(20), None)),
        TypedField("maxvisitnum", DataType.LongType),
        Some(TypedField("totallifetimeconversionvalue", DataType.DoubleType, None)),
        None,
        DataSource("customer2-visitors")))
  }
  it should "produce a typed AST from range query with a where clause" in {
    val ast = SplParser.generateTree("RANGE(..10,20) OVER maxvisitnum OF totallifetimeconversionvalue WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      RangeQuery(
        None,
        List(RangeDef(None, Some(10)), RangeDef(Some(20), None)),
        TypedField("maxvisitnum", DataType.LongType),
        Some(TypedField("totallifetimeconversionvalue", DataType.DoubleType, None)),
        Some(WhereClause(
          TypedAndOperator(
            TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "true"), None),
            TypedOrNotOperator(
              TypedCondition(TypedField("urls.URL", DataType.StringType, Some("urls")), ComparisonOperator("=", "url"), None),
              TypedCondition(TypedField("urls.timestamp", DataType.DateType, Some("urls")), ComparisonOperator(">", "2012-02-01"), None),
              Some("urls")),
            None))),
        DataSource("customer2-visitors")))
  }
  it should "produce a typed AST regardless of case" in {
    val ast = SplParser.generateTree("RANGE(..10,20) OVER maxvisitnum OF totallifetimeconversionvalue WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer4.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      RangeQuery(
        None,
        List(RangeDef(None, Some(10)), RangeDef(Some(20), None)),
        TypedField("maxvisitnum", DataType.LongType),
        Some(TypedField("totallifetimeconversionvalue", DataType.DoubleType, None)),
        Some(WhereClause(
          TypedAndOperator(
            TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "true"), None),
            TypedOrNotOperator(
              TypedCondition(TypedField("urls.URL", DataType.StringType, Some("urls")), ComparisonOperator("=", "url"), None),
              TypedCondition(TypedField("urls.timestamp", DataType.DateType, Some("urls")), ComparisonOperator(">", "2012-02-01"), None),
              Some("urls")),
            None))),
        DataSource("customer2-visitors")))
  }
  it should "fail when the data source is unknown" in {
    val ast = SplParser.generateTree("RANGE(..10,20) OVER maxvisitnum OF totallifetimeconversionvalue WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING children.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when a field is unknown" in {
    val ast = SplParser.generateTree("RANGE(..10,20) OVER maxvisitnum OF totallifetimeconversion WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when the field data type is non numeric" in {
    val ast = SplParser.generateTree("RANGE(..10,20) OVER maxvisitnum OF urls.URL WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }

  "A pivot query AST" should "produce a typed AST from simple query" in {
    val ast = SplParser.generateTree("PIVOT urls.URL ON conversionflag USING customer4.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      PivotQuery(
        None,
        None,
        TypedField("urls.URL", DataType.StringType, Some("urls")),
        TypedField("conversionflag", DataType.BooleanType),
        None,
        DataSource("customer2-visitors")))
  }
  it should "produce a typed AST from a complex query" in {
    val ast = SplParser.generateTree("PIVOT TOP 50 urls.URL ON conversionflag WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer4.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      PivotQuery(
        None,
        Some(TopLimit(50)),
        TypedField("urls.URL", DataType.StringType, Some("urls")),
        TypedField("conversionflag", DataType.BooleanType),
        Some(WhereClause(
          TypedAndOperator(
            TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "true"), None),
            TypedOrNotOperator(
              TypedCondition(TypedField("urls.URL", DataType.StringType, Some("urls")), ComparisonOperator("=", "url"), None),
              TypedCondition(TypedField("urls.timestamp", DataType.DateType, Some("urls")), ComparisonOperator(">", "2012-02-01"), None),
              Some("urls")),
            None))),
        DataSource("customer2-visitors")))
  }
  it should "fail when the data source is unknown" in {
    val ast = SplParser.generateTree("PIVOT TOP 50 urls.URL ON conversionflag WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING children.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when the dimension is unknown" in {
    val ast = SplParser.generateTree("PIVOT TOP 50 url ON conversionflag WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when a field in the where clause is unknown" in {
    val ast = SplParser.generateTree("PIVOT TOP 50 urls.URL ON conversionflag WHERE (conversionflag = true) AND (URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }

  "A relative histogram AST" should "produce a typed AST from simple query" in {
    val ast = SplParser.generateTree("HISTOGRAM OF numconvertingvisits BY DAY OF WEEK OF timestamp USING customer4.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      RelativeHistogramQuery(
        None,
        TypedField("numconvertingvisits", DataType.LongType),
        DayOfWeek(),
        TypedField("timestamp", DataType.DateType),
        None,
        DataSource("customer2-visitors")))
  }
  it should "produce a typed AST from complex query" in {
    val ast = SplParser.generateTree("HISTOGRAM OF numconvertingvisits BY DAY OF WEEK OF timestamp WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer4.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      RelativeHistogramQuery(
        None,
        TypedField("numconvertingvisits", DataType.LongType),
        DayOfWeek(),
        TypedField("timestamp", DataType.DateType),
        Some(WhereClause(
          TypedAndOperator(
            TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "true"), None),
            TypedOrNotOperator(
              TypedCondition(TypedField("urls.URL", DataType.StringType, Some("urls")), ComparisonOperator("=", "url"), None),
              TypedCondition(TypedField("urls.timestamp", DataType.DateType, Some("urls")), ComparisonOperator(">", "2012-02-01"), None),
              Some("urls")),
            None))),
        DataSource("customer2-visitors")))
  }
  it should "fail when the time dimension is not a date type" in {
    val ast = SplParser.generateTree("HISTOGRAM OF numconvertingvisits BY day of week OF urls.URL WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when the dimension is non numeric" in {
    val ast = SplParser.generateTree("HISTOGRAM OF urls.URL BY day of week OF timestamp WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when the data source is unknown" in {
    val ast = SplParser.generateTree("HISTOGRAM OF urls.URL BY day of week OF timestamp WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING children.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when a where clause field is unknown" in {
    val ast = SplParser.generateTree("HISTOGRAM OF urls.URL BY day of week OF timetamp WHERE (conversionflag = true) AND (URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when a dimension is unknown" in {
    val ast = SplParser.generateTree("HISTOGRAM OF url BY day of week OF timetamp WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }

  "An absolute histogram AST" should "produce a typed AST from simple query" in {
    val ast = SplParser.generateTree("HISTOGRAM OF conversionvisits.conversionvalue INTERVAL 2 USING customer4.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      AbsoluteHistogramQuery(
        None,
        TypedField("conversionvisits.conversionvalue", DataType.DoubleType, Some("conversionvisits")),
        None,
        Numeric(2),
        None,
        DataSource("customer2-visitors")))
  }
  it should "produce a typed AST from complex query" in {
    val ast = SplParser.generateTree("HISTOGRAM OF conversionvisits.conversionvalue OVER  conversionvisits.timestamp INTERVAL 2 DAYS WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer4.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      AbsoluteHistogramQuery(
        None,
        TypedField("conversionvisits.conversionvalue", DataType.DoubleType, Some("conversionvisits")),
        Some(TypedField("conversionvisits.timestamp", DataType.DateType, Some("conversionvisits"))),
        Day(2),
        Some(WhereClause(
          TypedAndOperator(
            TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "true"), None),
            TypedOrNotOperator(
              TypedCondition(TypedField("urls.URL", DataType.StringType, Some("urls")), ComparisonOperator("=", "url"), None),
              TypedCondition(TypedField("urls.timestamp", DataType.DateType, Some("urls")), ComparisonOperator(">", "2012-02-01"), None),
              Some("urls")),
            None))),
        DataSource("customer2-visitors")))
  }
  it should "fail when the data source is unknown" in {
    val ast = SplParser.generateTree("HISTOGRAM OF conversionvisits.conversionvalue OVER  urls.URL INTERVAL 2 DAYS WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when a where clause field is unknown" in {
    val ast = SplParser.generateTree("HISTOGRAM OF conversionvisits.conversionvalue OVER  urls.URL INTERVAL 2 DAYS WHERE (conversionflag = true) AND (URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when a dimension is unknown" in {
    val ast = SplParser.generateTree("HISTOGRAM OF conversionvisits.conversionvalue OVER url INTERVAL 2 DAYS WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when key dimension is non numeric" in {
    val ast = SplParser.generateTree("HISTOGRAM OF conversionvisits.conversionvalue OVER urls.URL INTERVAL 2 DAYS WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when key dimension type doesn't match the interval type" in {
    val ast = SplParser.generateTree("HISTOGRAM OF conversionvisits.conversionvalue OVER conversionvisits.timestamp INTERVAL 2 WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when value dimension is non numeric" in {
    val ast = SplParser.generateTree("HISTOGRAM OF urls.URL OVER conversionvisits.timestamp INTERVAL 2 DAYS WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }

  "A stats AST" should "produce a typed AST for simple query" in {
    val ast = SplParser.generateTree("STATS OF visitorid,conversionvisits.conversionvalue,urls.URL USING customer4.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      StatsQuery(
        None,
        List(TypedField("visitorid", DataType.StringType), TypedField("conversionvisits.conversionvalue", DataType.DoubleType, Some("conversionvisits")), TypedField("urls.URL", DataType.StringType, Some("urls"))),
        None,
        DataSource("customer2-visitors")))
  }
  it should "produce a typed AST for complex query" in {
    val ast = SplParser.generateTree("STATS OF visitorid,conversionvisits.conversionvalue,urls.URL WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer4.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      StatsQuery(
        None,
        List(TypedField("visitorid", DataType.StringType), TypedField("conversionvisits.conversionvalue", DataType.DoubleType, Some("conversionvisits")), TypedField("urls.URL", DataType.StringType, Some("urls"))),
        Some(WhereClause(
          TypedAndOperator(
            TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "true"), None),
            TypedOrNotOperator(
              TypedCondition(TypedField("urls.URL", DataType.StringType, Some("urls")), ComparisonOperator("=", "url"), None),
              TypedCondition(TypedField("urls.timestamp", DataType.DateType, Some("urls")), ComparisonOperator(">", "2012-02-01"), None),
              Some("urls")),
            None))),
        DataSource("customer2-visitors")))
  }
  it should "fail when the data source is unknown" in {
    val ast = SplParser.generateTree("STATS OF visitorid,conversionvisits.conversionvalue,urls.URL WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING children.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when a where clause field is unknown" in {
    val ast = SplParser.generateTree("STATS OF visitorid,conversionvisits.conversionvalue,urls.URL WHERE (conversionflag = true) AND (URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }
  it should "fail when a field is unknown" in {
    val ast = SplParser.generateTree("STATS OF visitorid,conversionvisits.conversionvalue,url WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(false)
  }

  "A count AST" should "produce a typed AST for simple query" in {
    val ast = SplParser.generateTree("COUNT RECORDS USING customer4.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      CountQuery(
        None,
        DataSource("customer2-visitors")))
  }
  it should "produce a typed AST for complex query" in {
    val ast = SplParser.generateTree("COUNT RECORDS WHERE (conversionflag = true) AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01') USING customer4.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      CountQuery(
        Some(WhereClause(
          TypedAndOperator(
            TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "true"), None),
            TypedOrNotOperator(
              TypedCondition(TypedField("urls.URL", DataType.StringType, Some("urls")), ComparisonOperator("=", "url"), None),
              TypedCondition(TypedField("urls.timestamp", DataType.DateType, Some("urls")), ComparisonOperator(">", "2012-02-01"), None),
              Some("urls")),
            None))),
        DataSource("customer2-visitors")))
  }

  "Complicated nested queries" should "produce correst ASTs" in {
    val ast = SplParser.generateTree("COUNT RECORDS WHERE (conversionflag = true) AND (urls.URL = 'newurl' AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01')) USING customer4.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      CountQuery(
        Some(WhereClause(
          TypedAndOperator(
            TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "true"), None),
            TypedAndOperator(
              TypedCondition(TypedField("urls.URL", DataType.StringType, Some("urls")), ComparisonOperator("=", "newurl"), None),
              TypedOrNotOperator(
                TypedCondition(TypedField("urls.URL", DataType.StringType, Some("urls")), ComparisonOperator("=", "url"), None),
                TypedCondition(TypedField("urls.timestamp", DataType.DateType, Some("urls")), ComparisonOperator(">", "2012-02-01"), None),
                None), Some("urls")),
            None))),
        DataSource("customer2-visitors")))
  }
  it should "deal with intermingled nested and non nested conditions" in {
    val ast = SplParser.generateTree("COUNT RECORDS WHERE (conversionflag = true) AND (conversionflag = false AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01')) USING customer4.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get should equal(
      CountQuery(
        Some(WhereClause(
          TypedAndOperator(
            TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "true"), None),
            TypedAndOperator(
              TypedCondition(TypedField("conversionflag", DataType.BooleanType), ComparisonOperator("=", "false"), None),
              TypedOrNotOperator(
                TypedCondition(TypedField("urls.URL", DataType.StringType, Some("urls")), ComparisonOperator("=", "url"), None),
                TypedCondition(TypedField("urls.timestamp", DataType.DateType, Some("urls")), ComparisonOperator(">", "2012-02-01"), None),
                Some("urls")), None),
            None))),
        DataSource("customer2-visitors")))
  }

  "ElasticSearch queries" should "produce AST with ElasticSearch backend marker" in {
    val ast = SplParser.generateTree("COUNT RECORDS WHERE (conversionflag = true) AND (urls.URL = 'newurl' AND (urls.URL = 'url' OR NOT urls.timestamp > '2012-02-01')) USING customer4.visitors")
    val annotatedAst = SplSemanticAnalyzer.annotate(ast.get)

    annotatedAst.isRight should be(true)
    annotatedAst.right.get match {
      case q: QueryWithDataSource => q.src.backend should equal(BackendType.ElasticSearch)
      case _                      => fail("AST does not have the backend marker")
    }
  }

}
