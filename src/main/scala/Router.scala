package com.zengularity.appmusic

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._

import play.api.libs.json.Json

object Router {

  val router: Route =
    path("hello") {
      get {
        complete {
          Json.obj("text" -> "Hello World")
        }
      }
    }
}
