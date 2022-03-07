package org.demuinck

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event

class SnowplowAggregate(val dimensions: Map[String, Option[String]], val count: Long, val eventTs: Long)

object SnowplowAggregate {

  def apply(event: String, dimensions: List[String]): SnowplowAggregate = {
    Event.parse(event).toOption match {
      case Some(x) => new SnowplowAggregate(Map("app_id" -> x.app_id, "dvce_type" -> x.dvce_type).filterKeys(dimensions.contains(_)), 1L, x.collector_tstamp.getEpochSecond)
      case _ => new SnowplowAggregate(Map(), 0L, 0L)
    }
  }
}
