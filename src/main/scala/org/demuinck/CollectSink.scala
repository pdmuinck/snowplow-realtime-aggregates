package org.demuinck

import org.apache.flink.streaming.api.functions.sink.SinkFunction

import java.util

class CollectSink extends SinkFunction[SnowplowCustomAggregate] {

  override def invoke(value: SnowplowCustomAggregate): Unit = {
    synchronized {
      CollectSink.values.add(value)
    }
  }
}

object CollectSink {
  val values: util.List[SnowplowCustomAggregate] = new util.ArrayList()
}