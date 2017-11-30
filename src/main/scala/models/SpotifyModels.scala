package com.zengularity.appmusic.models

object SpotifyModels {
  case class Artist(id: String, name: String, uri: String)
  case class Track(id: String, title: String, uri: String, duration_ms: Int, artists: List[Artist])
  case class Image(url: String, height: Int, width: Int)
  case class Playlist(id: String, name: String, uri: String, images: List[Image], tracks: List[Track])
}

