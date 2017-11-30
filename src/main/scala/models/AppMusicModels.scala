package com.zengularity.appmusic.models

import play.api.libs.json.Json

object AppMusicModels {

  case class Artist(id: String, name: String, uri: String)

  object Artist {
    implicit val jsonWriter = Json.writes[Artist]
  }

  case class Track(id: String, title: String, uri: String, duration: Long, artists: List[Artist])

  object Track {
    implicit val jsonWriter = Json.writes[Track]
  }

  case class Playlist(id: String, title: String, uri: String, imageUri: String, tracks: List[Track])

  object Playlist {
    implicit val jsonWriter = Json.writes[Playlist]
  }
}
