package com.zengularity.appmusic

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import com.typesafe.scalalogging.Logger


object App {

  def main(args: Array[String]): Unit = {

    val logger = Logger(getClass)
    implicit val actorSystem: ActorSystem = ActorSystem("scripts")
    implicit val mat = ActorMaterializer()
    implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher

    val bindingFuture = Http(actorSystem).bindAndHandle(Route.handlerFlow(Router.router), "localhost", 9000)
    logger.info("Server on localhost:9000")
    logger.info("Press enter to stop")

    StdIn.readLine()

    bindingFuture
      .flatMap(_.unbind())
      .andThen { case _ =>
        logger.info("Closing resources...")
        mat.shutdown()
        actorSystem.terminate()
        logger.info("Closing resources... OK")
      }
  }
}
