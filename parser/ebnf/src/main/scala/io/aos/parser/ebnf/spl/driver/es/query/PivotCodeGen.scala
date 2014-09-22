package io.aos.parser.ebnf.spl.driver.es.query

import io.aos.parser.ebnf.spl.ast.PivotQuery
import io.aos.parser.ebnf.spl.driver.es.ElasticSearchQuery
import io.aos.parser.ebnf.spl.driver.es.ElasticSearchQueryResult
import io.aos.parser.ebnf.spl.ast.TopLimit
import io.aos.parser.ebnf.spl.ast.Field
import io.aos.parser.ebnf.spl.ast.WhereClause
import io.aos.parser.ebnf.spl.ast.Condition
import io.aos.parser.ebnf.spl.ast.BottomLimit
import org.elasticsearch.search.facet.FacetBuilders._
import io.aos.parser.ebnf.spl.ast.AndOperator
import io.aos.parser.ebnf.spl.ast.ComparisonOperator
import io.aos.parser.ebnf.spl.semantic.SplSemanticAnalyzer
import io.aos.parser.ebnf.spl.ast.UntypedField
import io.aos.parser.ebnf.spl.ast.TypedBooleanExpression
import org.elasticsearch.search.facet.FacetBuilder
import io.aos.parser.ebnf.spl.driver.es.ElasticSearchQueryResult
import io.aos.parser.ebnf.spl.ast.BooleanExpression
import scala.collection.mutable

/**
 * Class to generate ElasticSearch code for Pivot queries
 */
object PivotCodeGen extends EsCodeGen {
  type T = PivotQuery

  private val MaxAllowedDimSize = 25
  private val DefaultSizeLimit = 10
  private val DimOneFacet = "dimOneFacet"
  private val DimTwoFacet = "dimTwoFacet"
  private val ExcludeTerms = Seq(ElasticSearchQuery.NullTerm, ElasticSearchQuery.OtherTerm)

  override def generate(ast: PivotQuery)(execute: ElasticSearchQuery => Option[ElasticSearchQueryResult]): Either[String, ElasticSearchQueryResult] = {
    logger.debug("Generating Pivot code")
    /*
     * Pivot algorithm is as follows:
     * 1. Find the top n values of dimension one and dimension two
     * 2. For each pair of values obtained from step one, find the count of records where dimension one and dimension two equals the specified value
     */

    if (isNumericField(ast.dimensionTwo)) {
      val stepOne = doNumericDimensionFirstStep(ast)(execute)
      stepOne.fold(
        fail => Left(fail),
        succ => doNumericDimensionSecondStep(ast, succ)(execute))

    }
    else {
      val stepOne = doNonNumericDimensionFirstStep(ast)(execute)
      stepOne.fold(
        fail => Left(fail),
        succ => doNonNumericDimensionSecondStep(ast, succ)(execute))
    }

  }

  /* ================================ Numeric Second Dimension ================================ */

  private def doNumericDimensionFirstStep(ast: PivotQuery)(execute: ElasticSearchQuery => Option[ElasticSearchQueryResult]): Either[String, ElasticSearchQueryResult] = {
    logger.debug("On first step of pivot with numeric dimension")
    var numericDim: Field = null
    var nonNumericDim: Field = null

    numericDim = ast.dimensionTwo
    nonNumericDim = ast.dimensionOne

    val filterSourceBuilder = createFilterSourceBuilder()

    var facetOne: FacetBuilder = null

    ast match {
      case PivotQuery(_, Some(TopLimit(n)), _, _, whereClause, _) => {
        facetOne = buildTermsFacet(field = nonNumericDim, count = n, filter = whereClause, reverse = false, facetName = DimOneFacet)
      }

      case PivotQuery(_, Some(BottomLimit(n)), _, _, whereClause, _) => {
        facetOne = buildTermsFacet(field = nonNumericDim, count = n, filter = whereClause, reverse = true, facetName = DimOneFacet)
      }

      case PivotQuery(_, None, _, _, whereClause, _) => {
        facetOne = buildTermsFacet(field = nonNumericDim, count = DefaultSizeLimit, filter = whereClause, reverse = false, facetName = DimOneFacet)
      }
    }

    filterSourceBuilder.facet(facetOne)

    if ((ast.filter.isDefined) && (areAnyDimensionsNested(numericDim, nonNumericDim))) {
      val queryDef = buildQueryFromWhereClause(ast.filter.get)
      filterSourceBuilder.query(queryDef)
    }

    val result = execute(ElasticSearchQuery(filterSourceBuilder, ast.src.src, Seq(nonNumericDim.name), ast, returnRawResults = true))
    result match {
      case Some(r) => Right(r)

      case None    => Left("Query execution failed")
    }
  }

  private def doNumericDimensionSecondStep(ast: PivotQuery, results: ElasticSearchQueryResult)(execute: ElasticSearchQuery => Option[ElasticSearchQueryResult]): Either[String, ElasticSearchQueryResult] = {
    logger.debug("On first step of pivot with numeric dimension")
    var numericDim: Field = null
    var nonNumericDim: Field = null

    numericDim = ast.dimensionTwo
    nonNumericDim = ast.dimensionOne

    val dimOneFields = (results.data.get(DimOneFacet) map { m => m.keySet }).getOrElse(Set.empty)

    val filterSourceBuilder = createFilterSourceBuilder()

    if (ast.filter.isDefined) {
      val queryDef = buildQueryFromWhereClause(ast.filter.get)
      filterSourceBuilder.query(queryDef)
    }

    for (fieldOne <- dimOneFields if (!ExcludeTerms.contains(fieldOne))) {
      val facet = buildStatsFacet(fieldOne, ast, numericDim, nonNumericDim, fieldOne)
      filterSourceBuilder.facet(facet)
    }

    val esQuery = ElasticSearchQuery(filterSourceBuilder, ast.src.src, Seq(nonNumericDim.name), ast)
    logger.debug(esQuery.asString())
    val result = execute(esQuery)
    result match {
      case Some(r) => Right(r)

      case None    => Left("Query execution failed")
    }
  }

  def buildStatsFacet(facetName: String, ast: PivotQuery, numericField: Field, nonNumericField: Field, nonNumericFieldValue: String): FacetBuilder = {
    val dimFilter = buildPivotStatsCondition(ast, nonNumericField, nonNumericFieldValue)

    val facet = statisticalFacet(facetName)
    facet.field(numericField.name)
    facet.facetFilter(buildFilterFromWhereClause(WhereClause(dimFilter)))
    return facet
  }

  private[this] def buildPivotStatsCondition(ast: PivotQuery, field: Field, fieldValue: String): TypedBooleanExpression = {
    var tmpNewCond: BooleanExpression = Condition(UntypedField(field.name), ComparisonOperator("=", fieldValue))

    val retval = SplSemanticAnalyzer.annotateBooleanExpression(ast.src.src, tmpNewCond)
    retval.fold(
      fail => throw new RuntimeException(fail),
      succ => return succ)
  }

  /* ================================ Non-numeric Second Dimension ================================ */

  private def doNonNumericDimensionFirstStep(ast: PivotQuery)(execute: ElasticSearchQuery => Option[ElasticSearchQueryResult]): Either[String, ElasticSearchQueryResult] = {
    logger.debug("On first step of Pivot with non numeric dimension")
    val filterSourceBuilder = createFilterSourceBuilder()

    // build the correct facet based on the sorting order and field type
    var facetOne: FacetBuilder = null
    var facetTwo: FacetBuilder = null

    ast match {
      case PivotQuery(_, Some(TopLimit(n)), dimOne, dimTwo, whereClause, _) => {
        facetOne = buildTermsFacet(field = dimOne, count = n, filter = whereClause, reverse = false, facetName = DimOneFacet)
        facetTwo = buildTermsFacet(field = dimTwo, count = n, filter = whereClause, reverse = false, facetName = DimTwoFacet)
      }

      case PivotQuery(_, Some(BottomLimit(n)), dimOne, dimTwo, whereClause, _) => {
        facetOne = buildTermsFacet(field = dimOne, count = n, filter = whereClause, reverse = true, facetName = DimOneFacet)
        facetTwo = buildTermsFacet(field = dimTwo, count = n, filter = whereClause, reverse = true, facetName = DimTwoFacet)
      }

      case PivotQuery(_, None, dimOne, dimTwo, whereClause, _) => {
        facetOne = buildTermsFacet(field = dimOne, count = DefaultSizeLimit, filter = whereClause, reverse = false, facetName = DimOneFacet)
        facetTwo = buildTermsFacet(field = dimTwo, count = DefaultSizeLimit, filter = whereClause, reverse = false, facetName = DimTwoFacet)
      }
    }

    filterSourceBuilder.facet(facetOne)
    filterSourceBuilder.facet(facetTwo)

    if ((ast.filter.isDefined) && (areAnyDimensionsNested(ast.dimensionOne, ast.dimensionTwo))) {
      val queryDef = buildQueryFromWhereClause(ast.filter.get)
      filterSourceBuilder.query(queryDef)
    }

    val result = execute(ElasticSearchQuery(filterSourceBuilder, ast.src.src, Seq(ast.dimensionOne.name, ast.dimensionTwo.name), ast, returnRawResults = true))
    result match {
      case Some(r) => Right(r)

      case None    => Left("Query execution failed")
    }
  }

  private def doNonNumericDimensionSecondStep(ast: PivotQuery, results: ElasticSearchQueryResult)(execute: ElasticSearchQuery => Option[ElasticSearchQueryResult]): Either[String, ElasticSearchQueryResult] = {
    val dimOneData = results.data(DimOneFacet)
    val dimTwoData = results.data(DimTwoFacet)

    val dimOneFields = dimOneData map { m => m._1 }
    val dimTwoFields = dimTwoData map { m => m._1 }

    val filterSourceBuilder = createFilterSourceBuilder()

    if (ast.filter.isDefined) {
      val queryDef = buildQueryFromWhereClause(ast.filter.get)
      filterSourceBuilder.query(queryDef)
    }

    for (
      fieldOne <- dimOneFields;
      fieldTwo <- dimTwoFields if ((!ExcludeTerms.contains(fieldOne)) && (!ExcludeTerms.contains(fieldTwo)))
    ) {
      val tmpFacetName = Seq(fieldOne, fieldTwo).map(x => if (x == null) { "" } else { x })
      val facet = buildCountFacet(tmpFacetName.mkString("|"), ast, fieldOne, fieldTwo)
      filterSourceBuilder.facet(facet)

      for (p <- ast.param) {
        if (p.equals(ElasticSearchQuery.ConversionsTerm)) {
          val converterFacetName = tmpFacetName.mkString("|") + ":" + ElasticSearchQuery.ConversionsTerm
          val converterFacet = buildCountConvertersFacet(converterFacetName, ast, fieldOne, fieldTwo)
          filterSourceBuilder.facet(converterFacet)
        }
      }
    }

    val esQuery = ElasticSearchQuery(filterSourceBuilder, ast.src.src, Seq(ast.dimensionOne.name, ast.dimensionTwo.name), ast)
    logger.debug(esQuery.asString())
    val result = execute(esQuery)
    result match {
      case Some(r) => {
        val totals: Map[String, Map[String, Any]] = Map(ElasticSearchQuery.TotalsTerm -> Map(ast.dimensionOne.name -> dimOneData, ast.dimensionTwo.name -> dimTwoData))
        Right(enhanceQueryResult(r, totals))
      }

      case None => Left("Query execution failed")
    }
  }

  def buildTermsFacet(field: Field, count: Int, filter: Option[WhereClause], reverse: Boolean, facetName: String): FacetBuilder = {
    if (count > MaxAllowedDimSize) {
      throw new RuntimeException("Maximum dimension size allowed is " + MaxAllowedDimSize)
    }

    if (isNumericField(field)) {
      TopNCodeGen.buildTermStatsFacet(count, field, None, filter, reverse, facetName)
    }
    else {
      TopNCodeGen.buildTermsFacet(count, field, None, filter, reverse, facetName)
    }
  }

  def buildCountFacet(facetName: String, ast: PivotQuery, fieldOneValue: String, fieldTwoValue: String): FacetBuilder = {
    val countFilter = buildPivotCountCondition(ast, fieldOneValue, fieldTwoValue)

    if (areAnyDimensionsNested(ast.dimensionOne, ast.dimensionTwo)) {
      val q = queryFacet(facetName)
      q.query(buildQueryFromWhereClause(WhereClause(countFilter)))
      return q
    }
    else {
      return filterFacet(facetName, buildFilterFromWhereClause(WhereClause(countFilter)))
    }
  }

  private[this] def buildPivotCountCondition(ast: PivotQuery, fieldOneValue: String, fieldTwoValue: String): TypedBooleanExpression = {
    var tmpNewCond = AndOperator(Condition(UntypedField(ast.dimensionOne.name), ComparisonOperator("=", fieldOneValue)), Condition(UntypedField(ast.dimensionTwo.name), ComparisonOperator("=", fieldTwoValue)))

    val retval = SplSemanticAnalyzer.annotateBooleanExpression(ast.src.src, tmpNewCond)
    retval.fold(
      fail => throw new RuntimeException(fail),
      succ => return succ)
  }

  def buildCountConvertersFacet(facetName: String, ast: PivotQuery, fieldOneValue: String, fieldTwoValue: String): FacetBuilder = {
    val countFilter = buildPivotCountConvertersCondition(ast, fieldOneValue, fieldTwoValue)

    if (areAnyDimensionsNested(ast.dimensionOne, ast.dimensionTwo)) {
      val q = queryFacet(facetName)
      q.query(buildQueryFromWhereClause(WhereClause(countFilter)))
      return q
    }
    else {
      return filterFacet(facetName, buildFilterFromWhereClause(WhereClause(countFilter)))
    }
  }

  private[this] def buildPivotCountConvertersCondition(ast: PivotQuery, fieldOneValue: String, fieldTwoValue: String): TypedBooleanExpression = {
    var tmpNewCond = AndOperator(
      Condition(UntypedField("conversionflag"), ComparisonOperator("=", "true")),
      AndOperator(
        Condition(UntypedField(ast.dimensionOne.name), ComparisonOperator("=", fieldOneValue)),
        Condition(UntypedField(ast.dimensionTwo.name), ComparisonOperator("=", fieldTwoValue))))

    val retval = SplSemanticAnalyzer.annotateBooleanExpression(ast.src.src, tmpNewCond)
    retval.fold(
      fail => throw new RuntimeException(fail),
      succ => return succ)
  }

  /**
   * Massive hack to show conversions in the result set. Yuck!
   */
  def enhanceQueryResult(result: ElasticSearchQueryResult, totals: Map[String, Map[String, Any]]): ElasticSearchQueryResult = {
    var newdata: Map[String, Map[String, Any]] = null;
    newdata = result.data match {
      case d: Map[String, Map[String, Map[String, _]]] @unchecked => {
        d.map(lvlOne => {
          val merged: mutable.Map[String, mutable.Map[String, Any]] = mutable.HashMap()
          lvlOne._2.foreach(kv => {
            val key = kv._1.replaceAllLiterally(":" + ElasticSearchQuery.ConversionsTerm, "")
            if (!merged.contains(key)) {
              merged.put(key, new mutable.HashMap())
            }

            if (kv._1.endsWith(ElasticSearchQuery.ConversionsTerm)) {
              merged(key).put(ElasticSearchQuery.ConversionsTerm, kv._2("count"))
            }
            else {
              merged(key).put("count", kv._2("count"))
            }
          })

          (lvlOne._1 -> (merged.map(x => (x._1 -> x._2.toMap))).toMap)
        })
      }

      case d @ _ => d
    }

    val keys = result.keys.map(kv => (kv._1 -> kv._2.filter(f => !(f.asInstanceOf[String].endsWith(ElasticSearchQuery.ConversionsTerm)))))

    return ElasticSearchQueryResult(result.fieldNames, keys, newdata ++ totals, result.stats)

  }

}