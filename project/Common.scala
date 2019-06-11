import sbt._
import sbt.Keys._
import sbtassembly.AssemblyPlugin.autoImport.{MergeStrategy, assemblyMergeStrategy}
import sbtassembly.PathList
import sbtassembly.AssemblyKeys._

object Version {
  val kafka = "2.0.0"
  val flink = "1.6.3"
  val scalaTest = "3.0.1"
  val log4j = "1.2.17"
  val slf4j = "1.7.25"
}



object Common {
  lazy val settings = Seq(
    organization := "ai.bleckwen.teastream",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.12",
    libraryDependencies ++= commonDependencies,
    libraryDependencies ++= testDependencies,
    libraryDependencies ++= logDependencies,
    libraryDependencies ++= configDependencies,
    assemblyMergeStrategy in assembly := {
      case "reference.conf" => MergeStrategy.concat
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case _ => MergeStrategy.last
    }


  )
  lazy val kafkaDependencies = Seq(
    "org.apache.kafka" %% "kafka" % Version.kafka
  )
  lazy val flinkDependencies = Seq(
    "org.apache.flink" %% "flink-streaming-scala" % Version.flink,
    "org.apache.flink" % "flink-core" % Version.flink,
    "org.apache.flink" %% "flink-scala" % Version.flink,
    "org.apache.flink" %% "flink-connector-kafka-base" % Version.flink,
    "org.apache.flink" %% "flink-connector-kafka-0.10" % Version.flink
  )
  lazy val configDependencies = Seq(
    "com.typesafe" % "config" % "1.2.1"
  )
  lazy val commonDependencies = Seq(
    "net.liftweb" %% "lift-json" % "2.6.3"
  )
  lazy val logDependencies = Seq(
    "org.slf4j" % "slf4j-api" % Version.slf4j,
    "log4j" % "log4j" % Version.log4j
  )
  lazy val testDependencies = Seq(
    "org.scalactic" %% "scalactic" % Version.scalaTest % Test,
    "org.scalatest" %% "scalatest" % Version.scalaTest % Test
  )
  lazy val h2oDependencies = Seq(
    "ai.h2o" %% "h2o-scala" % "3.22.0.1" pomOnly(),
    "ai.h2o" % "h2o-genmodel" % "3.22.0.1" % "runtime"
  )
  lazy val twitterDependencies =  Seq(
    "com.twitter" % "hbc-core" % "2.2.0"
  )
}
