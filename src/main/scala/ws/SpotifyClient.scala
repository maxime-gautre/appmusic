package com.zengularity.appmusic.ws

import java.nio.charset.StandardCharsets

import com.typesafe.scalalogging.Logger
import com.zengularity.appmusic.models.{AppMusicModels, SpotifyModels}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.libs.ws.DefaultBodyWritables._
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class SpotifyClient(wsClient: StandaloneAhcWSClient, spotifyEndPoint: String) extends StreamingClient {

  private final val OAUTH_CLIENT_ID: String = "72a27ca374f84ad09e643d29a58ba8ae"
  private final val OAUTH_CLIENT_SECRET: String = "fa6b4f4d89754c1a993216beae469f6c"
  private final val OAUTH_REFRESH_TOKEN: String = "AQD8PFCjjmMJh2Y9KWSatQ1KN5yi1MTgVjv5e0j9y_wrrF0qUeIWDFiGXexFIltENqv42brmYVKTPKI3jX9edSLpDU9me8eHJcFotwOtBgfbkoXp_1RIB5BawmNrvJEb4Jg"
  
  private var accessToken: String = "FAKE" // TODO Default null value and handle it in get()

  private def parseResponse(body: String): Either[String, JsValue] = {
    Try(Json.parse(body)) match {
      case Success(jsonBody) => Right(jsonBody)
      case Failure(err) => Left(s"Cannot parse response $body")
    }
  }

  private def get(path: String, params: (String, String)*)(implicit ec: ExecutionContext): Future[Either[String, JsValue]] = {
    wsClient.url(s"$spotifyEndPoint/$path").addHttpHeaders(("Authorization", s"Bearer ${accessToken}")).withQueryStringParameters(params:_*).get().flatMap { response =>
      response.status match {
        case 401 => refreshToken().flatMap { _ =>
          get(path, params:_*)
        }
        case _ => Future.successful(parseResponse(response.body))
      }
    }
  }

  private def put(path: String, params: (String, String)*)(implicit ec: ExecutionContext): Future[Either[String, JsValue]] = {
    wsClient.url(s"$spotifyEndPoint/$path").addHttpHeaders(("Authorization", s"Bearer ${accessToken}")).withQueryStringParameters(params:_*).put("").flatMap { response =>
      response.status match {
        case 401 => refreshToken().flatMap { _ =>
          put(path, params:_*)
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
    get(s"users/$userId");
  }

  def userPlaylist(userId: String)(implicit ec: ExecutionContext): Future[Either[String, List[AppMusicModels.Playlist]]] = {
    get(s"users/$userId/albums");
    ???
  }

  def userAlbums(userId: String)(implicit ec: ExecutionContext): Future[Either[String, List[AppMusicModels.Album]]] = ???

  def saveAlbum(id: String)(implicit ec: ExecutionContext): Unit = {
    put("me/albums", ("ids", id))
  }

  def album(id: String)(implicit ec: ExecutionContext): Future[Either[String, AppMusicModels.Album]] = {
    get(s"albums/$id").flatMap {
      case Right(json) => {
        Logger(getClass).info(s"BOOM 2 ${json.toString()}")
        val albumResult = json.validate[SpotifyModels.Album]
        val res = albumResult match {
          case JsSuccess(album, _) => Right(AppMusicModels.ConvertAlbum.convert(album)(AppMusicModels.Instances.albumConverterSpotify))
          case JsError(errors) => Left(JsError.toJson(errors).toString())
        }
        Future.successful(res)
      }
      case Left(err) => Future.successful(Left(err))
    }
  }

  def albumLike(name: String, artist: String)(implicit ec: ExecutionContext): Future[Either[String, AppMusicModels.Album]] = {
    get("search", ("type", "album"), ("q", s"""album:"$name" artist:"$artist"""")).flatMap {
      case Right(json) => {
        val albumResult = (json \ "albums" \ "items").validate[Seq[SpotifyModels.AlbumSimplified]]
        albumResult match {
          case JsSuccess(Nil, _) => Future.successful(Left("Empty album"))
          case JsSuccess(albumSimplified :: _, _) => album(albumSimplified.id)
          case JsError(errors) => Future.successful(Left(JsError.toJson(errors).toString()))
        }
      }
      case Left(err) => Future.successful(Left(err))
    }
  }
}
