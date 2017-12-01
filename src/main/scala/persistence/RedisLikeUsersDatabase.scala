package persistence

object RedisLikeUsersDatabase {

  private val db = Map(
    ("1234", "deezer") -> "13349100",
    ("1234", "spotify") -> "spotify_id" //
  )

  def getUserId(id: String, service: String): Option[String] = db.get((id, service))
}