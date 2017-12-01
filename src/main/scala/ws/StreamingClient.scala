package com.zengularity.appmusic.ws

import scala.concurrent.{ExecutionContext, Future}

import play.api.libs.json.JsValue

import com.zengularity.appmusic.models.AppMusicModels

trait StreamingClient {
  def userData(userId: String)(implicit ec: ExecutionContext): Future[Either[String, JsValue]]

  def userPlaylist(userId: String)(implicit ec: ExecutionContext): Future[Either[String, List[AppMusicModels.Playlist]]]
}

