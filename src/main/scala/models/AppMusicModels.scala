package com.zengularity.appmusic.models

import java.util.UUID

import play.api.libs.json.Json

import reactivemongo.bson.Macros

object AppMusicModels {

  import helpers.BsonHelpers.bsonUUID

  case class Origin(id: String, service: String)

  object Origin {
    implicit val jsonWrites = Json.writes[Origin]
    implicit val bsonHandler = Macros.handler[Origin]
  }

  case class Artist(id: String, name: String)

  object Artist {
    implicit val jsonWriter = Json.writes[Artist]
    implicit val bsonHandler = Macros.handler[Artist]
  }

  case class Track(id: String, title: String, uri: String, duration: Int, artists: List[Artist])

  object Track {
    implicit val jsonWriter = Json.writes[Track]
    implicit val bsonHandler = Macros.handler[Track]
  }

  case class Playlist(id: UUID, title: String, uri: String, imageUri: String, tracks: List[Track], origin: Origin)

  object Playlist {
    implicit val jsonWriter = Json.writes[Playlist]
    implicit val bsonHandler = Macros.handler[Playlist]
  }

  case class Album(id: UUID, title: String, uri: String, cover: String, nbTracks: Int, tracks: List[Track], artist: Artist, origin: Origin)

  object Album {
    implicit val jsonWriter = Json.writes[Album]
    implicit val bsonHandler = Macros.handler[Album]
  }

  trait ArtistConverter[A] {
    def convert(data: A): Artist
  }

  trait TrackConverter[A] {
    def convert(data: A): Track
  }

  trait PlaylistConverter[A] {
    def convert(data: A): Playlist
  }

  trait AlbumConverter[A] {
    def convert(data: A): Album
  }

  object ConvertArtist {
    def convert[A](data : A)(implicit converter: ArtistConverter[A]) = converter.convert(data)
  }

  object ConvertTrack {
    def convert[A](data : A)(implicit converter: TrackConverter[A]) = converter.convert(data)
  }

  object ConvertPlaylist {
    def convert[A](data : A)(implicit converter: PlaylistConverter[A]) = converter.convert(data)
  }

  object ConvertAlbum {
    def convert[A](data: A)(implicit converter: AlbumConverter[A]) = converter.convert(data)
  }

  object Instances {
    implicit val artistConverterSpotify: ArtistConverter[SpotifyModels.Artist] = new ArtistConverter[SpotifyModels.Artist] {
      override def convert(data: SpotifyModels.Artist): Artist = {
        Artist(data.id, data.name)
      }
    }

    implicit val trackConverterSpotify: TrackConverter[SpotifyModels.Track] = new TrackConverter[SpotifyModels.Track] {
      override def convert(data: SpotifyModels.Track): Track = {
        Track(data.id, data.title, data.uri, data.duration_ms / 1000, data.artists.map(ConvertArtist.convert(_)))
      }
    }

    implicit val playlistConverterSpotify: PlaylistConverter[SpotifyModels.Playlist] = new PlaylistConverter[SpotifyModels.Playlist] {
      override def convert(data: SpotifyModels.Playlist): Playlist = {
        Playlist(UUID.randomUUID(), data.name, data.uri, data.images.head.url, data.tracks.map(ConvertTrack.convert(_)), Origin(data.id, "spotify"))
      }
    }

    implicit val artistConverterDeezer: ArtistConverter[DeezerModels.Artist] = new ArtistConverter[DeezerModels.Artist] {
      override def convert(data: DeezerModels.Artist): Artist = {
        Artist(data.id.toString, data.name)
      }
    }

    implicit val trackConverterDeezer: TrackConverter[DeezerModels.Track] = new TrackConverter[DeezerModels.Track] {
      override def convert(data: DeezerModels.Track): Track = {
        Track(data.id.toString, data.title, data.link, data.duration, List(ConvertArtist.convert(data.artist)))
      }
    }

    implicit val playlistConverterDeezer: PlaylistConverter[DeezerModels.Playlist] = new PlaylistConverter[DeezerModels.Playlist] {
      def convert(data: DeezerModels.Playlist): Playlist = {
        Playlist(UUID.randomUUID(), data.title, data.link, data.picture, data.tracks.map(ConvertTrack.convert(_)), Origin(data.id.toString, "deezer"))
      }
    }

    implicit val albumConverterDeezer = new AlbumConverter[DeezerModels.Album] {
      def convert(data: DeezerModels.Album): Album = {
        Album(UUID.randomUUID(),
          data.title,
          data.link,
          data.cover,
          data.nbTracks,
          data.tracks.map(ConvertTrack.convert(_)),
          ConvertArtist.convert(data.artist),
          Origin(data.id.toString, "deezer"))
      }
    }
  }
}