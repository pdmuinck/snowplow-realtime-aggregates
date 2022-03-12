package org.demuinck

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import org.demuinck.utils.AggregatorUtils

case class SnowplowStructuredEventAggregate(dimensions: List[String], key: String, count: Long, minTimestamp: Long, maxTimestamp: Long)
  extends SnowplowAggregate {
}

object SnowplowStructuredEventAggregate {
  val dimensions = List("se_category", "se_action")
  def apply(event: Event): SnowplowStructuredEventAggregate = {
    new SnowplowStructuredEventAggregate(
      dimensions,
      AggregatorUtils.getKey(event, dimensions),
      1L,
      event.collector_tstamp.getEpochSecond,
      event.collector_tstamp.getEpochSecond)
  }
}


