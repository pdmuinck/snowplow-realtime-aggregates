package org.demuinck

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner


class SnowplowEventTimestampAssigner extends SerializableTimestampAssigner[PageViewsAggregate] {
  override def extractTimestamp(element: PageViewsAggregate, recordTimestamp: Long): Long = {
    element.eventTs
  }
}
