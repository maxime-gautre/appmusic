package com.zengularity.appmusic.models

import play.api.libs.json.Json

case class SynchronizeBody(id: String, service: String)

object SynchronizeBody {
  implicit val jsonReads = Json.reads[SynchronizeBody]
}