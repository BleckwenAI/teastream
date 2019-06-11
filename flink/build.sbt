name:= "flink"

//Dependencies for flink subproject
libraryDependencies ++= Common.flinkDependencies
libraryDependencies ++= Common.h2oDependencies
assemblyOutputPath in assembly := baseDirectory.value /"../deploy/flink-job/flink-assembly-0.1-SNAPSHOT.jar"
mainClass in assembly := Some("ai.bleckwen.teastream.Job")


