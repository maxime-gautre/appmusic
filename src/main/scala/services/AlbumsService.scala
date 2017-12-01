package com.zengularity.appmusic.services

import scala.concurrent.{ExecutionContext, Future}
import persistence.{MongoAlbumsPersistence, RedisLikeUsersDatabase}
import com.zengularity.appmusic.AppContext
import com.zengularity.appmusic.models.AppMusicModels
import com.zengularity.appmusic.models.AppMusicModels.Album
import com.zengularity.appmusic.ws.{DeezerClient, SpotifyClient, StreamingClient}

class AlbumsService(streamingClients: Map[String, StreamingClient], persistence: MongoAlbumsPersistence) {
  def saveAlbum(internalId: String)(implicit ex: ExecutionContext) = {
    streamingClients.get("spotify").map { streamingClient =>
      getAlbumById(internalId).map {
        case Some(album) =>
          for {
            spotifyAlbum <- streamingClient.albumLike(album.title, album.artists.head.name)
            sdsd = streamingClient.saveAlbum(spotifyAlbum.right.get.id.toString)
          } yield ()
        case None => {}
      }
    }  }

  def synchronize(userId: String, service: String)(implicit ec: ExecutionContext): Future[Either[String, Unit]] = {
    RedisLikeUsersDatabase.getUserId(userId, service).map { serviceId =>
      streamingClients.get(service).map { streamingClient =>
        streamingClient.userAlbums(serviceId).flatMap {
          case Left(err) => Future.successful(Left(err))
          case Right(albums) => persistence.save(albums).map(Right(_))
        }
      }.getOrElse(Future.successful(Left(s"service $service not found")))
    }.getOrElse(Future.successful(Left(s"Wrong id $userId")))
  }

  def albums(implicit ec: ExecutionContext): Future[List[AppMusicModels.Album]] = persistence.allAlbums

  def getAlbumById(id: String)(implicit ex: ExecutionContext): Future[Option[Album]] = persistence.getAlbumById(id)
}

object AlbumsService {
  def apply(appContext: AppContext): AlbumsService =
    new AlbumsService(
      Map(
        "deezer" -> new DeezerClient(appContext.wsClient, "https://api.deezer.com"),
        "spotify" -> new SpotifyClient(appContext.wsClient, "https://api.spotify.com/v1")
      ),
      new MongoAlbumsPersistence(appContext.mongoConnection, appContext.dbName)
    )
}
