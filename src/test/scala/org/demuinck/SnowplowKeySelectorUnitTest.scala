package org.demuinck

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.time.Instant

class SnowplowKeySelectorUnitTest extends AnyFunSuite with Matchers {

  test("testGetKey should return comma delimited string for all dimensions") {
    val snowplowKeySelector = new SnowplowKeySelector
    val snowplowAggregate = new SnowplowAggregate(Map("app_id" -> Option("org.pdemuinck.web"), "device" -> Option("smartphone")), 16L, Instant.now().getEpochSecond)
    val result = snowplowKeySelector.getKey(snowplowAggregate)
    result should equal("org.pdemuinck.web;smartphone")
  }

}
