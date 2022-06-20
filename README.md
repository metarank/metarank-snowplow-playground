# Snowplow local 

A set of config files and docker-compose manifest to run a Snowplow Analytics server
locally, with setup replicating the one from com.minds.

## What's inside

The docker-compose.yml manifests does the following:
* Localstack based API mocks for kinesis, dynamodb and cloudwatch
* Snowplow Collector writing to raw Kinesis streams
* Snowplow Enrich reads raw events, validates them via the com.minds schema, and pushes forward to a next Kinesis stream

## Usage

```bash
git clone <this repo>
git submodule sync
docker compose up
```

## License

For the com.minds iglu schemas, see the corresponsing license in the submodule: https://gitlab.com/minds/iglu-registry/-/blob/master/LICENSE.md

For the remaining code: Apache 2.0