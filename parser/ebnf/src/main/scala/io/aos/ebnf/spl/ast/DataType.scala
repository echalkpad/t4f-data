package io.aos.ebnf.spl.ast

import org.joda.time.format.ISODateTimeFormat
import scala.collection.JavaConversions._

object DataType extends Enumeration {
  val StringType = Value("string")
  val CharType = Value("char")
  val LongType = Value("long")
  val IntegerType = Value("int")
  val DoubleType = Value("double")
  val FloatType = Value("float")
  val BooleanType = Value("boolean")
  val DateType = Value("date")

  private val EpochDate = """^\d+$""".r

  def convert(dataType: DataType.Value): (String => _) = {
    dataType match {
      case StringType  => { value: String => value }

      case CharType    => { value: String => value }

      case LongType    => { value: String => value.toLong }

      case IntegerType => { value: String => value.toInt }

      case DoubleType  => { value: String => value.toDouble }

      case FloatType   => { value: String => value.toFloat }

      case BooleanType => { value: String =>
        {
          value match {
            case "T" | "t" => true

            case "F" | "f" => false

            case _         => value.toBoolean
          }

        }
      }

      case DateType => { value: String => parseDate(value) }

      case _        => { value: String => value }
    }
  }

  def convertList(dataType: DataType.Value): (Seq[String] => java.lang.Iterable[_]) = {
    dataType match {
      case StringType  => { values: Seq[String] => values }

      case CharType    => { values: Seq[String] => values }

      case LongType    => { values: Seq[String] => values.map(s => s.toLong) }

      case IntegerType => { values: Seq[String] => values.map(s => s.toInt) }

      case DoubleType  => { values: Seq[String] => values.map(s => s.toDouble) }

      case FloatType   => { values: Seq[String] => values.map(s => s.toFloat) }

      case BooleanType => { values: Seq[String] =>
        values.map(s => {

          s match {
            case "T" => true

            case "F" => false

            case _   => s.toBoolean
          }

        })
      }

      case DateType => { values: Seq[String] => values.map(s => parseDate(s)) }

      case _        => { values: Seq[String] => values }
    }
  }

  private[this] def parseDate(dateStr: String): Long = {
    dateStr match {
      case EpochDate() => dateStr.toLong
      case _           => ISODateTimeFormat.dateTimeParser.withZoneUTC.parseMillis(dateStr)
    }
  }

  def isNumericType(dataType: DataType.Value): Boolean = {
    dataType match {
      case LongType | IntegerType | DoubleType | FloatType => true
      case _ => false
    }
  }

  def isDateType(dataType: DataType.Value): Boolean = {
    dataType match {
      case DateType => true
      case _        => false
    }
  }

  def isDateOrNumericType(dataType: DataType.Value): Boolean = {
    isNumericType(dataType) || isDateType(dataType)
  }

  def isNonNumericType(dataType: DataType.Value): Boolean = {
    !isNumericType(dataType)
  }
}