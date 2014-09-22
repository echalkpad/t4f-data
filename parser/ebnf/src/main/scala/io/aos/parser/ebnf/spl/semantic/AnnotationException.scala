package io.aos.parser.ebnf.spl.semantic

sealed case class AnnotationException(val errorMessage: String) extends Exception(s"Annotation Error: [$errorMessage]")