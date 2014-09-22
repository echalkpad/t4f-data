package io.aos.parser.ebnf.spl.ast

sealed abstract class Field(val name: String)

case class UntypedField(override val name: String) extends Field(name)

case class TypedField(override val name: String, dataType: DataType.Value, nestedPath: Option[String] = None) extends Field(name)

