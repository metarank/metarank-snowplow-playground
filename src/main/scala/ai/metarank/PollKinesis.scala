package ai.metarank

import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.kinesis.KinesisClient
import software.amazon.awssdk.services.kinesis.model.{GetRecordsRequest, GetShardIteratorRequest, ShardIteratorType}
import scala.jdk.CollectionConverters._
import java.net.URI

object PollKinesis {
  def main(args: Array[String]): Unit = {
    val client =
      KinesisClient.builder().endpointOverride(new URI("http://localhost:4566")).region(Region.US_EAST_1).build()
    val topics = client.listStreams()
    println(s"topics: ${topics.streamNames().asScala.toList}")

    val iterator = client.getShardIterator(
      GetShardIteratorRequest
        .builder()
        .shardIteratorType(ShardIteratorType.TRIM_HORIZON)
        .shardId("shardId-000000000000")
        .streamName("enrich")
        .build()
    )

    val records = client.getRecords(GetRecordsRequest.builder().shardIterator(iterator.shardIterator()).build())
    records.records().asScala.foreach(r => println(new String(r.data().asByteArray())))
  }
}
