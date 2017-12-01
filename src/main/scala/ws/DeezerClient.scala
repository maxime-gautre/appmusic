package com.zengularity.appmusic.ws

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

import play.api.libs.json.{JsSuccess, JsValue, Json, Reads, _}
import play.api.libs.ws.ahc.StandaloneAhcWSClient

import com.zengularity.appmusic.models.{AppMusicModels, DeezerModels}

class DeezerClient(wsClient: StandaloneAhcWSClient, deezerEndPoint: String) extends StreamingClient {

  private def parseResponse[A: Reads](body: String): Either[String, A] = {
    Try(Json.parse(body)).map { data =>
      (data \ "data").validate[A]
    } match {
      case Success(JsSuccess(playlist, _)) => Right(playlist)
      case Success(JsError(err)) => println(err)
        Left(s"Cannot decode json playlist $body")
      case _ => Left(s"Cannot decode json playlist $body")
    }
  }

  private def eitherFuture[A, B](list: List[A])(f: A => Future[Either[String, B]])(implicit ec: ExecutionContext): Future[Either[String, List[B]]] = {
    list.foldLeft[Future[Either[String, List[B]]]](Future.successful(Right(List.empty[B]))) { case (prev, current) =>
      prev.flatMap {
        case Right(data) => f(current).map { _.map(x => data :+ x) }
        case Left(err) => Future.successful(Left(err))
      }
    }
  }

  def userData(userId: String)(implicit ec: ExecutionContext): Future[Either[String, JsValue]] = {
    wsClient.url(s"$deezerEndPoint/user/$userId").get().map { response =>
      Try(Json.parse(response.body)) match {
        case Success(value) => Right(value)
        case Failure(err) => Left(s"Cannot parse json $err")
      }
    }
  }

  def userPlaylist(userId: String)(implicit ec: ExecutionContext): Future[Either[String, List[AppMusicModels.Playlist]]] = {

    wsClient.url(s"$deezerEndPoint/user/$userId/playlists").get().map { response =>
      parseResponse[List[DeezerModels.PlaylistSimplified]](response.body)
    }.flatMap {
      case Right(playlists) => eitherFuture(playlists) { playlistSimplified =>
        wsClient.url(playlistSimplified.tracklist).get().map { response =>
          parseResponse[List[DeezerModels.Track]](response.body).map { tracklist =>
            DeezerModels.Playlist.fromSimplified(playlistSimplified, tracklist)
          }
        }
      }
      case Left(err) => Future.successful(Left(err))
    }
  }.map(_.map(_.map(AppMusicModels.ConvertPlaylist.convert(_)(AppMusicModels.Instances.playlistConverterDeezer))))

  def userAlbums(userId: String)(implicit ec: ExecutionContext): Future[Either[String, List[AppMusicModels.Album]]] = {

    wsClient.url(s"$deezerEndPoint/user/$userId/albums").get().map { response =>
      parseResponse[List[DeezerModels.AlbumSimplified]](response.body)
    }.flatMap {
      case Right(albums) => eitherFuture(albums) { albumSimplified =>
        wsClient.url(albumSimplified.tracklist).get().map { response =>
          parseResponse[List[DeezerModels.Track]](response.body).map { tracklist =>
            DeezerModels.Album.fromSimplified(albumSimplified, tracklist)
          }
        }
      }
      case Left(err) => Future.successful(Left(err))
    }
  }.map(_.map(_.map(AppMusicModels.ConvertAlbum.convert(_)(AppMusicModels.Instances.albumConverterDeezer))))

  override def album(id: String)(implicit ec: ExecutionContext): Future[Either[String, AppMusicModels.Album]] = ???

  override def albumLike(name: String, artist: String)(implicit ec: ExecutionContext): Future[Either[String, AppMusicModels.Album]] = ???

  override def saveAlbum(id: String)(implicit ec: ExecutionContext) = {

  }
}
