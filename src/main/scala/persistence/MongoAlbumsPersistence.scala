package persistence

import scala.concurrent.{ExecutionContext, Future}

import reactivemongo.api.MongoConnection
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument

import com.zengularity.appmusic.models.AppMusicModels.Album

class MongoAlbumsPersistence(mongoConnection: MongoConnection, dbName: String) {

  import Album.bsonHandler
  private val AlbumsCollection = "albums"

  private def albumCollection(implicit ec: ExecutionContext): Future[BSONCollection] = {
    mongoConnection.database(dbName).map { db =>
      db.collection[BSONCollection](AlbumsCollection)
    }
  }

  def save(albums: List[Album])(implicit ec: ExecutionContext): Future[Unit] = {
    for {
      collection <- albumCollection
      existingAlbums <- allAlbums
      existingAlbumsTitle = existingAlbums.map(_.title)
      newAlbums  = albums.filterNot(album => existingAlbumsTitle.contains(album.title))
      _ <- collection.insert(ordered = true).many(newAlbums)
    } yield ()
  }

  def allAlbums(implicit ec: ExecutionContext): Future[List[Album]] = {
    for {
      collection <- albumCollection
      albums <- collection.find(BSONDocument()).cursor[Album]().collect[List]()
    } yield albums
  }
}
