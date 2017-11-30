name := "appmusic"

version := "0.1"

scalaVersion := "2.12.4"

val akkaVersion = "2.5.4"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.1",
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "1.1.3",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "de.heikoseeberger" %% "akka-http-play-json" % "1.18.1"
)
        