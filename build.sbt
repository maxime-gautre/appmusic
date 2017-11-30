name := "appmusic"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.1",
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "1.1.3",
  "org.reactivemongo" %% "reactivemongo" % "0.12.7",
  "org.slf4j" % "log4j-over-slf4j" % "1.7.25",
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-stream" % "2.5.4",
  "com.typesafe.akka" %% "akka-actor" % "2.5.4"
)
        