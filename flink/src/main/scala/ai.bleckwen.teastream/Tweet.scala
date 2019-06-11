package ai.bleckwen.teastream

import java.text.SimpleDateFormat
import java.util.Date

import net.liftweb.json.DefaultFormats
import net.liftweb.json.Serialization.write
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode

import scala.util.Try


case class Tweet(time: Date, text: String, words: Seq[String], hashtags: Set[String],retweet_count: Int=0, geo: String = "unknown", tweet_type: String = "normal", key:Set[String] = Set(), usedKey:Set[String] = Set(), score: String = "undefined") {
  override def toString: String = {
    write(this)
  }
  implicit val formats = DefaultFormats
}
object Tweet {
  val hashtagPattern = """(\s|\A)#(\w+)""".r
  val wordPattern = """\w+""".r
  val dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
  lazy val jsonParser = new ObjectMapper()

  def apply(obj: ObjectNode): Try[Tweet] ={
    Try({
      val key = obj.get("key").asText().split(" ").toSet
      val value = obj.get("value")
      var text = ""
      if (value.get("truncated").asBoolean()){
        text = value.get("extended_tweet").get("full_text").asText ()
      }
      else{
        text = value.get("text").asText ()
      }
      val time = dateFormat.parse (value.get ("created_at").asText ())
      val hashtags: Set[String] = hashtagPattern.findAllIn (text).toSet
      val words = Tokenizer().transform(text)
      val geo = value.get("user").get ("location").asText ()
      var tweet_type = "normal"
      if(value.has("retweeted_status")) tweet_type ="retweet"
      if(value.has("quoted_status")) tweet_type ="reply"
      val retweet_count = value.get("retweet_count").asInt()

      val usedKey = key.filter(words.contains(_))

      Tweet (time, text, words, hashtags,retweet_count, geo, tweet_type, key, usedKey)
    })
  }
}

