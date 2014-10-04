package io.aos.ebnf.spl.semantic

import java.io.File

import scala.collection.mutable
import io.aos.ebnf.spl.semantic.EsMetadata;
import io.aos.ebnf.spl.ast.DataType
import io.aos.ebnf.spl.ast.Field
import io.aos.ebnf.spl.ast.TypedField
import io.aos.ebnf.spl.protocol.BaseDataSourceMetadataResponse
import io.aos.ebnf.spl.protocol.DataSourceMetadataResponse
import io.aos.ebnf.spl.protocol.EsDataSourceMetadataResponse
import io.aos.ebnf.spl.protocol.EsDataSourceMetadataResponse
import io.aos.ebnf.spl.protocol.EsFieldMetadata
import io.aos.ebnf.spl.protocol.FieldMetadata
import grizzled.slf4j.Logging
import io.aos.ebnf.util.JsonUtil

/**
 * Base trait for all metadata for a data source.
 */

//trait DataSourceMetadata(dataSources: Map[String, Seq[String]], objectFields: Map[String, mutable.Map[String, GenericFieldMetadata]])
trait DataSourceMetadataLike {
  type T

  def dataSources: Map[String, Seq[String]]

  def objectFields: Map[String, mutable.Map[String, T]]
}

/*
 * Metadata specific to Elastic Search 
 */
sealed case class EsMetadata(override val dataSources: Map[String, Seq[String]], override val objectFields: Map[String, mutable.Map[String, EsFieldMetadata]]) extends DataSourceMetadataLike {
  type T = EsFieldMetadata
}

object OfflineDataSourceMetadata extends Logging {
  val MetaDataFile = "datasource-metadata.json"
//  val MetaDataLocation = System.getProperty("user.home") + File.separator + "spl"
  val MetaDataLocation = ".src/test/resources"
  var metadata: EsMetadata = null

  def reload() {
    metadata = null
    load()
  }

  def load(): BaseDataSourceMetadataResponse = {
    if (metadata == null) {
      val file: File = new File(MetaDataLocation + File.separator + MetaDataFile)
      if (file.exists()) {
        logger.info("Reading metadata from " + file.getAbsolutePath())
        metadata = JsonUtil.fromFile[EsMetadata](file)
      }
      else {
        logger.info("Reading metadata from classpath")
        metadata = JsonUtil.fromStream[EsMetadata](getClass.getClassLoader.getResourceAsStream(MetaDataFile))
      }
    }
    new EsDataSourceMetadataResponse(metadata.dataSources, metadata.objectFields)
  }
}

object DataSourceMetadata extends Logging {
  var metadata: BaseDataSourceMetadataResponse = _
  private var updateCallbacks: mutable.ArrayBuffer[() => BaseDataSourceMetadataResponse] = mutable.ArrayBuffer.empty[() => BaseDataSourceMetadataResponse]

  def registerUpdater(f: () => BaseDataSourceMetadataResponse) = {
    logger.debug(s"Registering updater-callback with datasources ${f().dataSources} " + f.toString)
    logger.debug(s"FOOO")
    updateCallbacks += f
    logger.debug(s"New length of updater-callbacks is now: ${updateCallbacks.length}")
  }

  def updateMetadata(): Unit = {
    if (updateCallbacks.isEmpty) {
      throw new RuntimeException("No updaters registered!. Exiting")
    }
    var results = updateCallbacks.map { f => f() }
    logger.debug("Updating metadata informationz. Have: " + results.flatMap { _.dataSources })
    logger.debug(s"Calling func(0) directly gives me: ${updateCallbacks(0)()}")
    results foreach { r =>

    }
    //    TODO: JUST HEAD. REMOVE! do something like this...
    //    map {
    //      case r: EsDataSourceMetadataResponse => new EsDataSourceMetadataResponse(r.dataSources, r.objectFields)
    //    } 
    //    
    metadata = results.head
  }

  def dataSources(): Set[String] = {
    updateMetadata()
    //    val res = metadata.foldLeft(Set.empty[String]) { (acc, s) =>
    //      acc ++ s.dataSources.keySet
    //    }
    //    res.toSet
    metadata.dataSources.keySet.toSet
  }

  def fieldsOfDataSource(dataSrcName: String): Option[DataSourceMetadataResponse] = {
    updateMetadata()
    val indexName = dataSrcName.replace(".", "-")

    //    val sources = metadata.flatMap{_.dataSources.get(indexName)}
    //    sources.headOption match {
    //      case Some(objname) => DataSourceMetadataResponse(metadata.objectFields(objname).toMap)
    //      case _ => None
    //    }

    metadata.dataSources.get(indexName) match {
      case Some(x: Seq[_]) => {
        val objName = x.head
        //        Some(DataSourceMetadataResponse(metadata.objectFields(objName).toMap))
        Some(DataSourceMetadataResponse(metadata.fieldMeta(objName).toMap))
      }
      case None => None
    }

  }
  //  TODO: this should return the rendered text of the query
  //  def secondaryDatasourceDetail()

  def dataSourceExists(dataSrcName: String): Boolean = {
    try {
      val fields = fieldsOfDataSource(dataSrcName.toLowerCase)

      fields match {
        case Some(_) => true
        case None    => false
      }
    }
    catch {
      case e: Exception => {
        logger.error("Failed to lookup datasource " + dataSrcName, e)
        return false
      }
    }
  }

  def typeAnnotateField(dataSrcName: String, fieldName: String): Field = {

    try {
      val dataSource = dataSrcName.toLowerCase()
      //val field = fieldName.toLowerCase()

      val dataTypes = fieldsOfDataSource(dataSource)
      if (!dataTypes.isDefined) {
        throw AnnotationException(s"Unknown datasource: $dataSource")
      }

      val fieldMeta = dataTypes.get.fieldMeta.get(fieldName)
      fieldMeta match {
        case Some(fm: EsFieldMetadata) => {
          val dataType = DataType.withName(fm.dataType)
          TypedField(fieldName, dataType, fm.nestedPath)
        }

        case _ => throw AnnotationException(s"Unknown field: $fieldName")
      }
    }
    catch {
      case ae: AnnotationException => throw ae
      case e: Exception => {
        logger.error(s"Failed to lookup field $fieldName from $dataSrcName", e)
        throw AnnotationException(s"Internal error during lookup of  ${dataSrcName}.${fieldName} [ ${e.getMessage}]")
      }
    }
  }
}
