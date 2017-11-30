package com.zengularity.appmusic.models

object AppMusicModels {
  case class Artist(id: String, name: String, uri: String)
  case class Track(id: String, title: String, uri: String, duration: Long, artists: List[Artist])
  case class Playlist(id: String, title: String, uri: String, imageUri: String, tracks: List[Track])
}
