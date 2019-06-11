package ai.bleckwen.teastream


import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable.Map


class KafkaTwitterProducer(args: String, producer: KafkaProducer[String, String], authMap: Map[String,String]) {
  val keyWds = List(args)
  val key = keyWds.mkString("\""," ", "\"")
  val topic = "twitter-topic"
  val client = new HttpClient(keyWds, authMap)
  var stopBool = false
  client.connect
  Future{
    while(!stopBool) {
      val message = client.take
      sendMessage(producer,topic,key,message)
      println(message)
      producer.flush()
    }
  }
  def stop(): Unit ={
    stopBool = true
    client.stop()
  }
  def sendMessage(producer: KafkaProducer[String, String], topic: String, key: String, message: String) = {
    val record = new ProducerRecord[String, String] (topic, key, message)
    producer.send (record)
  }
}