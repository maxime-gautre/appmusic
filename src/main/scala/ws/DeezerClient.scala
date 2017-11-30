package com.zengularity.appmusic.ws

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.ahc.StandaloneAhcWSClient

class DeezerAhcClient(wsClient: StandaloneAhcWSClient, deezerEndPoint: String) extends StreamingClient {

  private def parseResponse(body: String): Either[String, JsValue] = {
    Try(Json.parse(body)) match {
      case Success(jsonBody) => Right(jsonBody)
      case Failure(err) => Left(s"Cannot parse response $body")
    }
  }

  def userData(userId: String)(implicit ec: ExecutionContext): Future[Either[String, JsValue]] = {
    wsClient.url(s"$deezerEndPoint/user/$userId").get().map { response =>
      parseResponse(response.body)
    }
  }

  def userAlbums(userId: String)(implicit ec: ExecutionContext): Future[Either[String, JsValue]] = {
    wsClient.url(s"$deezerEndPoint/user/$userId/albums").get().map { response =>
      parseResponse(response.body)
    }
  }
}
