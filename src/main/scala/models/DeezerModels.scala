package com.zengularity.appmusic.models

import play.api.libs.json.Json

object DeezerModels {
  case class Artist(id: Long, name: String)

  object Artist {
    implicit val jsonReads = Json.reads[Artist]
  }

  case class Track(id: Long, title: String, link: String, duration: Int, artist: Artist)

  object Track {
    implicit val jsonReads = Json.reads[Track]
  }

  case class Playlist(id: Long, title: String, link: String, picture: String, tracks: List[Track])

  object Playlist {
    def fromSimplified(playlistSimplified: PlaylistSimplified, tracklist: List[Track]) = {
      Playlist(
        playlistSimplified.id,
        playlistSimplified.title,
        playlistSimplified.link,
        playlistSimplified.picture,
        tracklist
      )
    }
  }

  case class PlaylistSimplified(id: Long, title: String, link: String, picture: String, tracklist: String)

  object PlaylistSimplified {
    implicit val jsonReads = Json.reads[PlaylistSimplified]
  }

  case class AlbumSimplified(id: Long, title: String, link: String, cover: String, nb_tracks: Int, tracklist: String, artist: Artist)

  object AlbumSimplified {
    implicit val jsonReads = Json.reads[AlbumSimplified]
  }

  case class Album(id: Long, title: String, link: String, cover: String, nbTracks: Int, tracks: List[Track], artist: Artist)

  object Album {
    def fromSimplified(albumSimplified: AlbumSimplified, tracklist: List[Track]): Album = {
      Album(
        albumSimplified.id,
        albumSimplified.title,
        albumSimplified.link,
        albumSimplified.cover,
        albumSimplified.nb_tracks,
        tracklist,
        Artist(
          albumSimplified.artist.id,
          albumSimplified.artist.name
        )
      )
    }
  }
}

