package ai.bleckwen.teastream

import java.util.concurrent.LinkedBlockingQueue

import com.typesafe.config.ConfigFactory

import collection.JavaConverters._

import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint
import com.twitter.hbc.httpclient.auth.OAuth1
import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core.Constants
import com.twitter.hbc.core.processor.StringDelimitedProcessor

import scala.collection.mutable.Map


class HttpClient(keyWds: List[String], authMap: Map[String,String]) {
  val queue = new LinkedBlockingQueue[String](100000)
  val endPoint = new StatusesFilterEndpoint()
  endPoint.stallWarnings(false)
  endPoint.trackTerms(keyWds.asJava)
  endPoint.addPostParameter("lang","en")

  val conf = ConfigFactory.load()

  val auth = new OAuth1(
    authMap("consumerKey"),
    authMap("consumerSecret"),
    authMap("accessToken"),
    authMap("accessTokenSecret"))

  val client = new ClientBuilder()
    .name("twitter")
    .hosts(Constants.STREAM_HOST)
    .endpoint(endPoint)
    .authentication(auth)
    .processor(new StringDelimitedProcessor(queue))
    .build()
  def connect = client.connect()

  def stop()= client.stop()

  def take() = queue.take()
}
