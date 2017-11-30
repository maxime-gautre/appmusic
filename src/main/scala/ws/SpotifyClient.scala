package com.zengularity.appmusic.ws

import java.nio.charset.StandardCharsets

import com.typesafe.scalalogging.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.DefaultBodyWritables._
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class SpotifyAhcClient(wsClient: StandaloneAhcWSClient, spotifyEndPoint: String) extends StreamingClient {

  private final val OAUTH_CLIENT_ID: String = "72a27ca374f84ad09e643d29a58ba8ae"
  private final val OAUTH_CLIENT_SECRET: String = "fa6b4f4d89754c1a993216beae469f6c"
  private final val OAUTH_CODE: String = "AQDS8jC5kKOQE-Z1g-td1ScCpdYb9lw0bRT3ddwzgJvweMUoI9XSTyDFySKhpOvCWA2G_Tj-R6Lu-P8ETIuLng6oOPO8k-qZ548aDphppPCcKzraLUwbUpikdREqvy4t40HHsAkp1aIdO22PrYgCm_QmafJgpWH56gz_gKFS8ne-G6THI94L68ChI_reZRvLjFXkuxg6Uang2fiRTEYF3RLcEcuTRoY8ZbbWr9_KNHc2ye8cVi8JtVw"
  private final val OAUTH_REDIRECT_URI: String = "http://localhost:8888/callback/"
  private final val OAUTH_REFRESH_TOKEN: String = "AQCHdgYFcB6F87UXxpvloUAQIdUhrr7cc-wmHyUg95K9p_a-4IzHVTnO432trgrpwQUMlQSOoX6QsbmiS-GmegI4U9i5dkRtW5uZnepE_TsGXGg0H-qxJ9g3ZnK6v1ahJAw"

  private var accessToken: String = "FAKE" // TODO Default null value and handle it in get()

  private def parseResponse(body: String): Either[String, JsValue] = {
    Try(Json.parse(body)) match {
      case Success(jsonBody) => Right(jsonBody)
      case Failure(err) => Left(s"Cannot parse response $body")
    }
  }

  private def get(url: String)(implicit ec: ExecutionContext): Future[Either[String, JsValue]] = {
    wsClient.url(url).addHttpHeaders(("Authorization", s"Bearer ${accessToken}")).get().flatMap { response =>
      response.status match {
        case 401 => refreshToken().flatMap { _ =>
          get(url)
        }
        case _ => Future.successful(parseResponse(response.body))
      }
    }
  }

  private def refreshToken()(implicit ec: ExecutionContext): Future[Unit] = {
    val body = Map (
      "grant_type" -> "refresh_token",
      "refresh_token" -> OAUTH_REFRESH_TOKEN
    )

    val base64 = java.util.Base64.getEncoder.encodeToString(s"$OAUTH_CLIENT_ID:$OAUTH_CLIENT_SECRET".getBytes(StandardCharsets.UTF_8))
    val auth_header = s"Basic $base64"

    wsClient.url("https://accounts.spotify.com/api/token").addHttpHeaders(("Authorization", auth_header)).post(body).map { response =>
      val json = Json.parse(response.body)
      accessToken = (json \ "access_token").as[String]
    }
  }

  def userData(userId: String)(implicit ec: ExecutionContext): Future[Either[String, JsValue]] = {
    get(s"$spotifyEndPoint/users/$userId");
  }

  def userAlbums(userId: String)(implicit ec: ExecutionContext): Future[Either[String, JsValue]] = {
    get(s"$spotifyEndPoint/users/$userId/albums");
  }
}
