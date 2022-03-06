package org.demuinck

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner

class SnowplowTimestampAssigner extends SerializableTimestampAssigner[VisitorAggregate] {
  override def extractTimestamp(element: VisitorAggregate, recordTimestamp: Long): Long = {
    element.eventTs
  }
}
