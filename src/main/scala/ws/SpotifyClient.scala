package com.zengularity.appmusic.ws

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.ahc.StandaloneAhcWSClient

class SpotifyAhcClient(wsClient: StandaloneAhcWSClient, spotifyEndPoint: String) extends StreamingClient {

  private def parseResponse(body: String): Either[String, JsValue] = {
    Try(Json.parse(body)) match {
      case Success(jsonBody) => Right(jsonBody)
      case Failure(err) => Left(s"Cannot parse response $body")
    }
  }

  private def get(url: String)(implicit ec: ExecutionContext): Future[Either[String, JsValue]] = {
    wsClient.url(url).addHttpHeaders(("Authorization", s"Bearer ${accessToken()}")).get().map { response =>
      parseResponse(response.body)
    }
  }

  private def accessToken(): String = {
    // FIXME Retrieve token from OAuth code
    "BQBB4LVUtUGJSb-3Y7AmPFFeAxJhY9RZHGm_e50wo-IR0HNkINYB6ZCWxcqz8uNoxSlq2Uv8c8FlWZ7W27S6EwBdQFF5WAm6RC2I5fpafS7Dwd1xHLhNfQUqGuZOcDxnvjQsII68oQBtqocKLz6jN7OSjWONL5VAQ0f-waNMMm5h9bzPNQ"
  }

  def userData(userId: String)(implicit ec: ExecutionContext): Future[Either[String, JsValue]] = {
    get(s"$spotifyEndPoint/users/$userId");
  }

  def userAlbums(userId: String)(implicit ec: ExecutionContext): Future[Either[String, JsValue]] = {
    get(s"$spotifyEndPoint/users/$userId/albums");
  }
}
