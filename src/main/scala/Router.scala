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
    pathPrefix("deezer") {
      (get & path("user" / Segment)) { id =>
        complete {
          deezerClient.userData(id).map {
            case Left(err) => (StatusCodes.BadRequest, Json.obj("error" -> err))
            case Right(user) => (StatusCodes.OK, Json.obj("data" -> user))
          }
        }
      } ~ {
        (get & path("user" / Segment / "playlists")) { id =>
          complete {
            deezerClient.userPlaylist(id).map {
              case Left(err) => (StatusCodes.BadRequest, Json.obj("error" -> err))
              case Right(albums) => (StatusCodes.OK, Json.toJson(albums))
            }
          }
        }
      }
    }
  }
}
