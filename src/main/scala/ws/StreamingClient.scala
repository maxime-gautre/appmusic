package com.zengularity.appmusic.ws

import scala.concurrent.{ExecutionContext, Future}

import play.api.libs.json.JsValue

import com.zengularity.appmusic.models.AppMusicModels

trait StreamingClient {
  def userData(userId: String)(implicit ec: ExecutionContext): Future[Either[String, JsValue]]
  def userPlaylist(userId: String)(implicit ec: ExecutionContext): Future[Either[String, List[AppMusicModels.Playlist]]]
  def userAlbums(userId: String)(implicit ec: ExecutionContext): Future[Either[String, List[AppMusicModels.Album]]]
  def album(id: String)(implicit  ec: ExecutionContext): Future[Either[String, AppMusicModels.Album]]
  def albumLike(name: String, artist: String)(implicit  ec: ExecutionContext): Future[Either[String, AppMusicModels.Album]]
  def saveAlbum(id: String)(implicit  ec: ExecutionContext): Unit
}

