package org.demuinck

import org.apache.flink.streaming.api.functions.sink.SinkFunction

import java.util

class CollectSink extends SinkFunction[PageViewsAggregate] {

  override def invoke(value: PageViewsAggregate): Unit = {
    synchronized {
      CollectSink.values.add(value)
    }
  }
}

object CollectSink {
  val values: util.List[PageViewsAggregate] = new util.ArrayList()
}