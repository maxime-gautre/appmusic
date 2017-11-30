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

  object ConvertArtistFromDeezer {
    def convert(data : DeezerModels.Artist)(implicit converter: ArtistConverter[DeezerModels.Artist]) = converter.convert(data)
  }

  object ConvertTrackFromDeezer {
    def convert(data : DeezerModels.Track)(implicit converter: TrackConverter[DeezerModels.Track]) = converter.convert(data)
  }

  object ConvertPlaylistFromDeezer {
    def convert(data : DeezerModels.Playlist)(implicit converter: PlaylistConverter[DeezerModels.Playlist]) = converter.convert(data)
  }



  object Instances {
    implicit val artistConverterSpotify: ArtistConverter[SpotifyModels.Artist] = new ArtistConverter[SpotifyModels.Artist] {
      override def convert(data: SpotifyModels.Artist): Artist = {
        Artist(data.id, data.name, data.uri)
      }
    }

    implicit val trackConverterSpotify: TrackConverter[SpotifyModels.Track] = new TrackConverter[SpotifyModels.Track] {
      override def convert(data: SpotifyModels.Track): Track = {
        Track(data.id, data.title, data.uri, data.duration_ms / 1000, data.artists.map(ConvertArtistFromSpotify.convert))
      }
    }

    implicit val playlistConverterSpotify: PlaylistConverter[SpotifyModels.Playlist] = new PlaylistConverter[SpotifyModels.Playlist] {
      override def convert(data: SpotifyModels.Playlist): Playlist = {
        Playlist(data.id, data.name, data.uri, data.images.head.url, data.tracks.map(ConvertTrackFromSpotify.convert))
      }
    }

    implicit val artistConverterDeezer: ArtistConverter[DeezerModels.Artist] = new ArtistConverter[DeezerModels.Artist] {
      override def convert(data: DeezerModels.Artist): Artist = {
        Artist(data.id.toString, data.name, data.link)
      }
    }

    implicit val trackConverterDeezer: TrackConverter[DeezerModels.Track] = new TrackConverter[DeezerModels.Track] {
      override def convert(data: DeezerModels.Track): Track = {
        Track(data.id.toString, data.title, data.link, data.duration, List(ConvertArtistFromDeezer.convert(data.artist)))
      }
    }

    implicit val playlistConverterDeezer: PlaylistConverter[DeezerModels.Playlist] = new PlaylistConverter[DeezerModels.Playlist] {
      override def convert(data: DeezerModels.Playlist): Playlist = {
        Playlist(data.id.toString, data.title, data.link, data.picture, data.tracks.map(ConvertTrackFromDeezer.convert))
      }
    }

  }

}
