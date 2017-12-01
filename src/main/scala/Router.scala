package com.zengularity.appmusic

import scala.concurrent.ExecutionContext

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import play.api.libs.json.Json

import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._

import com.zengularity.appmusic.models.SynchronizeBody
import com.zengularity.appmusic.services.AlbumsService

class Router(albumsService: AlbumsService)(implicit ec: ExecutionContext) {

  val instance: Route = {
    pathPrefix("api") {
      (get & path("albums")) {
        complete {
          albumsService.albums.map { albums =>
            Json.toJson(albums)
          }
        }
      } ~ {
        (get & path("album" / Segment)) { id =>
          complete {
            albumsService.getAlbumById(id).map { album =>
              Json.toJson(album)
            }
          }
        }
      } ~ {
        (get & path("sync")) {
          complete {
            StatusCodes.OK
          }
        }
      } ~ {
        (get & path("import" / Segment)) { id =>
          complete {
            StatusCodes.OK
          }
        }
      } ~ {
        (post & path("synchronize") & entity(as[SynchronizeBody])) { synchronizeBody =>
          complete {
            albumsService.synchronize(synchronizeBody.id, synchronizeBody.service).map { _ =>
              StatusCodes.OK -> Json.obj("Synchronized" -> "Ok")
            }
          }
        }
      }
    }
  }
}