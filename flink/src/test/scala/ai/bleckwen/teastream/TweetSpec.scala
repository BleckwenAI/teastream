package ai.bleckwen.teastream

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.{JsonNodeFactory, ObjectNode}
import java.text.SimpleDateFormat
import java.util.Date

import ai.bleckwen.teastream.Tweet.{dateFormat, hashtagPattern}
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.scalatest.{Matchers, WordSpec}

import scala.io.Source
import scala.reflect.io.File


class TweetSpec extends WordSpec with Matchers {
  val mapper = new ObjectMapper()
  "A tweet" can {
    val obj = mapper.createObjectNode()
    "fail" in {
      assert(Tweet(obj).isFailure)
    }
    "Tweet constructor" should {

      val stream = getClass.getResourceAsStream("/sample_tweet")
      val messageJson = Source.fromInputStream( stream ).getLines().toList
      print(messageJson)

      val file = File("sample_tweet")

      val obj: ObjectNode = mapper.readTree(messageJson(0)).deepCopy()

      val dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
      val text = "RT @keithboykin: While Trump had you focused\non a caravan of immigrants\n\nan American\nkilled 12 people in a a bar\n\nan American\nkilled 11 peo\u2026"

      "parse a valid message correctly " in {

        val expectedTweet = Tweet(obj)

        val key = obj.get("key").asText().split(" ").toSet
        val value = obj.get("value")
        val text: String = value.get("text").asText ()
        val time = dateFormat.parse (value.get ("created_at").asText ())
        val hashtags: Set[String] = hashtagPattern.findAllIn (text).toSet
        val words = Tokenizer().transform(text)
        val geo = value.get("user").get ("location").asText ()
        var tweet_type = "normal"
        if(value.has("retweeted_status")) tweet_type ="retweet"
        if(value.has("quoted_status")) tweet_type ="reply"
        val retweet_count = value.get("retweet_count").asInt()

        val myTweet = Tweet(time, text, words, hashtags,retweet_count, geo, tweet_type, key)


        assert(expectedTweet.get === myTweet)

      }
    }
  }
}