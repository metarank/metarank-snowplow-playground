package ai.metarank

import cats.data.NonEmptyList
import cats.effect.{ExitCode, IO, IOApp}
import com.snowplowanalytics.snowplow.scalatracker.Emitter.EndpointParams
import com.snowplowanalytics.snowplow.scalatracker.Tracker
import com.snowplowanalytics.snowplow.scalatracker.emitters.http4s.Http4sEmitter
import org.http4s.blaze.client.BlazeClientBuilder

object Collector extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val tracker = for {
      client  <- BlazeClientBuilder[IO](executionContext).resource
      emitter <- Http4sEmitter.build[IO](EndpointParams("localhost", port = Some(8080), https = false), client)
    } yield {
      new Tracker(NonEmptyList.of(emitter), "com.minds", "test")
    }
    tracker
      .use(t => {
        t.trackPageView(pageUrl = "http://www.example.com/test")
      })
      .map(_ => ExitCode.Success)
  }
}
