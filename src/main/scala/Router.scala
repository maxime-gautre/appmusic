package com.zengularity.appmusic

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.zengularity.appmusic.services.BusinessService
import com.zengularity.appmusic.ws.StreamingClient
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext

class Router(businessService: BusinessService)(implicit ec: ExecutionContext) {

  val instance: Route = {
    pathPrefix("api") {
      (get & path("albums")) {
        complete {
          businessService.getAllAlbums()
          StatusCodes.OK
        }
      }
    } ~ {
      (get & path("album" / Segment )) { id =>
        complete {
          businessService.getAlbumDetails()
          StatusCodes.OK
        }
      }
    } ~ {
      (get & path("sync")) {
        complete {
          businessService.sync()
          StatusCodes.OK
        }
      }
    } ~ {
      (get & path("export" / Segment )) { id =>
        complete {
          businessService.exportToExternalService()
          StatusCodes.OK
        }
      }
    }
  }
}