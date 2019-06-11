package ai.bleckwen.teastream

import hex.genmodel.MojoReaderBackendFactory
import hex.genmodel.ModelMojoReader
import hex.genmodel.easy.EasyPredictModelWrapper
import hex.genmodel.easy.RowData
import scala.collection.JavaConverters._

case class Word2vecModel(word2vecModel: EasyPredictModelWrapper) {

  import Word2vecModel._

  def transform(tokenizedText: Seq[String]): Option[Array[Float]] = {

    tokenizedText match {
      case words if words.isEmpty => None
      case words =>
        filterNull(word2vecModel.predictWord2Vec(createWord2vecRowData(words)).wordEmbeddings.asScala.toMap) match {
          case filteredEmbeddingMap if filteredEmbeddingMap.isEmpty => None
          case filteredEmbeddingMap => Some(averageWordsEmbeddings(filteredEmbeddingMap.values))
      }
    }

  }
}

object Word2vecModel {

  def apply(mojoName: String): Word2vecModel = {

    val mojoURL = getClass.getResource(mojoName)
    val mojoReader =
      MojoReaderBackendFactory.createReaderBackend(mojoURL, MojoReaderBackendFactory.CachingStrategy.MEMORY)
    val mojoModel = ModelMojoReader.readFrom(mojoReader)
    Word2vecModel(new EasyPredictModelWrapper(mojoModel))

  }

  def createWord2vecRowData(tokenizedText: Seq[String]): RowData = {

    val rowData = new RowData
    rowData.putAll(tokenizedText.map(x => (x, x)).toMap.asJava)
    rowData

  }

  def filterNull(textEmbeddingMap: Map[String, Array[Float]]): Map[String, Array[Float]] = {

    def filter(word: String, embedding: Array[Float]): Option[(String, Array[Float])] =
      if (embedding == null) None else Some(word -> embedding)

    textEmbeddingMap.flatMap{case (word, embedding) => filter(word, embedding)}

  }

  def addWordsEmbeddings(embedding1: Array[Float], embedding2: Array[Float]): Array[Float] =
    embedding1.zip(embedding2).map{case (coordinate1, coordinate2) => coordinate1 + coordinate2}

  def averageWordsEmbeddings(embeddings: Iterable[Array[Float]]): Array[Float] =
    embeddings.reduce(addWordsEmbeddings(_, _)).map(_ / embeddings.size)

}
