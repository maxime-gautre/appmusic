package com.zengularity.appmusic.models

import play.api.libs.json.Json

object DeezerModels {
  case class Artist(id: Int, name: String, link: String)

  object Artist {
    implicit val jsonReads = Json.reads[Artist]
    implicit val jsonWriter = Json.writes[Artist]
  }

  case class Track(id: Long, title: String, link: String, duration: Int, artist: Artist)

  object Track {
    implicit val jsonReads = Json.reads[Track]
    implicit val jsonWriter = Json.writes[Track]
  }

  case class Playlist(id: Long, title: String, link: String, picture: String, tracks: List[Track])

  object Playlist {
    implicit val jsonReads = Json.reads[Playlist]
    implicit val jsonWriter = Json.writes[Playlist]

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
    implicit val jsonWriter = Json.writes[PlaylistSimplified]
  }
}

