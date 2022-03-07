package org.demuinck

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner


class SnowplowEventTimestampAssigner extends SerializableTimestampAssigner[SnowplowAggregate] {
  override def extractTimestamp(element: SnowplowAggregate, recordTimestamp: Long): Long = {
    element.eventTs
  }
}
