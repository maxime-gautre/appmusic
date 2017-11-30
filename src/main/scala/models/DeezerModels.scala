package com.zengularity.appmusic.models

object DeezerModels {
  case class Artist(id: Int, name: String, link: String)
  case class Track(id: Int, title: String, link: String, duration: Int, artist: Artist)
  case class Playlist(id: Int, title: String, link: String, picture: String, tracks: List[Track])
}

