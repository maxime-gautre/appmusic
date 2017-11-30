package com.zengularity.appmusic

import scala.concurrent.ExecutionContext

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import play.api.libs.json.Json

import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._

import com.zengularity.appmusic.ws.DeezerClient

class Router(deezerClient: DeezerClient)(implicit ec: ExecutionContext) {

  val instance: Route = {
    pathPrefix("deezer") {
      (get & path("user" / Segment)) { id =>
        complete {
          deezerClient.userData(id).map {
            case Left(err) => (StatusCodes.BadRequest, Json.obj("error" -> err))
            case Right(user) => (StatusCodes.OK, Json.obj("data" -> user))
          }
        }
      } ~ {
        (get & path("user" / Segment / "albums")) { id =>
          complete {
            deezerClient.userAlbums(id).map {
              case Left(err) => (StatusCodes.BadRequest, Json.obj("error" -> err))
              case Right(albums) => (StatusCodes.OK, Json.obj("data" -> albums))
            }
          }
        }
      }
    }
  }
}
