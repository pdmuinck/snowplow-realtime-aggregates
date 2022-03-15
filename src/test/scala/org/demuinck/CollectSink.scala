package org.demuinck

import org.apache.flink.streaming.api.functions.sink.SinkFunction

import java.util

class CollectSink extends SinkFunction[SnowplowCustomAggregateResult] {

  override def invoke(value: SnowplowCustomAggregateResult): Unit = {
    synchronized {
      CollectSink.values.add(value)
    }
  }
}

object CollectSink {
  val values: util.List[SnowplowCustomAggregateResult] = new util.ArrayList()
}