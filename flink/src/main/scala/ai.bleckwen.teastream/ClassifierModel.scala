package ai.bleckwen.teastream

import hex.genmodel.GenModel
import hex.genmodel.easy.EasyPredictModelWrapper
import hex.genmodel.easy.RowData
import scala.collection.JavaConverters._

case class ClassifierModel(classifierModel: EasyPredictModelWrapper) {

  import ClassifierModel._

  def predict(tweet: Tweet): Tweet = {

    val predictedSentiment = Word2vecModel("/w2v_hex.zip").transform(tweet.words) match {
      case textEmbedding if textEmbedding != None =>
        predictSentiment(classifierModel.predictBinomial(createClassifierRowData(textEmbedding.get)).classProbabilities(1))
      case _ => "undefined"
    }

    tweet.copy(score = predictedSentiment)

  }

}

object ClassifierModel {

  def apply(pojoName: String): ClassifierModel =
    ClassifierModel(new EasyPredictModelWrapper(Class.forName(pojoName).newInstance().asInstanceOf[GenModel]))

  def createClassifierRowData(textEmbedding: Array[Float]): RowData = {

    val featuresNames = (1 to 100).map("C" + _.toString)
    val stringTextEmbedding = textEmbedding.map(_.toString)
    val featuresMap = featuresNames.zip(stringTextEmbedding).toMap
    val rowData = new RowData
    rowData.putAll(featuresMap.asJava)
    rowData

  }

  def predictSentiment(positiveProbability: Double, tolerance: Double = 0.05): String = {

    positiveProbability match {
      case probability if probability > 0.5 + tolerance => "positive"
      case probability if probability < 0.5 - tolerance => "negative"
      case _ => "undecided"
    }

  }

}
