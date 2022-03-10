package org.demuinck

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner


class SnowplowEventTimestampAssigner extends SerializableTimestampAssigner[Event] {
  override def extractTimestamp(element: Event, recordTimestamp: Long): Long = {
    element.collector_tstamp.getEpochSecond
  }
}
