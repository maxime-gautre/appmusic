package com.zengularity.appmusic

import scala.util.{Failure, Success, Try}

import akka.stream.Materializer

import play.api.libs.ws.ahc.StandaloneAhcWSClient

import com.typesafe.config.Config
import reactivemongo.api.{MongoConnection, MongoDriver}

class AppContext(mongoUri: MongoConnection.ParsedURI)(implicit mat: Materializer) {
  val wsClient = StandaloneAhcWSClient()
  val mongoDriver = MongoDriver()

  val mongoConnection = mongoDriver.connection(mongoUri)

  private val db = mongoUri.db match {
    case Some(n) => Success(n)
    case _ => Failure(new IllegalArgumentException(
      s"Missing DB name in connection URI: $mongoUri"))
  }

  val dbName = db.get

  def close(): Unit = {
    mongoDriver.close()
    wsClient.close
  }
}

object AppContext {
  def apply(config: Config)(implicit mat: Materializer): Try[AppContext] = {
    for {
      mongoUri <- Try(config getString "mongodb.uri")
      parsedUri <- MongoConnection.parseURI(mongoUri)
    } yield new AppContext(parsedUri)
  }
}
