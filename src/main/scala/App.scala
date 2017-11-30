package com.zengularity.appmusic

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.io.StdIn

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import play.api.libs.ws.ahc.StandaloneAhcWSClient

import com.typesafe.scalalogging.Logger

import com.zengularity.appmusic.ws.DeezerAhcClient


object App {

  def main(args: Array[String]): Unit = {

    val logger = Logger(getClass)
    implicit val actorSystem: ActorSystem = ActorSystem("scripts")
    implicit val mat = ActorMaterializer()
    implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher

    val wsClient = StandaloneAhcWSClient()
    val router = new Router(new DeezerAhcClient(wsClient, "https://api.deezer.com"))

    val bindingFuture = Http(actorSystem).bindAndHandle(Route.handlerFlow(router.instance), "localhost", 9000)
    logger.info("Server on localhost:9000")
    logger.info("Press enter to stop")

    StdIn.readLine()

    bindingFuture
      .flatMap(_.unbind())
      .onComplete { _ =>
        logger.info("Closing resources...")
        wsClient.close()
        mat.shutdown()
        actorSystem.terminate()
        logger.info("Closing resources... OK")
      }
  }
}
