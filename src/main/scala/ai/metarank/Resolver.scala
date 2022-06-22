package ai.metarank

import ai.metarank.Collector.json
import better.files.File
import cats.Id
import cats.data.EitherT
import cats.effect.{ExitCode, IO, IOApp}
import com.snowplowanalytics.iglu.client.Client
import com.snowplowanalytics.snowplow.scalatracker.SelfDescribingJson
import io.circe.parser._

object Resolver extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = for {
    resolverConfig <- IO.fromEither(parse(File("iglu.json").contentAsString))
    clientIO       <- Client.parseDefault[IO](resolverConfig).value
    client         <- IO.fromEither(clientIO)
    result         <- client.check(SelfDescribingJson.apply("iglu:com.minds/view/jsonschema/1-0-0", json)).value
  } yield {
    println(result)
    ExitCode.Success
  }
}
