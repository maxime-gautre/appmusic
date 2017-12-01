package com.zengularity.appmusic

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.zengularity.appmusic.ws.StreamingClient
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext

class Router(deezerClient: StreamingClient)(implicit ec: ExecutionContext) {

  val instance: Route = {
    pathPrefix("api") {
      (get & path("albums")) {
        complete {
          StatusCodes.OK
        }
      }
    } ~ {
      (get & path("album" / Segment )) { id =>
        complete {
          StatusCodes.OK
        }
      }
    } ~ {
      (get & path("sync")) {
        complete {
          StatusCodes.OK
        }
      }
    } ~ {
      (get & path("import" / Segment )) { id =>
        complete {
          StatusCodes.OK
        }
      }
    }
  }
}