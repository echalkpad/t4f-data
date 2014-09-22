package io.aos.parser.ebnf.spl.semantic

import io.aos.parser.ebnf.spl.semantic.DataSourceMetadata;
import io.aos.parser.ebnf.spl.semantic.SplSemanticAnalyzer;

import org.scalatest.junit.ShouldMatchersForJUnit
import org.scalatest.junit.JUnitSuite
import org.junit.Test

import io.aos.parser.ebnf.spl.ast._
import io.aos.parser.ebnf.spl.parser.SplParser

import org.junit.Before

class BugTest extends JUnitSuite with ShouldMatchersForJUnit {

  @Before def setup() {
    DataSourceMetadata.registerUpdater(OfflineDataSourceMetadata.load)
  }

  @Test def testComplexWhere() {
    val ast = SplParser.generateTree("SELECT visitorid,conversionvisits.conversionvalue,urls.URL WHERE (conversionflag NOT EXISTS) AND (urls.URL IN ('url') OR NOT urls.timestamp > '2012-02-01') USING customer2.visitors")
    val annotatedAst = SplSemanticAnalyzer.buildAnnotatedSyntaxTree(ast.get)

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

}