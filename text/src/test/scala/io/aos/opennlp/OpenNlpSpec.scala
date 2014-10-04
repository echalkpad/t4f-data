package io.aos.opennlp

import java.io.FileInputStream
import java.util.EmptyStackException
import java.util.Stack

import org.scalatest._

import opennlp.tools.sentdetect.SentenceDetectorME
import opennlp.tools.sentdetect.SentenceModel

class OpenNlpSpec extends FlatSpec with Matchers {

  it should "" in {
    val paragraph = "Hi. How are you? This is Mike."
    val s = sentences(paragraph)
    s.length should be(2)
  }

  def sentences(sentence: String): Array[String] = {
    val is = new FileInputStream("./src/test/resources/io/aos/opennlp/en-sent.bin")
    val model = new SentenceModel(is)
    val sentencedetector = new SentenceDetectorME(model)
    val sentences = sentencedetector.sentDetect(sentence)
    println(sentences.take(0))
    println(sentences.take(1))
    is.close();
    sentences;
  }

}
