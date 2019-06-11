
name := "teastream"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/contents/repositories/snapshots"
resolvers += "mvnrepository" at "http://mvnrepository.com/artifact/"
resolvers += "central" at "http://repo1.maven.org/maven2/"
//resolvers += Resolver.sbtPluginRepo("releases")

val flink = (project in file("flink"))
  .settings(Common.settings)

val kafkatwitter = (project in file("kafkatwitter"))
  .settings(Common.settings)

