package ai.metarank

import better.files.File
import com.snowplowanalytics.iglu.core.{SchemaKey, SchemaVer, SelfDescribingData}
import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import com.snowplowanalytics.snowplow.analytics.scalasdk.SnowplowEvent.UnstructEvent
import io.circe.Json
import io.circe.parser._

import java.time.Instant
import java.util.UUID

object GenerateSampleEvents {
  def main(args: Array[String]): Unit = {
    val item =
      """
        |{
        |  "event": "item",
        |  "id": "81f46c34-a4bb-469c-8708-f8127cd67d27",
        |  "timestamp": "1599391467000",
        |  "item": "item1",
        |  "fields": [
        |    {"name": "title", "value": "You favourite cat"},
        |    {"name": "color", "value": ["white", "black"]},
        |    {"name": "is_cute", "value": true}
        |  ]
        |}""".stripMargin

    val user    = """{
                          |  "event": "user",
                          |  "id": "81f46c34-a4bb-469c-8708-f8127cd67d27",
                          |  "timestamp": "1599391467000",
                          |  "user": "user1",
                          |  "fields": [
                          |    {"name": "age", "value": 33},
                          |    {"name": "gender", "value": "m"}
                          |  ]
                          |}""".stripMargin
    val ranking = """{
                          |  "event": "ranking",
                          |  "id": "81f46c34-a4bb-469c-8708-f8127cd67d27",
                          |  "timestamp": "1599391467000",
                          |  "user": "user1",
                          |  "session": "session1",
                          |  "fields": [
                          |      {"name": "query", "value": "cat"},
                          |      {"name": "source", "value": "search"}
                          |  ],
                          |  "items": [
                          |    {"id": "item3", "relevancy":  2.0},
                          |    {"id": "item1", "relevancy":  1.0},
                          |    {"id": "item2", "relevancy":  0.5} 
                          |  ]
                          |}""".stripMargin
    val click   = """{
                          |  "event": "interaction",
                          |  "id": "0f4c0036-04fb-4409-b2c6-7163a59f6b7d",
                          |  "ranking": "81f46c34-a4bb-469c-8708-f8127cd67d27",
                          |  "timestamp": "1599391467000",
                          |  "user": "user1",
                          |  "session": "session1",
                          |  "type": "purchase",
                          |  "item": "item1",
                          |  "fields": [
                          |    {"name": "count", "value": 1},
                          |    {"name": "shipping", "value": "DHL"}
                          |  ]
                          |}""".stripMargin
    File("/tmp/ranking.tsv").writeText(encode(ranking, "ranking"))
    File("/tmp/item.tsv").writeText(encode(item, "item"))
    File("/tmp/user.tsv").writeText(encode(user, "user"))
    File("/tmp/interaction.tsv").writeText(encode(click, "interaction"))
    File("/tmp/empty.tsv").writeText(Event.minimal(UUID.randomUUID(), Instant.now(), "a", "a").toTsv)
    File("/tmp/other.tsv").writeText(
      Event
        .minimal(UUID.randomUUID(), Instant.now(), "a", "a")
        .copy(unstruct_event =
          UnstructEvent(
            Some(
              SelfDescribingData(
                SchemaKey("com.example", "example", "jsonschema", SchemaVer.Full(1, 0, 0)),
                Json.fromString("oops")
              )
            )
          )
        )
        .toTsv
    )
  }

  def encode(event: String, schema: String) = {
    val json = parse(event).right.get
    Event
      .minimal(UUID.randomUUID(), Instant.now(), "dev", "dev")
      .copy(unstruct_event =
        UnstructEvent(
          Some(SelfDescribingData(SchemaKey("ai.metarank", schema, "jsonschema", SchemaVer.Full(1, 0, 0)), json))
        )
      )
      .toTsv
  }
}
