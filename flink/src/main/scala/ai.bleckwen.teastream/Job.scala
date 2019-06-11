package ai.bleckwen.teastream

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer010, FlinkKafkaProducer010}
import java.util.Properties

import scala.collection.JavaConverters._



object Job {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.getConfig.disableClosureCleaner()

    //If we have an argument the job is executed on localhost else it is executed with docker in mind
    val localExecution = (!args.isEmpty)
    val kafkaConsumerProperties = initKafkaConsumer(localExecution)
    val kafkaProducerProperties = initKafkaProducer(localExecution)

    val kafkaConsumer = new FlinkKafkaConsumer010(
      List("twitter-topic").asJava,
      new JSONKeyValueDeserializationSchema(true),
      kafkaConsumerProperties
    )
    val kafkaProducer = new FlinkKafkaProducer010(
      "flink-output",
      new SimpleStringSchema,
      kafkaProducerProperties
    )
    val lines = env.addSource(kafkaConsumer)
    val tweets: DataStream[Tweet] = lines.map(Tweet(_)).filter(_.isSuccess).map(_.get)
    val scoredTweets = tweets.map(ClassifierModel("gbm_embeddings_hex").predict(_))
    scoredTweets.print()
    scoredTweets
      .map(x => x.toString())
      .addSink(kafkaProducer)

    env.execute()
  }
  def initKafkaConsumer(localExecution: Boolean) = {
    val props = new Properties()
    if(localExecution) {
      props.setProperty("bootstrap.servers", "localhost:9092")
      props.setProperty("zookeeper.connect", "localhost:2181")
    }
    else {
      props.setProperty("bootstrap.servers", "broker:29092")
      props.setProperty("zookeeper.connect", "zookeeper:2181")
    }
    props.setProperty("group.id", "flink")
    props
  }
  def initKafkaProducer(localExecution: Boolean) = {
    val props = new Properties()
    if(localExecution) {
      props.setProperty("bootstrap.servers", "localhost:9092")
      props.setProperty("zookeeper.connect", "localhost:2181")
    }
    else {
      props.setProperty("bootstrap.servers", "broker:29092")
      props.setProperty("zookeeper.connect", "zookeeper:2181")
    }
    props.setProperty("group.id", "output")
    props
  }

}

