name := "kafka-producer"

libraryDependencies ++= Common.kafkaDependencies
libraryDependencies ++= Common.twitterDependencies
assemblyOutputPath in assembly := baseDirectory.value /"../deploy/producer-job/producer-assembly-0.1-SNAPSHOT.jar"
mainClass in assembly := Some("ai.bleckwen.teastream.Requests")
