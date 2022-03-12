package org.demuinck.utils

import org.demuinck.SnowplowEvent
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class AggregatorUtilsTest extends AnyFunSuite with Matchers {

  test("get key should return string") {
    val event = SnowplowEvent.minimalEvent().copy(
      app_id = Option("org.pdemuinck.web"),
      dvce_type = Option("smartphone")
    )

    val result = AggregatorUtils.getKey(event, List("app_id", "dvce_type"))
    result shouldBe "org.pdemuinck.web;smartphone"
  }

  test("get key should return empty string when field not found") {
    val event = SnowplowEvent.minimalEvent()

    val result = AggregatorUtils.getKey(event, List("app_id", "dvce_type"))
    result shouldBe ";"
  }

}
