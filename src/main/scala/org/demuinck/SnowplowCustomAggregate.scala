package org.demuinck

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import org.demuinck.utils.AggregatorUtils

final case class SnowplowCustomAggregate(dimensions: List[String], key: String, count: Long, minTimestamp: Long, maxTimestamp: Long) extends SnowplowAggregate

object SnowplowCustomAggregate {
  def apply(event: Event, dimensions: List[String]): SnowplowCustomAggregate = {
    new SnowplowCustomAggregate(
      dimensions,
      AggregatorUtils.getKey(event, dimensions),
      1L,
      event.collector_tstamp.getEpochSecond,
      event.collector_tstamp.getEpochSecond)
  }
}
