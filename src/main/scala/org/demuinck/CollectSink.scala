package org.demuinck

import org.apache.flink.streaming.api.functions.sink.SinkFunction

import java.util

class CollectSink extends SinkFunction[SnowplowAggregate] {

  override def invoke(value: SnowplowAggregate): Unit = {
    synchronized {
      CollectSink.values.add(value)
    }
  }
}

object CollectSink {
  val values: util.List[SnowplowAggregate] = new util.ArrayList()
}