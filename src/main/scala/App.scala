package com.zengularity.appmusic

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.Failure

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger

import com.zengularity.appmusic.services.AlbumsService

object App {

  def main(args: Array[String]): Unit = {

    implicit val actorSystem: ActorSystem = ActorSystem("scripts")
    implicit val mat = ActorMaterializer()
    implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher

    val logger = Logger(getClass)
    for {
      appContext <- AppContext(ConfigFactory.load()).recoverWith { case err =>
        mat.shutdown()
        actorSystem.terminate()
        Failure(err)
      }
    } yield {
      val router = new Router(AlbumsService(appContext))

      val bindingFuture = Http(actorSystem).bindAndHandle(Route.handlerFlow(router.instance), "localhost", 9000)
      logger.info("Server on localhost:9000")
      logger.info("Press enter to stop")

      StdIn.readLine()

      bindingFuture
        .flatMap(_.unbind())
        .onComplete { _ =>
          logger.info("Closing resources...")
          appContext.close()
          mat.shutdown()
          actorSystem.terminate()
          logger.info("Closing resources... OK")
        }
    }
  }
}
