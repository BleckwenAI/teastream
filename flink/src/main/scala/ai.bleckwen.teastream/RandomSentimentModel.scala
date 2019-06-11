package ai.bleckwen.teastream

import scala.util.Random

case class RandomSentimentModel() {
  val rdm = new Random
  def predict(tweet: Tweet) = {
    val score = if (rdm.nextBoolean()) "positive" else "negative"
    tweet.copy(score=score)
  }
}
