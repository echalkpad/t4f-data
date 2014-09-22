package io.aos.parser.ebnf.spl.ast

abstract class BooleanExpression

case class BooleanTerm(boolExpr: BooleanExpression) extends BooleanExpression

case class AndTerm(booleanTerm: BooleanExpression) extends BooleanExpression

case class AndNotTerm(booleanTerm: BooleanExpression) extends BooleanExpression

case class OrTerm(booleanTerm: BooleanExpression) extends BooleanExpression

case class OrNotTerm(booleanTerm: BooleanExpression) extends BooleanExpression

case class Condition(fieldName: Field, operator: Operator) extends BooleanExpression

abstract class TypedBooleanExpression extends BooleanExpression

case class TypedBooleanTerm(boolExpr: BooleanExpression, nestedPath: Option[String]) extends TypedBooleanExpression

case class TypedAndTerm(booleanTerm: BooleanExpression, nestedPath: Option[String]) extends TypedBooleanExpression

case class TypedAndNotTerm(booleanTerm: BooleanExpression, nestedPath: Option[String]) extends TypedBooleanExpression

case class TypedOrTerm(booleanTerm: BooleanExpression, nestedPath: Option[String]) extends TypedBooleanExpression

case class TypedOrNotTerm(booleanTerm: BooleanExpression, nestedPath: Option[String]) extends TypedBooleanExpression

case class TypedCondition(fieldName: Field, operator: Operator, nestedPath: Option[String]) extends TypedBooleanExpression

