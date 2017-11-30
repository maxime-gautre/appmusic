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

  case class Playlist(id: String, title: String, uri: String, imageUri: String, tracks: List[Track])

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

  object ConvertArtistFromSpotify {
    def convert(data : SpotifyModels.Artist)(implicit converter: ArtistConverter[SpotifyModels.Artist]) = converter.convert(data)
  }

  object ConvertTrackFromSpotify {
    def convert(data : SpotifyModels.Track)(implicit converter: TrackConverter[SpotifyModels.Track]) = converter.convert(data)
  }

  object ConvertPlaylistFromSpotify {
    def convert(data : SpotifyModels.Playlist)(implicit converter: PlaylistConverter[SpotifyModels.Playlist]) = converter.convert(data)
  }


  object Instances {
    implicit val artistConverter: ArtistConverter[SpotifyModels.Artist] = new ArtistConverter[SpotifyModels.Artist] {
      override def convert(data: SpotifyModels.Artist): Artist = {
        Artist(data.id, data.name, data.uri)
      }
    }

    implicit val trackConverter: TrackConverter[SpotifyModels.Track] = new TrackConverter[SpotifyModels.Track] {
      override def convert(data: SpotifyModels.Track): Track = {
        Track(data.id, data.title, data.uri, data.duration_ms / 1000, data.artists.map(ConvertArtistFromSpotify.convert))
      }
    }

    implicit val playlistConverter: PlaylistConverter[SpotifyModels.Playlist] = new PlaylistConverter[SpotifyModels.Playlist] {
      override def convert(data: SpotifyModels.Playlist): Playlist = {
        Playlist(data.id, data.name, data.uri, data.images.head.url, data.tracks.map(ConvertTrackFromSpotify.convert))
      }
    }
  }


}
