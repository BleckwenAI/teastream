package ai.bleckwen.teastream

import scala.io.Source

case class Tokenizer(){

  lazy val stopWords = Source
    .fromInputStream(getClass.getResourceAsStream("/stopwords.txt"))
    .getLines
    .map(_.toString)
    .toSet

  def transform(text: String): Seq[String] = {

    text
      .split("\\W+")
      .map(x => x.toLowerCase)
      .filter(_.length() > 2)
      .filter(!stopWords(_))
      .toSeq

  }

}
