package org.demuinck

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration
import org.apache.flink.streaming.api.scala.{ StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.test.util.MiniClusterWithClientResource
import org.scalatest.{BeforeAndAfter}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.time.{Duration, Instant}


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

    val now = Instant.now().getEpochSecond

    val testStream = env.fromElements(
      new PageViewsAggregate(new VisitorKey("hello", "ref", "device"), 2, now),
      new PageViewsAggregate(new VisitorKey("hello", "ref", "device"), 3, now)
    )

    SnowplowFlinkAggregator.reduce(testStream).addSink(new CollectSink)

    env.execute()

    CollectSink.values.size() should equal(1)
    CollectSink.values.get(0).pageViews should equal(5)
    CollectSink.values.get(0).key.appId should equal("hello")
  }
}
