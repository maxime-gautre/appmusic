package com.zengularity.appmusic.models

import play.api.libs.json.Json

object DeezerModels {
  case class Artist(id: Int, name: String, link: String)

  object Artist {
    implicit val jsonReads = Json.reads[Artist]
  }

  case class Track(id: Int, title: String, link: String, duration: Int, artist: Artist)

  object Track {
    implicit val jsonReads = Json.reads[Track]
  }

  case class Playlist(id: Int, title: String, link: String, picture: String, tracks: List[Track])

  object Playlist {
    implicit val jsonReads = Json.reads[Playlist]
  }
}

