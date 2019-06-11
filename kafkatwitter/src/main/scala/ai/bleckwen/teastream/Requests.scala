package ai.bleckwen.teastream

import java.util.Properties

import org.apache.kafka.clients.consumer._
import org.apache.kafka.clients.producer.KafkaProducer

import collection.JavaConverters._
import scala.collection.mutable.Map


object Requests {
  val topic: String = "requests_topic"

  def main(args:Array[String]): Unit = {
    val authMap = Map("consumerKey" -> args(0), "consumerSecret" -> args(1), "accessToken" -> args(2), "accessTokenSecret" -> args(3))
    val props: Properties = initPropertiesC()
    val consumer: KafkaConsumer[String, String] = initConsumer(props)
    val properties = initPropertiesP()
    val producer = initProducer(properties)
    consumer.subscribe(java.util.Arrays.asList(topic))
    processMessages(consumer, producer, authMap)
    consumer.close()
    producer.close()
  }
  def initPropertiesP(): Properties = {
    val properties = new Properties()
    properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    properties.setProperty("client.id", "KafkaTwitterProducer")
    properties.setProperty("bootstrap.servers", "broker:29092")
    properties.setProperty("acks","all")
    properties.setProperty("retries","0")
    properties
  }

  def initProducer(props: Properties): KafkaProducer[String, String] = new KafkaProducer[String, String](props)


  def initPropertiesC(): Properties = {
    val properties = new Properties()
    properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("client.id", "KafkaTwitterProducer")
    properties.setProperty("bootstrap.servers", "broker:29092")
    properties.setProperty("group.id","teest")
    properties
  }

  def initConsumer(props: Properties): KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)

  var mymap = Map[String , KafkaTwitterProducer]()

  def processMessages(consumer: KafkaConsumer[String, String], producer: KafkaProducer[String, String], authMap: Map[String, String]): Unit = {
    while(true){
      val messages = consumer.poll(1000).asScala.toSeq
      messages.foreach(x => {
        if (x.key()=="track")
          mymap(x.value()) = new KafkaTwitterProducer(x.value(), producer, authMap)
        if (x.key()=="stop") {
          if(mymap.contains(x.value())){
            mymap(x.value()).stop()
            mymap = mymap - x.value()
          }
        }
      })
    }
  }
}
