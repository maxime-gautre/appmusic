package com.zengularity.appmusic

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import org.slf4j.LoggerFactory

object App {

  def main(args: Array[String]): Unit = {

    val logger = LoggerFactory.getLogger(getClass)
    implicit val actorSystem: ActorSystem = ActorSystem("scripts")
    implicit val mat = ActorMaterializer()
    implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher

    val done = Http(actorSystem).bindAndHandle(Route.handlerFlow(Router.router), "localhost", 9000)

    done.andThen { case _ =>
      logger.info("Closing resources...")
      actorSystem.terminate()
      mat.shutdown()
      logger.info("Closing resources... OK")
      ()
    }
  }
}
