package io.aos.ebnf.spl.semantic.impl

import scala.collection.mutable.ListBuffer
import scala.util.{ Failure, Success, Try }
import io.aos.ebnf.spl.ast._
import io.aos.ebnf.spl.backend.BackendType
import io.aos.ebnf.spl.semantic.{ AnnotationException, DataSourceMetadata, SplSemanticAnalyzer }
import io.aos.ebnf.spl.backend.BackendRegistry

class SplSemanticAnalyzerImpl extends SplSemanticAnalyzer {
  override def annotate(ast: SplQuery): Either[String, SplQuery] = {
    Try(annotateQuery(ast)) match {
      case Success(q)                      => Right(q)

      case Failure(e: AnnotationException) => Left(e.errorMessage)

      case Failure(e)                      => Left(e.getMessage())
    }
  }

  private def annotateQuery(ast: SplQuery): SplQuery = {
    ast match {
      case descQuery: DescQuery => annotateDescQuery(descQuery)

      case selectQuery: SelectQuery => annotateSelectQuery(selectQuery)

      case topQuery: TopNQuery => annotateTopnQuery(topQuery)

      case bottomQuery: BottomNQuery => annotateTopnQuery(bottomQuery)

      case rangeQuery: RangeQuery => annotateRangeQuery(rangeQuery)

      case pivotQuery: PivotQuery => annotatePivotQuery(pivotQuery)

      case relativeHistoQuery: RelativeHistogramQuery => annotateRelativeHistogramQuery(relativeHistoQuery)

      case absoluteHistoQuery: AbsoluteHistogramQuery => annotateAbsoluteHistogramQuery(absoluteHistoQuery)

      case statsQuery: StatsQuery => annotateStatsQuery(statsQuery)

      case countQuery: CountQuery => annotateCountQuery(countQuery)

      case _ => throw AnnotationException("Unknown AST")
    }
  }

  private def annotateDescQuery(descQuery: DescQuery): SplQuery = {
    val dataSource = annotateDataSource(DataSource(descQuery.src))
    DescQuery(dataSource.src)
  }

  private def annotateSelectQuery(selectQuery: SelectQuery): SplQuery = {
    val dataSource = annotateDataSource(selectQuery.src)

    dataSource match {

    case DataSource(_, BackendType.ElasticSearch) => {
        selectQuery match {
          case SelectQuery(Some(fieldList), whereClause, _) => {
            val fl = annotateFieldList(dataSource.src, fieldList)
            val wc = annotateWhereClause(dataSource.src, whereClause)
            SelectQuery(Some(fl), wc, dataSource)
          }

          case SelectQuery(None, whereClause, _) => {
            val wc = annotateWhereClause(dataSource.src, whereClause)
            SelectQuery(None, wc, dataSource)
          }
        }
      }
    }
  }

  private def annotateTopnQuery(ast: TopTermsQuery): SplQuery = {

      /* private function that handles the actual semantic analysis */
      def enhanceParameters(param: Option[String], count: Int, field: Field, ordering: Option[Ordering], filter: Option[WhereClause], datasrc: DataSource, constructor: ((Option[String], Int, Field, Option[Ordering], Option[WhereClause], DataSource)) => SplQuery): SplQuery = {
        val dataSource = annotateDataSource(datasrc)

        var annotatedWhere: Option[WhereClause] = None
        for (whereClause <- filter) {
          annotatedWhere = Some(annotateWhereClause(dataSource.src, whereClause))
        }

        val annotatedField = DataSourceMetadata.typeAnnotateField(dataSource.src, field.name)

        //Check that the ordering is correctly defined
        for (order <- ordering) {
          val typedField = annotatedField.asInstanceOf[TypedField]

          if (DataType.isNonNumericType(typedField.dataType) && (!Ordering.isNonNumericOrdering(order))) {
            throw AnnotationException(s"${typedField.name} is a non numeric field and can only be ordered by term or count")
          }
        }
        // build the object
        constructor((param, new java.lang.Integer(count), annotatedField, ordering, annotatedWhere, dataSource))
      }

    /* TopN and BottomN have the same parameter lists but we need to pattern match to correctly produce the typed instances */
    ast match {
      case TopNQuery(param, count, field, ordering, filter, datasrc) => enhanceParameters(param, count, field, ordering, filter, datasrc, TopNQuery.tupled).asInstanceOf[TopNQuery]

      case BottomNQuery(param, count, field, ordering, filter, datasrc) => enhanceParameters(param, count, field, ordering, filter, datasrc, BottomNQuery.tupled).asInstanceOf[BottomNQuery]

      case _ => throw AnnotationException("Unknown query type")
    }

  }

  private def annotateRangeQuery(ast: RangeQuery): SplQuery = {
    val dataSource = annotateDataSource(ast.src)
    val enhancedResult = annotateTwoDimensionsAndFilter(dataSource.src, ast.dimensionOne, ast.dimensionTwo, ast.filter)
    val annotatedQuery = RangeQuery(ast.param, ast.rangeList, enhancedResult._1, enhancedResult._2, enhancedResult._3, dataSource)

    // Dimensions should be numeric for the query to work
    annotatedQuery match {
      case RangeQuery(_, _, TypedField(_, dimOneType, _), None, _, _) if DataType.isNumericType(dimOneType) => annotatedQuery

      case RangeQuery(_, _, TypedField(_, dimOneType, _), Some(TypedField(_, dimTwoType, _)), _, _) if DataType.isNumericType(dimOneType) && DataType.isNumericType(dimTwoType) => annotatedQuery

      case _ => throw AnnotationException("Range query dimensions must be numeric fields")
    }
  }

  private def annotatePivotQuery(ast: PivotQuery): SplQuery = {
    val dataSource = annotateDataSource(ast.src)

    val enhancedResult = annotateTwoDimensionsAndFilter(dataSource.src, ast.dimensionOne, ast.dimensionTwo, ast.filter)
    PivotQuery(ast.param, ast.dimLimit, enhancedResult._1, enhancedResult._2, enhancedResult._3, dataSource)
  }

  private def annotateRelativeHistogramQuery(ast: RelativeHistogramQuery): SplQuery = {
    val dataSource = annotateDataSource(ast.src)
    val enhancedResult = annotateTwoDimensionsAndFilter(dataSource.src, ast.dimensionOne, ast.timeDimension, ast.filter)

    // type checking
    val finalResult = enhancedResult match {
      case q @ (TypedField(_, dataType, _), TypedField(_, time, _), _) => {
        if (DataType.isDateOrNumericType(dataType) && DataType.isDateType(time)) {
          q
        }
        else {
          throw AnnotationException("First dimension must be numeric and the second dimension must be a date")
        }
      }

      case _ => throw AnnotationException("Unexpected result during type checking")
    }

    RelativeHistogramQuery(ast.param, finalResult._1, ast.relativeInterval, finalResult._2, finalResult._3, dataSource)
  }

  private def annotateAbsoluteHistogramQuery(ast: AbsoluteHistogramQuery): SplQuery = {

    val dataSource = annotateDataSource(ast.src)
    val enhancedResult = annotateTwoDimensionsAndFilter(dataSource.src, ast.dimensionOne, ast.dimensionTwo, ast.filter)
    val annotatedQuery = AbsoluteHistogramQuery(ast.param, enhancedResult._1, enhancedResult._2, ast.interval, enhancedResult._3, dataSource)

      // Type checks
      def isKeyDimensionTypedCorrectly(dataType: DataType.Value, interval: Interval): Boolean = {
        if (DataType.isNumericType(dataType) && Interval.isNumeric(interval)) {
          true
        }
        else if (DataType.isDateType(dataType) && (!Interval.isNumeric(interval))) {
          true
        }
        else {
          false
        }
      }

    annotatedQuery match {
      case AbsoluteHistogramQuery(_, TypedField(_, dimOneType, _), None, interval, _, _) => {
        if (isKeyDimensionTypedCorrectly(dimOneType, interval)) {
          annotatedQuery
        }
        else {
          throw AnnotationException("Dimension must be either a numeric or a date type with a corresponding interval definition.")
        }
      }

      case AbsoluteHistogramQuery(_, TypedField(_, dimOneType, _), Some(TypedField(_, dimTwoType, _)), interval, _, _) => {
        if (isKeyDimensionTypedCorrectly(dimTwoType, interval) && DataType.isNumericType(dimOneType)) {
          annotatedQuery
        }
        else {
          throw AnnotationException("First dimension must be numeric and the second dimension must be either a numeric or a date type with a corresponding interval definition.")
        }
      }

      case _ => throw AnnotationException("Unable to validate histogram query")
    }
  }

  private def annotateStatsQuery(ast: StatsQuery): SplQuery = {
    val dataSource = annotateDataSource(ast.src)
    val enhancedFieldList = annotateFieldList(dataSource.src, ast.fieldList)
    val enhancedWhere: Option[WhereClause] = ast.filter.map(f => annotateWhereClause(dataSource.src, f))

    StatsQuery(ast.param, enhancedFieldList, enhancedWhere, dataSource)
  }

  private def annotateCountQuery(ast: CountQuery): SplQuery = {
    val dataSource = annotateDataSource(ast.src)

    val enhancedWhere: Option[WhereClause] = ast.filter.map(f => annotateWhereClause(dataSource.src, f))
    CountQuery(enhancedWhere, dataSource)
  }

  private def annotateTwoDimensionsAndFilter(dataSource: String, dimOne: Field, dimTwo: Field, whereClause: Option[WhereClause]): (Field, Field, Option[WhereClause]) = {
    val result = annotateTwoDimensionsAndFilter(dataSource, dimOne, Some(dimTwo), whereClause)
    return (result._1, result._2.get, result._3)
  }

  private def annotateTwoDimensionsAndFilter(dataSource: String, dimOne: Field, dimTwo: Option[Field], whereClause: Option[WhereClause]): (Field, Option[Field], Option[WhereClause]) = {
    val annotatedDimOne = DataSourceMetadata.typeAnnotateField(dataSource, dimOne.name)
    val annotatedDimTwo: Option[Field] = dimTwo.map(d => DataSourceMetadata.typeAnnotateField(dataSource, d.name))
    val annotatedWhere: Option[WhereClause] = whereClause.map(w => annotateWhereClause(dataSource, whereClause.get))

    (annotatedDimOne, annotatedDimTwo, annotatedWhere)
  }

  private def annotateDataSource(dataSource: DataSource): DataSource = {
    val formattedDataSourceName = formatDataSourceName(dataSource.src)
    val backend = BackendRegistry.detectBackend(formattedDataSourceName)

    if (!DataSourceMetadata.dataSourceExists(formattedDataSourceName)) {
      throw AnnotationException(s"Datasource '${dataSource.src}' not found")
    }

    BackendRegistry.detectBackend(formattedDataSourceName) match {
      case Some(backendTypeVal) => DataSource(formattedDataSourceName, backendTypeVal)
      case None                 => throw AnnotationException(s"Cannot determine a valid backend in [$formattedDataSourceName]")
    }

  }

  private def formatDataSourceName(dataSrcName: String): String = dataSrcName.toLowerCase.replaceAllLiterally(".", "-")

  private def annotateFieldList(dataSrc: String, fieldList: List[Field]): List[Field] = {
    val typedList: ListBuffer[Field] = new ListBuffer()

    // go through each field and try to annotate fields. Accumulate the errors if any
    fieldList.foreach { field =>
      val annotatedField = DataSourceMetadata.typeAnnotateField(dataSrc, field.name)
      typedList.append(annotatedField)
    }

    typedList.toList
  }

  private def annotateWhereClause(dataSrc: String, whereClause: WhereClause): WhereClause = WhereClause(annotateBooleanExpression(dataSrc, whereClause.boolExpression))

  private[semantic] def annotateBooleanExpression(dataSrc: String, expr: BooleanExpression): TypedBooleanExpression = {
    expr match {
      case Condition(field, operator) => {
        val annotatedField = DataSourceMetadata.typeAnnotateField(dataSrc, field.name)
        annotatedField match {
          case TypedField(_, _, Some(nestedPath)) => TypedCondition(annotatedField, operator, Some(nestedPath))

          case _                                  => TypedCondition(annotatedField, operator, None)
        }
      }

      case boolExpr: BooleanOperator => {
        val annotatedLeft = annotateBooleanExpression(dataSrc, boolExpr.lhs)
        val annotatedRight = annotateBooleanExpression(dataSrc, boolExpr.rhs)

        val (lhs, rhs, path) = promoteNestedPaths(annotatedLeft, annotatedRight)
        boolExpr match {
          case AndOperator(_, _)    => TypedAndOperator(lhs, rhs, path)

          case OrOperator(_, _)     => TypedOrOperator(lhs, rhs, path)

          case AndNotOperator(_, _) => TypedAndNotOperator(lhs, rhs, path)

          case OrNotOperator(_, _)  => TypedOrNotOperator(lhs, rhs, path)
        }
      }

      case b: TypedBooleanExpression => b

      case _                         => throw AnnotationException("Unknown boolean expression")

    }
  }

  /**
   * If both the left and right hand side expressions are of the same nested path, we need to promote it higher up in the tree. This is because Elastic Search
   * behaves incorrectly if nesting is not defined at the topmost level
   */
  private def promoteNestedPaths(lhs: TypedBooleanExpression, rhs: TypedBooleanExpression): (TypedBooleanExpression, TypedBooleanExpression, Option[String]) = {
    val lhsPath = extractNestedPath(lhs)
    val rhsPath = extractNestedPath(rhs)

    (lhsPath, rhsPath) match {
      case (Some(lp), Some(rp)) if (lp == rp) => {
        val tmpLhs = stripNestedPath(lhs)
        val tmpRhs = stripNestedPath(rhs)
        return (tmpLhs, tmpRhs, Some(lp))
      }

      case _ => (lhs, rhs, None)
    }
  }

  private def extractNestedPath(be: TypedBooleanExpression): Option[String] = {
    be match {
      case TypedAndOperator(_, _, path)    => path

      case TypedOrOperator(_, _, path)     => path

      case TypedAndNotOperator(_, _, path) => path

      case TypedOrNotOperator(_, _, path)  => path

      case TypedCondition(_, _, path)      => path

      case _                               => None
    }
  }

  private def stripNestedPath(expr: TypedBooleanExpression): TypedBooleanExpression = {
    expr match {
      case TypedAndOperator(lhs, rhs, _)    => TypedAndOperator(lhs, rhs, None)

      case TypedOrOperator(lhs, rhs, _)     => TypedOrOperator(lhs, rhs, None)

      case TypedAndNotOperator(lhs, rhs, _) => TypedAndNotOperator(lhs, rhs, None)

      case TypedOrNotOperator(lhs, rhs, _)  => TypedOrNotOperator(lhs, rhs, None)

      case TypedCondition(lhs, rhs, _)      => TypedCondition(lhs, rhs, None)

      case x @ _                            => x
    }
  }
}