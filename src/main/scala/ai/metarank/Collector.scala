package ai.metarank

import cats.data.NonEmptyList
import cats.effect.{ExitCode, IO, IOApp}
import com.snowplowanalytics.snowplow.scalatracker.Emitter.EndpointParams
import com.snowplowanalytics.snowplow.scalatracker.{SelfDescribingJson, Tracker}
import com.snowplowanalytics.snowplow.scalatracker.emitters.http4s.Http4sEmitter
import io.circe.{Json, JsonObject}
import org.http4s.blaze.client.BlazeClientBuilder

object Collector extends IOApp {
  val json = Json.fromJsonObject(
    JsonObject.fromMap(
      Map(
        "medium"            -> Json.fromString("feed"),
        "position"          -> Json.fromInt(1),
        "source"            -> Json.fromString("feed/subscribed"),
        "entity_owner_guid" -> Json.fromString("775514467126485008"),
        "campaign"          -> Json.fromString(""),
        "platform"          -> Json.fromString("mobile"),
        "entity_guid"       -> Json.fromString("1357841627493699585"),
        "page_token"        -> Json.fromString("09351419-49d03d4d-954958d9-f94f1e19-a1fe144a-02ca7f76"),
        "delta"             -> Json.fromInt(0)
      )
    )
  )
  override def run(args: List[String]): IO[ExitCode] = {
    val tracker = for {
      client  <- BlazeClientBuilder[IO](executionContext).resource
      emitter <- Http4sEmitter.build[IO](EndpointParams("localhost", port = Some(8080), https = false), client)
    } yield {
      new Tracker(NonEmptyList.of(emitter), "com.minds", "test")
    }
    tracker
      .use(
        _.trackSelfDescribingEvent(
          unstructEvent = SelfDescribingJson.apply("iglu:com.minds/view/jsonschema/1-0-0", json)
        )
      )
      .map(_ => ExitCode.Success)
  }
}
