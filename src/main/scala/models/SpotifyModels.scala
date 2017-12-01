package com.zengularity.appmusic.models

import play.api.libs.json.Json

object SpotifyModels {

  case class Artist(id: String, name: String, uri: String) 

  object Artist {
    implicit val jsonReads = Json.reads[Artist]
  }

  case class Track(id: String, name: String, uri: String, duration_ms: Int, artists: List[Artist])

  object Track {
    implicit val jsonReads = Json.reads[Track]
  }

  case class TrackList(href: String, items: List[Track])

  object TrackList {
    implicit val jsonReads = Json.reads[TrackList]
  }

  case class Image(url: String, height: Int, width: Int)

  object Image {
    implicit val jsonReads = Json.reads[Image]
  }

  case class Playlist(id: String, name: String, uri: String, images: List[Image], tracks: List[Track])

  object Playlist {
    implicit val jsonReads = Json.reads[Playlist]
  }

  case class AlbumSimplified(id: String, name: String, uri: String)

  object AlbumSimplified {
    implicit val jsonReads = Json.reads[AlbumSimplified]
  }

  case class Album(id: String, name: String, uri: String, images: List[Image], tracks: TrackList, artists: List[Artist])

  object Album {
    implicit val jsonReads = Json.reads[Album]
  }
}

