package org.demuinck

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import org.demuinck.utils.AggregatorUtils

final case class SnowplowCustomAggregate(
                                          dimensions: List[String],
                                          key: String,
                                          count: Long,
                                          minTimestamp: Long,
                                          maxTimestamp: Long,
                                          eventTypes: List[String])
  extends SnowplowAggregate

object SnowplowCustomAggregate {

  def apply(event: Event, dimensions: List[String], eventTypes: List[String]): Option[SnowplowCustomAggregate] = {
    try {
      if(eventTypes.isEmpty || eventTypes.contains(event.event.get)) {
        Option(new SnowplowCustomAggregate(
          dimensions,
          AggregatorUtils.getKey(event, dimensions),
          1L,
          event.collector_tstamp.getEpochSecond,
          event.collector_tstamp.getEpochSecond,
          eventTypes))
      } else {
        None
      }
    } catch {
      case _: Throwable => None
    }
  }
}
