package org.demuinck

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import org.demuinck.utils.AggregatorUtils

case class SnowplowPageViewAggregate(dimensions: List[String], key: String, count: Long, minTimestamp: Long, maxTimestamp: Long, eventTypes: List[String])
  extends SnowplowAggregate {
}

object SnowplowPageViewAggregate {
  val eventTypes = List("page_view")
  def apply(event: Event, dimensions: List[String]): Option[SnowplowPageViewAggregate] = {
    val isPageView = event.event match {
      case Some(x) => x.equals("page_view")
      case _ => false
    }

    if (isPageView) {
      Option(new SnowplowPageViewAggregate(
        dimensions,
        AggregatorUtils.getKey(event, dimensions),
        1L,
        event.collector_tstamp.getEpochSecond,
        event.collector_tstamp.getEpochSecond,
        List("page_view")))
    } else {
      None
    }
  }
}
