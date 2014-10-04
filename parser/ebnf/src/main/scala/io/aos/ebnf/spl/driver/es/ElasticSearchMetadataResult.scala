package io.aos.ebnf.spl.driver.es

import scala.collection.mutable

//TODO: using java.util.hashmap to do with strange behaviour of implicit conversions to mutable.map (from ES map). Revisit /JS
case class ElasticSearchMetadataResult(dataSources : Map[String, Seq[String]], objectFields: mutable.Map[String, java.util.HashMap[String, AnyRef]])