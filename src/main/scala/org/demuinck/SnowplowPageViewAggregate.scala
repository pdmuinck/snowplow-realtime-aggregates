package org.demuinck

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import org.demuinck.utils.AggregatorUtils

case class SnowplowPageViewAggregate(dimensions: List[String], key: String, count: Long, minTimestamp: Long, maxTimestamp: Long)
  extends SnowplowAggregate {
}

object SnowplowPageViewAggregate {
  val dimensions = List("page_title", "page_urlpath")
  def apply(event: Event): Option[SnowplowPageViewAggregate] = {
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
        event.collector_tstamp.getEpochSecond))
    } else {
      None
    }
  }
}
