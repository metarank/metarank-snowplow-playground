name := "metarank-snowplow"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "com.snowplowanalytics" %% "snowplow-scala-tracker-core"           % "1.0.1",
  "com.snowplowanalytics" %% "snowplow-scala-tracker-emitter-http4s" % "1.0.1",
  "com.snowplowanalytics" %% "snowplow-scala-analytics-sdk"          % "2.1.0",
  "org.http4s"            %% "http4s-blaze-client"                   % "0.22.4",
  "com.snowplowanalytics" %% "iglu-scala-client"                     % "1.1.1",
  "com.github.pathikrit"  %% "better-files"                          % "3.9.1",
  "software.amazon.awssdk" % "kinesis"                               % "2.17.215"
)
