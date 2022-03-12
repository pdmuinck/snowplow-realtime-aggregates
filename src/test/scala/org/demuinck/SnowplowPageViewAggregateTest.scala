package org.demuinck

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class SnowplowPageViewAggregateTest extends AnyFunSuite with Matchers {

  test("should create page view aggregate when event is a page view") {
    val event = SnowplowEvent.minimalEvent().copy(
      event = Option("page_view"),
      app_id = Option("org.pdemuinck.web")
    )
    val result = SnowplowPageViewAggregate(event, List("app_id"))
    result shouldBe defined
    result.get.dimensions shouldBe List("app_id")
    result.get.count shouldBe 1L
    result.get.key shouldBe "org.pdemuinck.web"
  }

  test("should not create page view aggregate when event is different from page view") {
    val event = SnowplowEvent.minimalEvent().copy(
      event = Option("screen_view"),
      app_id = Option("org.pdemuinck.web")
    )
    val result = SnowplowPageViewAggregate(event, List("app_id"))
    result should not be defined
  }

}
