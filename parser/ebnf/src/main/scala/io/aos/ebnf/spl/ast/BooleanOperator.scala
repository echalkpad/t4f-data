package io.aos.ebnf.spl.ast

sealed abstract class BooleanOperator(val lhs: BooleanExpression, val rhs: BooleanExpression) extends BooleanExpression

case class AndOperator(override val lhs: BooleanExpression, override val rhs: BooleanExpression) extends BooleanOperator(lhs, rhs)

case class OrOperator(override val lhs: BooleanExpression, override val rhs: BooleanExpression) extends BooleanOperator(lhs, rhs)

case class AndNotOperator(override val lhs: BooleanExpression, override val rhs: BooleanExpression) extends BooleanOperator(lhs, rhs)

case class OrNotOperator(override val lhs: BooleanExpression, override val rhs: BooleanExpression) extends BooleanOperator(lhs, rhs)

sealed abstract class TypedBooleanOperator(val lhs: TypedBooleanExpression, val rhs: TypedBooleanExpression, val nestedPath: Option[String]) extends TypedBooleanExpression

case class TypedAndOperator(override val lhs: TypedBooleanExpression, override val rhs: TypedBooleanExpression, override val nestedPath: Option[String]) extends TypedBooleanOperator(lhs, rhs, nestedPath)

case class TypedOrOperator(override val lhs: TypedBooleanExpression, override val rhs: TypedBooleanExpression, override val nestedPath: Option[String]) extends TypedBooleanOperator(lhs, rhs, nestedPath)

case class TypedAndNotOperator(override val lhs: TypedBooleanExpression, override val rhs: TypedBooleanExpression, override val nestedPath: Option[String]) extends TypedBooleanOperator(lhs, rhs, nestedPath)

case class TypedOrNotOperator(override val lhs: TypedBooleanExpression, override val rhs: TypedBooleanExpression, override val nestedPath: Option[String]) extends TypedBooleanOperator(lhs, rhs, nestedPath)

