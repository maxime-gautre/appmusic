package helpers

import java.util.UUID

import reactivemongo.bson.{BSONHandler, BSONString}

object BsonHelpers {

  implicit val bsonUUID: BSONHandler[BSONString, UUID] = BSONHandler(
    { uuid: BSONString => UUID.fromString(uuid.value) },
    { uuid: UUID => BSONString(uuid.toString) }
  )
}
