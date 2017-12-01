package com.zengularity.appmusic.models

import play.api.libs.json.Json

object AppMusicModels {
  case class Artist(id: String, name: String, uri: String)

  object Artist {
    implicit val jsonWriter = Json.writes[Artist]
  }

  case class Track(id: String, title: String, uri: String, duration: Int, artists: List[Artist])

  object Track {
    implicit val jsonWriter = Json.writes[Track]
  }

  case class Playlist(id: String, title: String, uri: String, imageUri: String, tracks: List[Track], origin: String)

  object Playlist {
    implicit val jsonWriter = Json.writes[Playlist]
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

  object ConvertArtist {
    def convert[A](data : A)(implicit converter: ArtistConverter[A]) = converter.convert(data)
  }

  object ConvertTrack {
    def convert[A](data : A)(implicit converter: TrackConverter[A]) = converter.convert(data)
  }

  object ConvertPlaylist {
    def convert[A](data : A)(implicit converter: PlaylistConverter[A]) = converter.convert(data)
  }

  object Instances {
    implicit val artistConverterSpotify: ArtistConverter[SpotifyModels.Artist] = new ArtistConverter[SpotifyModels.Artist] {
      override def convert(data: SpotifyModels.Artist): Artist = {
        Artist(data.id, data.name, data.uri)
      }
    }

    implicit val trackConverterSpotify: TrackConverter[SpotifyModels.Track] = new TrackConverter[SpotifyModels.Track] {
      override def convert(data: SpotifyModels.Track): Track = {
        Track(data.id, data.title, data.uri, data.duration_ms / 1000, data.artists.map(ConvertArtist.convert(_)))
      }
    }

    implicit val playlistConverterSpotify: PlaylistConverter[SpotifyModels.Playlist] = new PlaylistConverter[SpotifyModels.Playlist] {
      override def convert(data: SpotifyModels.Playlist): Playlist = {
        Playlist(data.id, data.name, data.uri, data.images.head.url, data.tracks.map(ConvertTrack.convert(_)), "spotify")
      }
    }

    implicit val artistConverterDeezer: ArtistConverter[DeezerModels.Artist] = new ArtistConverter[DeezerModels.Artist] {
      override def convert(data: DeezerModels.Artist): Artist = {
        Artist(data.id.toString, data.name, data.link)
      }
    }

    implicit val trackConverterDeezer: TrackConverter[DeezerModels.Track] = new TrackConverter[DeezerModels.Track] {
      override def convert(data: DeezerModels.Track): Track = {
        Track(data.id.toString, data.title, data.link, data.duration, List(ConvertArtist.convert(data.artist)))
      }
    }

    implicit val playlistConverterDeezer: PlaylistConverter[DeezerModels.Playlist] = new PlaylistConverter[DeezerModels.Playlist] {
      def convert(data: DeezerModels.Playlist): Playlist = {
        Playlist(data.id.toString, data.title, data.link, data.picture, data.tracks.map(ConvertTrack.convert(_)), "deezer")
      }
    }
  }
}