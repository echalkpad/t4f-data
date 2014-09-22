package io.aos.parser.ebnf.spl.parser

import scala.util.parsing.combinator._
import io.aos.parser.ebnf.spl.ast._
import io.aos.parser.ebnf.spl.cache.Caching
import org.apache.commons.lang.StringEscapeUtils

/**
 * Implementation of the parser for the Spl language. See the full EBNF notation for the grammar in syntax/spl.ebnf
 */
class SplParser extends JavaTokenParsers {

  /**
   * Main parser which lists all the parsers for known query types.
   *
   * '''EBNF:'''
   * `splQuery ::= descQuery | selectQuery | topnQuery | rangeQuery | pivotQuery | histogramQuery | statsQuery`
   */
  def splQuery: Parser[SplQuery] = descQuery | selectQuery | topnQuery | rangeQuery | pivotQuery | histogramQuery | statsQuery | countQuery | failure("Unknown query")

  /**
   * Parser for a desc query. Recognises either `desc` or `describe` followed by a period delimited string.
   *
   * '''Example:'''
   * `DESCRIBE stylistpick.visitors#urls.url`
   *
   * '''EBNF:'''
   * `descQuery ::=  'desc'  dataSource [ '#' fieldName ]  | 'describe' dataSource [ '#' fieldName ]`
   */
  def descQuery: Parser[DescQuery] = regex("(?i)desc(ribe)?".r) ~> dataSource ~ opt("#" ~> fieldName) ^^ {
    case d ~ f => DescQuery(d, f)
  }

  /**
   * Parser for a select query. The `select` keyword and the field list is optional.
   *
   * '''Example:'''
   * `SELECT urls.url, firstsearch WHERE (conversionflag = true) AND (conversionvalue > 10) USING stylistpick.visitors`
   *
   *  '''EBNF:'''
   * `selectQuery ::= ('select' fieldList)? filter sourceSelector`
   */
  def selectQuery: Parser[SelectQuery] = opt(regex("(?i)select".r) ~> fieldList) ~ filter ~ sourceSelector ^^ {
    case fl ~ f ~ s => SelectQuery(fl, f, s)
  }

  /**
   * Parser for the data source selector.
   *
   * '''Example:'''
   * `... USING stylistpick.visitors`
   *
   * '''EBNF:'''
   * `sourceSelector ::= 'using' dataSource`
   */
  def sourceSelector: Parser[DataSource] = regex("(?i)using".r) ~> dataSource ^^ {
    DataSource(_)
  }

  /**
   * Parser for a comma separated list of fields as used in a select query.
   *
   * '''Example:'''
   * `... field1, field2, field3.subfield ...`
   *
   * '''EBNF:'''
   * `fieldList ::= fieldName (',' fieldName)*`
   */
  def fieldList: Parser[List[Field]] = repsep(fieldName, ",")

  /**
   * Parser for a field name. A valid field name is an alphanumeric string delimited by periods.
   *
   * '''Example:'''
   * `urls.url`
   *
   * '''EBNF:'''
   * `fieldName ::= [a-zA-Z0-9_] ('.' [a-zA-Z0-9_])*`
   */
  def fieldName: Parser[Field] = repsep(regex("[\\w\\-]+".r), ".") ^^ {
    case f => UntypedField(f.mkString("."))
  }

  /**
   * Parser for a data source name. A valid data source is an alphanumeric string delimited by periods.
   *
   * '''Example:'''
   * `stylistpick.visitors`
   *
   * '''EBNF:'''
   * `dataSource ::= [a-zA-Z0-9_] ('.' [a-zA-Z0-9_])*`
   */
  def dataSource: Parser[String] = rep1sep(regex("[\\w\\-]+".r), ".") ^^ {
    _.mkString(".")
  }

  /**
   * over visits interval 6 where conversionflag = true
   * Parser for a filter. A filter start with the word `where` and includes a list of conditions delimited by boolean operators.
   *
   * '''Example:'''
   * `WHERE f1 = true AND NOT(f2 > 5 OR f3 < 10)`
   *
   * '''EBNF:'''
   * `filter ::= 'where' booleanExpression`
   */
  def filter: Parser[WhereClause] = regex("(?i)where".r) ~> booleanExpression ^^ {
    WhereClause(_)
  }

  /**
   * Parser for a boolean expression. A boolean expression consists of one or more conditions delimited by boolean operators (`and`, `or`).
   *
   * '''Example:'''
   * `f1 = true AND NOT(f2 > 5 OR f3 < 10)`
   *
   * '''EBNF:'''
   * `booleanExpression ::= booleanTerm (andTerm | orTerm)*`
   */
  def booleanExpression: Parser[BooleanExpression] = booleanTerm ~ rep(andTerm | orTerm) ^^ {
    case b ~ lst => (b /: lst) {
      case (lhs, AndTerm(rhs))    => AndOperator(lhs, rhs)
      case (lhs, OrTerm(rhs))     => OrOperator(lhs, rhs)
      case (lhs, AndNotTerm(rhs)) => AndNotOperator(lhs, rhs)
      case (lhs, OrNotTerm(rhs))  => OrNotOperator(lhs, rhs)
    }
  }

  /**
   * Parser for a boolean term. A boolean term is either a single condition or a bracketed boolean expression.
   *
   * '''Example:'''over visits interval 6 where conversionflag = true
   * `(f1 = true AND f2 = 10)`
   *
   * '''EBNF:'''
   * `booleanTerm ::=  condition | '(' booleanExpression ')'`
   */
  def booleanTerm: Parser[BooleanExpression] = condition | "(" ~> booleanExpression <~ ")"

  /**
   * Parser for an AND term. An AND term consists of `and` followed by a boolean term - optionally prefixed by `not`
   *
   * '''Example:'''
   * `... and not (f1 > 5)`
   *
   * '''EBNF:```
   * `'and' 'not'? booleanTerm`
   */
  def andTerm = regex("(?i)and".r) ~ opt(regex("(?i)not".r)) ~ booleanTerm ^^ {
    case _ ~ Some(_) ~ b => AndNotTerm(b)
    case _ ~ None ~ b    => AndTerm(b)
  }

  /**
   * Parser for an OR term. An OR term consists of `or` followed by a boolean term - optionally prefixed by `not`
   *
   * '''Example:'''
   * `... or not (f1 > 5)`
   *
   * '''EBNF:```
   * `'or' 'not'? booleanTerm`
   */
  def orTerm = regex("(?i)or".r) ~ opt(regex("(?i)not".r)) ~ booleanTerm ^^ {
    case _ ~ Some(_) ~ b => OrNotTerm(b)
    case _ ~ None ~ b    => OrTerm(b)
  }

  /**
   * Parser for a condition. A condition is a comparison optionally enclosed in brackets.
   *
   * '''Example:'''
   * `(field = 'value')`
   *
   * '''EBNF:'''
   * `condition ::= fieldName (comparisonOperator | inOperator | existsOperator) | '(' condition ')'`
   */
  def condition: Parser[Condition] = (
    fieldName ~ (comparisonOperator | inOperator | existsOperator | failure("Unknown operator")) ^^ { case f ~ c => Condition(f, c) }
    | "(" ~> condition <~ ")"
    | failure("Invalid condition"))

  /**
   * Parser for comparison operators.
   *
   * '''Example:'''
   * `... > ...`
   *
   * '''EBNF:'''
   * `comparisonOperator ::= ('=' | '!=' | '<=' | '>=' | '<' | '>') value`
   */
  def comparisonOperator: Parser[ComparisonOperator] = ("=" | "!=" | "<=" | ">=" | "<" | ">") ~ value ^^ {
    case op ~ v => ComparisonOperator(op, v)
  }

  /**
   * Parser for IN operator.
   *
   * '''EBNF:'''
   * `inOperator ::= 'not'? 'in' '(' value (',' value)* ')'`
   */
  def inOperator: Parser[InOperator] = opt(regex("(?i)not".r)) ~ regex("(?i)in".r) ~ ("(" ~> rep1sep(value, ",") <~ ")") ^^ {
    case Some(n) ~ in ~ v => InOperator(v.toSeq, true)

    case None ~ in ~ v    => InOperator(v.toSeq, false)
  }

  /**
   * Parser for EXISTS operator.
   *
   * '''EBNF:'''
   * `existsOperator ::= 'not'? 'exists'`
   */
  def existsOperator: Parser[ExistsOperator] = opt(regex("(?i)not".r)) ~ regex("(?i)exists".r) ^^ {
    case Some(n) ~ e => ExistsOperator(true)

    case None ~ e    => ExistsOperator(false)
  }

  /**
   * Parser for values. Values are either strings, numbers or boolean true/false literals.
   *
   * '''Example:'''
   * `... 3.14 ...`
   *
   * '''EBNF:'''
   * {{{
   * value ::= stringLiteral | numberLiteral | booleanLiteral
   * stringLiteral ::= "'" string "'"
   * numberLiteral ::= integer | floatingPointNumber
   * booleanLiteral ::= 'true' | 'false'
   * }}}
   */
  def value: Parser[String] = (
    //"'" ~> regex("""[\w\s\-\.\/]*""".r) <~ "'" ^^ { _.toString() }
    "'" ~ regex("""[^'\\]*(?:\\.[^'\\]*)*""".r) ~ "'" ^^ { case q1 ~ v ~ q2 => StringEscapeUtils.unescapeJava(v) }
    | floatingPointNumber ^^ { _.toString() }
    | regex("(?i)true".r)
    | regex("(?i)false".r)
    | failure("Invalid value. Must be a string, number, true or false"))

  /**
   * Parser for top-N or bottom-N queries.
   *
   * '''Example:'''
   * `TOP 20 urls.url BY MEAN WHERE conversionflag = true USING stylistpick.visitor`
   *
   * '''EBNF:'''
   * `topnQuery ::= ('top'|'bottom') optionList? integer fieldName ordering? filter? sourceSelector`
   *
   */
  def topnQuery: Parser[SplQuery] = (regex("(?i)top".r) | regex("(?i)bottom".r)) ~ opt(optionList) ~ wholeNumber ~ fieldName ~ opt(ordering) ~ opt(filter) ~ sourceSelector ^^ {
    case topOrBottom ~ optList ~ number ~ field ~ order ~ filterDef ~ src => {
      if (topOrBottom.equalsIgnoreCase("top")) {
        TopNQuery(optList, number.toInt, field, order, filterDef, src)
      }
      else {
        BottomNQuery(optList, number.toInt, field, order, filterDef, src)
      }
    }
  }

  /**
   * Parser for option lists. An option list contains the arguments to the underlying query engine.
   *
   * '''Example:'''
   * `("key_field_script": "doc['field'].value")`
   *
   * '''EBNF:'''
   * `optionList ::= '/*+' string '*/'`
   */
  def optionList: Parser[String] = regex("/\\*\\+.*\\*/".r) ^^ {
    _.replaceFirst("/\\*\\+", "").replaceFirst("\\*/", "").trim()
  }

  /**
   * Parser for ordering options.
   *
   * '''Example:'''
   * `BY MEAN`
   *
   * '''EBNF:'''
   * `ordering ::= 'by' ('count' | 'mean' | 'max' | 'min' | 'total' | 'term')`
   */
  def ordering: Parser[Ordering] = regex("(?i)by".r) ~> (regex("(?i)count".r) | regex("(?i)mean".r) | regex("(?i)max".r) | regex("(?i)min".r) | regex("(?i)total".r) | regex("(?i)term".r) | failure("Unknown ordering")) ^^ {
    _.toLowerCase() match {
      case "count" => ByCount()
      case "mean"  => ByMean()
      case "max"   => ByMax()
      case "min"   => ByMin()
      case "total" => ByTotal()
      case "term"  => ByTerm()
    }
  }

  /**
   * Parser for range queries.
   *
   * '''Example:'''
   * `RANGE(..10, 15..30, 30..) OVER conversionvalue USING stylistpick.visitors`
   *
   * '''EBNF:'''
   * `rangeQuery ::= 'range' optionList? '(' rangeList ')' 'over' fieldName ('of' fieldName)? filter? sourceSelector`
   */
  def rangeQuery: Parser[SplQuery] = regex("(?i)range".r) ~> opt(optionList) ~ rangeList ~ regex("(?i)over".r) ~ fieldName ~ opt(regex("(?i)of".r) ~> fieldName) ~ opt(filter) ~ sourceSelector ^^ {
    case opt ~ r ~ over ~ vf ~ kf ~ f ~ s => RangeQuery(opt, r, vf, kf, f, s)
  }

  /**
   * Parser for range definition list.
   *
   * '''Example:'''
   * `(..10, 15..30, 30..)`
   *
   * '''EBNF:'''
   * `rangeList ::= rangeDef (',' rangeDef)*`
   */
  def rangeList: Parser[List[RangeDef]] = "(" ~> rep1sep(rangeDef, ",") <~ ")"

  /**
   * Parser for a single range definition.
   *
   * '''Example:'''
   * `23..34`
   *
   * '''EBNF:'''
   * `rangeDef ::= integer '..' integer | '..' integer | integer`
   */
  def rangeDef: Parser[RangeDef] = (
    wholeNumber ~ ".." ~ wholeNumber ^^ { case f ~ ".." ~ t => RangeDef(Some(f.toInt), Some(t.toInt)) }
    | wholeNumber ^^ { case f => RangeDef(Some(f.toInt), None) }
    | ".." ~ wholeNumber ^^ { case ".." ~ t => RangeDef(None, Some(t.toInt)) }
    | failure("Invalid range definition"))

  /**
   * Parser for pivot queries.
   *
   * '''Example:'''
   * `PIVOT urls.url ON conversionflag USING stylistpick.visitors`
   *
   * '''EBNF:'''
   * `pivotQuery ::= 'pivot' optionList? dimensionLimit? firstDimension 'on' secondDimension filter? sourceSelector`
   */
  def pivotQuery: Parser[SplQuery] = regex("(?i)pivot".r) ~> opt(optionList) ~ opt(dimensionLimit) ~ fieldName ~ regex("(?i)on".r) ~ fieldName ~ opt(filter) ~ sourceSelector ^^ {
    case opt ~ dimLimit ~ d1 ~ on ~ d2 ~ f ~ s => PivotQuery(opt, dimLimit, d1, d2, f, s)
  }

  /**
   * Parser for dimension limit.
   *
   * '''Example:'''
   * `top 20`
   *
   * '''EBNF:'''
   * `dimensionLimit ::= ('top' | 'bottom') integer`
   */
  def dimensionLimit: Parser[DimensionLimit] = (regex("(?i)top".r) | regex("(?i)bottom".r) | failure("Unknown limit operator")) ~ wholeNumber ^^ {
    case topOrBottom ~ number => {
      if (topOrBottom.toLowerCase() == "top") {
        TopLimit(number.toInt)
      }
      else {
        BottomLimit(number.toInt)
      }
    }
  }

  /**
   * Parser for histogram queries.
   *
   * '''Example:'''
   * {{{
   * HISTOGRAM OF conversiondate BY DAY OF WEEK USING stylistpick.visitors
   * HISTOGRAM OF conversionvalue OVER conversiondate INTERVAL 2 DAYS WHERE conversionflag = true USING stylistpick.visitors
   * }}}
   *
   * '''EBNF:'''
   * `histogramQuery ::= 'histogram' optionList? 'of' fieldName (partialRelativeHistogramDef | partialAbsoluteHistorgramDef)`
   */
  def histogramQuery: Parser[SplQuery] = regex("(?i)histogram".r) ~> opt(optionList) ~ regex("(?i)of".r) ~ fieldName ~ (partialRelativeHistogramDef | partialAbsoluteHistogramDef | failure("Invalid histogram query")) ^^ {
    case o ~ of ~ fieldOne ~ PartialRelativeHistogram(interval, timeDim, f, s)  => RelativeHistogramQuery(o, fieldOne, interval, timeDim, f, s)
    case o ~ of ~ fieldOne ~ PartialAbsoluteHistogram(fieldTwo, interval, f, s) => AbsoluteHistogramQuery(o, fieldOne, fieldTwo, interval, f, s)
  }

  /**
   * Parser for partial relative histogram definitions.
   *
   * '''Example:'''
   * `BY DAY OF WEEK USING stylistpick.visitors`
   *
   * '''EBNF:'''
   * `partialRelativeHistogramDef ::= 'by' relativeTimeInterval 'of' fieldName filter? sourceSelector`
   */
  def partialRelativeHistogramDef: Parser[PartialRelativeHistogram] = regex("(?i)by".r) ~> relativeTimeInterval ~ regex("(?i)of".r) ~ fieldName ~ opt(filter) ~ sourceSelector ^^ {
    case interval ~ of ~ timeDim ~ f ~ s => PartialRelativeHistogram(interval, timeDim, f, s)
  }

  /**
   * Parser for partial absolute histogram definitions.
   *
   * '''Example:'''
   * `OVER conversiondate INTERVAL 2 DAYS WHERE conversionflag = true USING stylistpick.visitors`
   *
   * '''EBNF:'''
   * `partialAbsoluteHistogramDef ::= ('over' fieldName)? intervalDef filter? sourceSelector`
   */
  def partialAbsoluteHistogramDef: Parser[PartialAbsoluteHistogram] = opt(regex("(?i)over".r) ~> fieldName) ~ intervalDef ~ opt(filter) ~ sourceSelector ^^ {
    case field ~ i ~ f ~ s => PartialAbsoluteHistogram(field, i, f, s)
  }

  /**
   * Parser for interval definitions.
   *
   * '''Example:'''
   * {{{
   * 12.5
   * 3 days
   * }}}
   *
   * '''EBNF:'''
   * `intervalDef ::= 'interval' numberLiteral ('year' | 'month' | 'week' | 'day' | 'hour' | 'minute')? 's'?`
   */
  def intervalDef: Parser[Interval] = regex("(?i)interval".r) ~> floatingPointNumber ~ opt(regex("(?i)year(s)?".r) | regex("(?i)month(s)?".r) | regex("(?i)week(s)?".r) | regex("(?i)day(s)?".r) | regex("(?i)hour(s)?".r) | regex("(?i)minute(s)?".r)) ^^ {
    case n ~ interval => {
      val num = n.toFloat
      if (!interval.isDefined) {
        Numeric(num)
      }
      else {
        interval.get.toLowerCase match {
          case "year" | "years"     => Year(num)
          case "month" | "months"   => Month(num)
          case "week" | "weeks"     => Week(num)
          case "day" | "days"       => Day(num)
          case "hour" | "hours"     => Hour(num)
          case "minute" | "minutes" => Minute(num)
        }
      }
    }
  }

  /**
   * Parser for relative time interval definitions.
   *
   * '''Example:'''
   * {{{
   * DAY OF MONTH
   * MINUTE OF HOUR
   * }}}
   *
   * '''EBNF:'''
   * `relativeTimeInterval ::= 'day of week' | 'day of month' | 'month of year' | 'hour of day' | 'minute of hour' | 'minute of day'`
   */
  def relativeTimeInterval: Parser[RelativeTimeInterval] = (regex("(?i)month".r) | regex("(?i)day".r) | regex("(?i)hour".r) | regex("(?i)minute".r)) ~ regex("(?i)of".r) ~ (regex("(?i)year".r) | regex("(?i)month".r) | regex("(?i)week".r) | regex("(?i)day".r) | regex("(?i)hour".r)) ^^ {
    case a ~ of ~ b => {
      val pair = (a.toLowerCase, b.toLowerCase)
      pair match {
        case ("day", "week")    => DayOfWeek()

        case ("day", "month")   => DayOfMonth()

        case ("month", "year")  => MonthOfYear()

        case ("hour", "day")    => HourOfDay()

        case ("minute", "day")  => MinuteOfDay()

        case ("minute", "hour") => MinuteOfHour()
      }
    }
  }

  /**
   * Parser for stats queries.
   *
   * '''Example:'''
   * `STATS OF pageviewspervisit,visitnumber WHERE timestamp > ‘2012-01-01’ USING stylistpick.visitors`
   *
   * '''EBNF:'''
   * `statsQuery ::= 'stats' 'of' fieldList filter? sourceSelector`
   */
  def statsQuery: Parser[SplQuery] = regex("(?i)stats".r) ~> opt(optionList) ~ regex("(?i)of".r) ~ fieldList ~ opt(filter) ~ sourceSelector ^^ {
    case o ~ of ~ fl ~ f ~ s => StatsQuery(o, fl, f, s)
  }

  /**
   * Parser for count queries.
   *
   * '''Example:'''
   * `COUNT WHERE EXISTS conversions USING stylistpick.visitors`
   *
   * '''EBNF:'''
   * `countQuery ::= 'count filter sourceSelector`
   *
   */
  def countQuery: Parser[SplQuery] = regex("(?i)count".r) ~> regex("(?i)records".r) ~ opt(filter) ~ sourceSelector ^^ {
    case r ~ f ~ s => CountQuery(f, s)
  }

}

object SplParser extends SplParser with Caching {

  lazy val cachedBuildAst = cached(buildAst)

  def buildAbstractSyntaxTree(query: String): Either[String, SplQuery] = cachedBuildAst(query)

  private[spl] def buildAst(query: String): Either[String, SplQuery] = {
    val result = parseAll(splQuery, query)
    result match {
      case Success(q, _) => Right(q)
      case Failure(m, i) => Left("Failed to parse [" + query + "] : (" + i.pos + ") " + m)
      case _             => Left("Unexpected return value from parseAll for query [" + query + "]")
    }
  }

  private[spl] def generateTree(query: String): Option[SplQuery] = {
    val result = parseAll(splQuery, query)
    result match {
      case Success(q, _) => return Some(q)
      case Failure(m, i) => {
        println("Failed to parse [" + query + "] : (" + i.pos + ") " + m)
        return None
      }
      case _ => {
        println("Unexpected return value from parseAll")
        return None
      }
    }
  }

  private[spl] def generateBooleanExprTree(expr: String): Option[BooleanExpression] = {
    val result = parseAll(booleanExpression, expr)

    if (result.successful) {
      return Some(result.get)
    }
    else {
      return None
    }
  }
}