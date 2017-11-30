package com.zengularity.appmusic.ws

import scala.concurrent.{ExecutionContext, Future}

import play.api.libs.json.{JsValue}

trait StreamingClient {
  def userData(userId: String)(implicit ec: ExecutionContext): Future[Either[String, JsValue]]

  def userAlbums(userId: String)(implicit ec: ExecutionContext): Future[Either[String, JsValue]]
}

