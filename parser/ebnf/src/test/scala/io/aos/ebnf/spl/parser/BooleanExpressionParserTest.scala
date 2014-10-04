package io.aos.ebnf.spl.parser

import io.aos.ebnf.spl.parser.SplParser;
import io.aos.ebnf.spl.ast._

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class BooleanExpressionParserTest extends FlatSpec with ShouldMatchers {

  "A single condition" should "generate a Condition class" in {
    val ast = SplParser.generateBooleanExprTree("field = true")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("=", "true")))
  }

  "A single condition with brackets" should "still generate a Condition class" in {
    val ast = SplParser.generateBooleanExprTree("(field = true)")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("=", "true")))
  }

  "A simple and condition" should "generate an AndOperator class" in {
    val ast = SplParser.generateBooleanExprTree("f1 = 'v1' and f2 = 'v2'")
    ast.isDefined should be(true)
    ast.get should equal(AndOperator(Condition(UntypedField("f1"), ComparisonOperator("=", "v1")), Condition(UntypedField("f2"), ComparisonOperator("=", "v2"))))
  }

  "A simple or condition" should "generate an OrOperator class" in {
    val ast = SplParser.generateBooleanExprTree("f1 = 'v1' or f2 = 'v2'")
    ast.isDefined should be(true)
    ast.get should equal(OrOperator(Condition(UntypedField("f1"), ComparisonOperator("=", "v1")), Condition(UntypedField("f2"), ComparisonOperator("=", "v2"))))
  }

  "A simple and not condition" should "generate an AndNotOperator class" in {
    val ast = SplParser.generateBooleanExprTree("f1 = 'v1' and not(f2 = 'v2')")
    ast.isDefined should be(true)
    ast.get should equal(AndNotOperator(Condition(UntypedField("f1"), ComparisonOperator("=", "v1")), Condition(UntypedField("f2"), ComparisonOperator("=", "v2"))))
  }

  "A simple or not condition" should "generate an OrNotOperator class" in {
    val ast = SplParser.generateBooleanExprTree("f1 = 'v1' or not(f2 = 'v2')")
    ast.isDefined should be(true)
    ast.get should equal(OrNotOperator(Condition(UntypedField("f1"), ComparisonOperator("=", "v1")), Condition(UntypedField("f2"), ComparisonOperator("=", "v2"))))
  }

  "Nested conditions" should "generate a nested class tree" in {
    val ast = SplParser.generateBooleanExprTree("f1 = 'v1' and not(f2 = 'v2' or f3 > 25)")
    ast.isDefined should be(true)
    ast.get should equal(
      AndNotOperator(
        Condition(UntypedField("f1"), ComparisonOperator("=", "v1")),
        OrOperator(
          Condition(UntypedField("f2"), ComparisonOperator("=", "v2")),
          Condition(UntypedField("f3"), ComparisonOperator(">", "25")))))
  }

  "Nested conditions with mixed case keywords" should "generate a nested class tree" in {
    val ast = SplParser.generateBooleanExprTree("f1 = 'v1' AND nOt(f2 = 'v2' oR f3 > 25)")
    ast.isDefined should be(true)
    ast.get should equal(
      AndNotOperator(
        Condition(UntypedField("f1"), ComparisonOperator("=", "v1")),
        OrOperator(
          Condition(UntypedField("f2"), ComparisonOperator("=", "v2")),
          Condition(UntypedField("f3"), ComparisonOperator(">", "25")))))
  }

  "Spuriously bracketed conditions" should "still generate the correct tree" in {
    val ast = SplParser.generateBooleanExprTree("(f1 = 'v1') or not(f2 in (1,2,3) and (f3 not exists))")
    ast.isDefined should be(true)
    ast.get should equal(
      OrNotOperator(
        Condition(UntypedField("f1"), ComparisonOperator("=", "v1")),
        AndOperator(
          Condition(UntypedField("f2"), InOperator(Seq("1", "2", "3"), false)),
          Condition(UntypedField("f3"), ExistsOperator(true)))))

  }

  "weirdo" should "generate a tree" in {
    val ast = SplParser.generateBooleanExprTree("(urls.pagenum > 2 AND ( urls.timestamp > '2012-05-01' AND urls.timestamp < '2012-09-26' ))")

    ast.isDefined should be(true)
  }

  "Unknown operator" should "fail to produce a tree" in {
    val ast = SplParser.generateBooleanExprTree("f1 <> 'v1' and not(f2 = 'v2')")
    ast.isDefined should be(false)
  }

  "Mismatched brackets" should "fail to produce a tree" in {
    val ast = SplParser.generateBooleanExprTree("(f1 = 'v1') or not((f2 != 'v2') and (f3 < 25)")
    ast.isDefined should be(false)
  }

  "Unclosed string literal" should "fail to produce a tree" in {
    val ast = SplParser.generateBooleanExprTree("f1 = 'v1 and not(f2 = 'v2')")
    ast.isDefined should be(false)
  }

  "Unquoted string literal" should "fail to produce a tree" in {
    val ast = SplParser.generateBooleanExprTree("f1 = v1 and not(f2 = 'v2')")
    ast.isDefined should be(false)
  }

  "Dashes in field name" should "produce a tree" in {
    val ast = SplParser.generateBooleanExprTree("my-field = 'v1'")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("my-field"), ComparisonOperator("=", "v1")))
  }

  "Underscores in field name" should "produce a tree" in {
    val ast = SplParser.generateBooleanExprTree("my_field = 'v1'")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("my_field"), ComparisonOperator("=", "v1")))
  }

  "Numbers in field name" should "produce a tree" in {
    val ast = SplParser.generateBooleanExprTree("myfield_01 = 'v1'")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("myfield_01"), ComparisonOperator("=", "v1")))
  }

  /* =========================== Operators =========================== */

  "Operator parsing" should "recognise an equals operator" in {
    val ast = SplParser.generateBooleanExprTree("field=true")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("=", "true")))
  }
  it should "recognise a not equals operator" in {
    val ast = SplParser.generateBooleanExprTree("field!=true")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("!=", "true")))
  }
  it should "recognise a greater than operator" in {
    val ast = SplParser.generateBooleanExprTree("field>25")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator(">", "25")))
  }
  it should "recognise a greater than or equal to operator" in {
    val ast = SplParser.generateBooleanExprTree("field>=25")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator(">=", "25")))
  }
  it should "recognise a less than operator" in {
    val ast = SplParser.generateBooleanExprTree("field<25")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("<", "25")))
  }
  it should "recognise a less than or equal to operator" in {
    val ast = SplParser.generateBooleanExprTree("field<=25")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("<=", "25")))
  }
  it should "recognise an in clause" in {
    val ast = SplParser.generateBooleanExprTree("(field IN ('a','b','c'))")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), InOperator(Seq("a", "b", "c"), false)))
  }
  it should "recognise a negated in clause" in {
    val ast = SplParser.generateBooleanExprTree("(field NOT IN ('a','b','c'))")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), InOperator(Seq("a", "b", "c"), true)))
  }
  it should "recognise an exists clause" in {
    val ast = SplParser.generateBooleanExprTree("(field EXISTS)")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ExistsOperator(false)))
  }
  it should "recognise a negated exists" in {
    val ast = SplParser.generateBooleanExprTree("(field NOT EXISTS)")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ExistsOperator(true)))
  }

  /* =========================== Values =========================== */
  "Value parsing" should "recognise a string literal" in {
    val ast = SplParser.generateBooleanExprTree("field = 'some string goes here'")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("=", "some string goes here")))
  }
  it should "recognise a string literal with numbers" in {
    val ast = SplParser.generateBooleanExprTree("field = 'df59834983someB0085'")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("=", "df59834983someB0085")))
  }
  it should "recognise a string containing escaped characters" in {
    val ast = SplParser.generateBooleanExprTree("""field = 'some \n string containing \t crap'""")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("=", "some \n string containing \t crap")))
  }
  it should "recognise a string containing an escaped quote" in {
    val ast = SplParser.generateBooleanExprTree("""field = 'behold! \'tis I with stern accismus for callipygian delights'""")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("=", """behold! 'tis I with stern accismus for callipygian delights""")))
  }
  it should "recognise a date" in {
    val ast = SplParser.generateBooleanExprTree("field = '2012-05-01'")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("=", "2012-05-01")))
  }
  it should "recognise a string with punctuation characters" in {
    val ast = SplParser.generateBooleanExprTree("field = '111.67.234.33/search/srpcache?some_param=value:x;;@'")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("=", "111.67.234.33/search/srpcache?some_param=value:x;;@")))
  }
  it should "recognise an integer" in {
    val ast = SplParser.generateBooleanExprTree("field = 34839542")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("=", "34839542")))
  }
  it should "recognise a floating point number" in {
    val ast = SplParser.generateBooleanExprTree("field = 3483.9542")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("=", "3483.9542")))
  }
  it should "not recognise a number with multiple periods" in {
    val ast = SplParser.generateBooleanExprTree("field = 3483.95.42")
    ast.isDefined should be(false)
  }
  it should "recognise a boolean true" in {
    val ast = SplParser.generateBooleanExprTree("field = true")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("=", "true")))
  }
  it should "recognise a boolean true in mixed case" in {
    val ast = SplParser.generateBooleanExprTree("field = tRUe")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("=", "tRUe")))
  }
  it should "recognise a boolean false" in {
    val ast = SplParser.generateBooleanExprTree("field = false")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("=", "false")))
  }
  it should "recognise a boolean false in mixed case" in {
    val ast = SplParser.generateBooleanExprTree("field = fALse")
    ast.isDefined should be(true)
    ast.get should equal(Condition(UntypedField("field"), ComparisonOperator("=", "fALse")))
  }

}