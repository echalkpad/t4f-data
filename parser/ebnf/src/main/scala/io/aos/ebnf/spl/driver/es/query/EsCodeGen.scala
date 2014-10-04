package io.aos.ebnf.spl.driver.es.query

import scala.collection.JavaConversions.iterableAsScalaIterable

import org.elasticsearch.index.query.FilterBuilder
import org.elasticsearch.index.query.FilterBuilders.{andFilter, existsFilter, missingFilter, nestedFilter, notFilter, orFilter, queryFilter, rangeFilter, termFilter, termsFilter}
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.index.query.QueryBuilders.{boolQuery, inQuery, nestedQuery, rangeQuery, termQuery}
import org.elasticsearch.search.builder.SearchSourceBuilder

import io.aos.ebnf.spl.ast.{BooleanExpression, ComparisonOperator, DataType, ExistsOperator, Field, InOperator, SplQuery, TypedAndNotOperator, TypedAndOperator, TypedCondition, TypedField, TypedOrNotOperator, TypedOrOperator, WhereClause}
import io.aos.ebnf.spl.cache.Caching
import io.aos.ebnf.spl.driver.es.{ElasticSearchQuery, ElasticSearchQueryResult}

import grizzled.slf4j.Logging

trait EsCodeGen extends Logging with Caching {
  type T <: SplQuery

  private lazy val cachedBuildFilterFromWhereClause = cached(buildFilterFromWhereClauseImpl)
  private lazy val cachedBuildFilter = cached(buildFilter)
  private lazy val cachedBuildQueryFromWhereClause = cached(buildQueryFromWhereClauseImpl)
  private lazy val cachedBuildQuery = cached(buildQuery)

  def generate(ast: T)(execute: ElasticSearchQuery => Option[ElasticSearchQueryResult]): Either[String, ElasticSearchQueryResult]

  protected def createFilterSourceBuilder(): SearchSourceBuilder = {
    val builder = SearchSourceBuilder.searchSource()
    builder.size(0) // Disable fetching of documents by default
    return builder
  }

  protected def isNumericField(f: Field): Boolean = {
    f match {
      case TypedField(name, dataType, _) => DataType.isNumericType(dataType)
      case _                             => false
    }
  }

  protected def isDateField(f: Field): Boolean = {
    f match {
      case TypedField(name, dataType, _) => DataType.isDateType(dataType)
      case _                             => false
    }
  }

  protected def getPathIfNestedField(f: Field): Option[String] = {
    f match {
      case TypedField(_, _, Some(path)) => Some(path)
      case _                            => None
    }
  }

  protected def areAnyDimensionsNested(dimensions: Field*): Boolean = {
    for (d <- dimensions) {
      d match {
        case TypedField(_, _, Some(path)) => return true
        case _                            =>
      }
    }

    return false
  }

  protected def buildFilterFromWhereClause(whereClause: WhereClause): FilterBuilder = cachedBuildFilterFromWhereClause(whereClause)

  private[this] def buildFilterFromWhereClauseImpl(whereClause: WhereClause): FilterBuilder = cachedBuildFilter(whereClause.boolExpression)

  private[this] def buildFilter(boolExp: BooleanExpression): FilterBuilder = {
    boolExp match {
      case TypedCondition(field @ TypedField(fieldName, dataType, _), operator, nestedPath) => {
        var tempFilter = operator match {
          case ComparisonOperator("=", value)  => termFilter(fieldName, DataType.convert(dataType)(value))

          case ComparisonOperator(">", value)  => rangeFilter(fieldName).gt(DataType.convert(dataType)(value))

          case ComparisonOperator("<", value)  => rangeFilter(fieldName).lt(DataType.convert(dataType)(value))

          case ComparisonOperator(">=", value) => rangeFilter(fieldName).gte(DataType.convert(dataType)(value))

          case ComparisonOperator("<=", value) => rangeFilter(fieldName).lte(DataType.convert(dataType)(value))

          case ComparisonOperator("!=", value) => notFilter(termFilter(fieldName, DataType.convert(dataType)(value)))

          case InOperator(values, negate) => {
            val convertedValues = DataType.convertList(dataType)(values)
            val filter = termsFilter(fieldName, convertedValues).execution("bool")
            if (negate) {
              notFilter(filter)
            }
            else {
              filter
            }
          }

          case ExistsOperator(negate) => {
            if (negate) {
              missingFilter(fieldName)
              //notFilter(existsFilter(fieldName))
            }
            else {
              existsFilter(fieldName)
            }
          }

          case _ => throw new RuntimeException("Unknown operator " + operator)
        }

        //if the field is nested, build a nested filter
        /*for (path <- nestedPath) {
          tempFilter = nestedFilter(path, tempFilter)
        }*/

        return tempFilter

      }

      case TypedAndOperator(lhs, rhs, None)    => andFilter(cachedBuildFilter(lhs), cachedBuildFilter(rhs))

      case TypedOrOperator(lhs, rhs, None)     => orFilter(cachedBuildFilter(lhs), cachedBuildFilter(rhs))

      case TypedAndNotOperator(lhs, rhs, None) => andFilter(cachedBuildFilter(lhs), notFilter(cachedBuildFilter(rhs)))

      case TypedOrNotOperator(lhs, rhs, None)  => orFilter(cachedBuildFilter(lhs), notFilter(cachedBuildFilter(rhs)))

      case moi @ _                             => queryFilter(cachedBuildQuery(moi))

    }
  }

  private def nestFilterIfRequired(filter: FilterBuilder, nestedPath: Option[String]): FilterBuilder = {
    nestedPath match {
      case Some(path) => nestedFilter(path, filter)

      case None       => filter
    }
  }

  /* Query builder code */
  protected def buildQueryFromWhereClause(whereClause: WhereClause): QueryBuilder = cachedBuildQueryFromWhereClause(whereClause)

  private[this] def buildQueryFromWhereClauseImpl(whereClause: WhereClause): QueryBuilder = cachedBuildQuery(whereClause.boolExpression)

  private[this] def buildQuery(boolExp: BooleanExpression): QueryBuilder = {
    boolExp match {
      case TypedCondition(field @ TypedField(fieldName, dataType, _), operator, nestedPath) => {
        var tempQuery: QueryBuilder = operator match {
          case ComparisonOperator("=", value)  => termQuery(fieldName, DataType.convert(dataType)(value))

          case ComparisonOperator(">", value)  => rangeQuery(fieldName).gt(DataType.convert(dataType)(value))

          case ComparisonOperator("<", value)  => rangeQuery(fieldName).lt(DataType.convert(dataType)(value))

          case ComparisonOperator(">=", value) => rangeQuery(fieldName).gte(DataType.convert(dataType)(value))

          case ComparisonOperator("<=", value) => rangeQuery(fieldName).lte(DataType.convert(dataType)(value))

          case ComparisonOperator("!=", value) => boolQuery().mustNot(termQuery(fieldName, DataType.convert(dataType)(value)))

          case InOperator(values, negate) => {
            val convertedValues = DataType.convertList(dataType)(values)
            val query = inQuery(fieldName, convertedValues.toSeq.asInstanceOf[Seq[java.lang.Object]]: _*)
            if (negate) {
              boolQuery().mustNot(query)
            }
            else {
              query
            }
          }

          case ExistsOperator(negate) => throw new UnsupportedOperationException("Exists is not supported by the Query builder")

          case _                      => throw new RuntimeException("Unknown operator " + operator)
        }

        // if the field is nested, build a nested query
        /*for (path <- nestedPath) {
          tempQuery = nestedQuery(path, tempQuery)
        }*/

        return tempQuery

      }

      case TypedAndOperator(lhs, rhs, nestedPath)    => nestQueryIfRequired(boolQuery().must(cachedBuildQuery(lhs)).must(cachedBuildQuery(rhs)), nestedPath)

      case TypedOrOperator(lhs, rhs, nestedPath)     => nestQueryIfRequired(boolQuery().should(cachedBuildQuery(lhs)).should(cachedBuildQuery(rhs)).minimumNumberShouldMatch(1), nestedPath)

      case TypedAndNotOperator(lhs, rhs, nestedPath) => nestQueryIfRequired(boolQuery().must(cachedBuildQuery(lhs)).mustNot(cachedBuildQuery(rhs)), nestedPath)

      case TypedOrNotOperator(lhs, rhs, nestedPath)  => nestQueryIfRequired(boolQuery().should(cachedBuildQuery(lhs)).mustNot(cachedBuildQuery(rhs)).minimumNumberShouldMatch(1), nestedPath)

      case _                                         => throw new RuntimeException("Unknown boolean expression type")
    }
  }

  private def nestQueryIfRequired(query: QueryBuilder, nestedPath: Option[String]): QueryBuilder = {
    nestedPath match {
      case Some(path) => nestedQuery(path, query)

      case None       => query
    }
  }

}