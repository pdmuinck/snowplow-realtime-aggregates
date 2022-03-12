package org.demuinck

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.time.Instant
import java.util.UUID

class SnowplowCustomAggregateTest extends AnyFunSuite with Matchers {

  test("should construct custom aggregate when no eventTypes are provided.") {
    val event = SnowplowEvent.minimalEvent()
    val aggregate = SnowplowCustomAggregate(event, List("app_id"), List())
    aggregate shouldBe defined
    aggregate.get.eventTypes.isEmpty shouldBe true
    aggregate.get.count shouldBe 1L
    aggregate.get.dimensions shouldBe List("app_id")
  }

  test("should not construct custom aggregate when wrong eventTypes are provided.") {
    val event = SnowplowEvent.minimalEvent()
      .copy(
        app_id = Option("org.pdemuinck.web"),
        dvce_type = Option("smartphone"),
        event = Option("page_view")
      )
    val aggregate = SnowplowCustomAggregate(event, List("app_id"), List("screen_view"))
    aggregate should not be defined
  }

  test("should not construct custom aggregate when eventTypes are provided, but event has no event type.") {
    val event = SnowplowEvent.minimalEvent()
    val aggregate = SnowplowCustomAggregate(event, List("app_id"), List("screen_view"))
    aggregate should not be defined
  }



}
