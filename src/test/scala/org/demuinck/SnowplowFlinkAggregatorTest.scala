package org.demuinck

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.test.util.MiniClusterWithClientResource
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.time.Instant
import java.util.UUID
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`


class SnowplowFlinkAggregatorTest extends AnyFunSuite with BeforeAndAfter with Matchers{

  val cluster = new MiniClusterWithClientResource(
    new MiniClusterResourceConfiguration.Builder()
      .setNumberTaskManagers(2)
      .setNumberSlotsPerTaskManager(1)
      .build()
  )

  before {
    cluster.before()
  }

  after {
    cluster.after()
  }

  test("integration test windowing function") {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(2)

    CollectSink.values.clear()

    val now = Instant.now()

    val validUuid = "6ed5cc14-6bad-4964-889b-efa9e51906d1"
    val events = (1 to 10000).toList.map(x => Event
      .minimal(UUID.fromString(validUuid), Instant.now(), "Collector", "ETL")
      .copy(
        app_id = Option("org.pdemuinck.web"),
        dvce_type = Option("smartphone"),
        collector_tstamp = now
      ))

    val testStream = env.fromCollection(events)

    val aggregator = SnowplowFlinkAggregator(
      testStream,
      List(SnowplowCustomAggregate(List("app_id"), 60, Some(Map("app_id" -> List("org.pdemuinck.web")))))
    )

    aggregator.aggregate().addSink(new CollectSink)

    env.execute()

    CollectSink.values should have size 1
    CollectSink.values.map(x => x.count) should contain (10000L)
    //CollectSink.values.map(x => x.count) should contain (2L)

  }
}
