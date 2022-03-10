package org.demuinck

import com.snowplowanalytics.iglu.core.{SchemaKey, SchemaVer, SelfDescribingData}
import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import com.snowplowanalytics.snowplow.analytics.scalasdk.SnowplowEvent.Contexts
import io.circe.parser
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.test.util.MiniClusterWithClientResource
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.time.Instant
import java.util.UUID


class SnowplowFlinkAggregatorTest extends AnyFunSuite with BeforeAndAfter with Matchers{

  val cluster = new MiniClusterWithClientResource(
    new MiniClusterResourceConfiguration.Builder()
      .setNumberTaskManagers(1)
      .setNumberSlotsPerTaskManager(1)
      .build()
  )

  before {
    cluster.before()
  }

  after {
    cluster.after()
  }

  val env = StreamExecutionEnvironment.getExecutionEnvironment

  test("integration test windowing function") {

    env.setParallelism(2)

    CollectSink.values.clear()

    val now = Instant.now()

    val validUuid = "6ed5cc14-6bad-4964-889b-efa9e51906d1"

    val testStream = env.fromElements(
      Event
        .minimal(UUID.fromString(validUuid), Instant.now(), "Collector", "ETL")
        .copy(
          app_id = Option("org.pdemuinck.web"),
          dvce_type = Option("smartphone"),
          collector_tstamp = now
        )
        .toTsv,
      Event
        .minimal(UUID.fromString(validUuid), Instant.now(), "Collector", "ETL")
        .copy(
          app_id = Option("org.pdemuinck.web"),
          dvce_type = Option("smartphone"),
          collector_tstamp = now
        )
        .toTsv,
      Event
      .minimal(UUID.fromString(validUuid), Instant.now(), "Collector", "ETL")
      .copy(
        app_id = Option("org.pdemuinck.web"),
        dvce_type = Option("smartphone"),
        collector_tstamp = now.plusSeconds(300)
      )
      .toTsv
    )

    val aggregator = SnowplowFlinkAggregator()

    aggregator.aggregate(testStream, List("app_id", "dvce_type"), 5).addSink(new CollectSink)

    env.execute()

    CollectSink.values.size() should equal(1)
    CollectSink.values.get(0).count should equal(2)
  }
}
