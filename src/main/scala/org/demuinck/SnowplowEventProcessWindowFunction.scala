package org.demuinck

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import org.apache.flink.streaming.api.scala.OutputTag
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.demuinck.utils.AggregatorUtils

import scala.collection.mutable.ListBuffer

class SnowplowEventProcessWindowFunction(
                                          val outputTag: OutputTag[SnowplowCustomAggregateResult],
                                          val aggregate: SnowplowAggregate)
  extends ProcessWindowFunction[Event, SnowplowCustomAggregateResult, String, TimeWindow]{

  override def process(
                        key: String,
                        context: Context,
                        elements: Iterable[Event],
                        out: Collector[SnowplowCustomAggregateResult]): Unit = {
    var count = 0
    val collectorTimestamps = ListBuffer[Long]()
    for (in <- elements) {
      count += 1
      collectorTimestamps.append(in.collector_tstamp.getEpochSecond)
    }
    val refElement = elements.head
    val key = AggregatorUtils.getKey(refElement, aggregate.dimensions)
    val result = new SnowplowCustomAggregateResult(
      aggregate.dimensions,
      aggregate.timeWindowSeconds,
      aggregate.filters,
      key,
      count,
      collectorTimestamps.min,
      collectorTimestamps.max)
    context.output[SnowplowCustomAggregateResult](outputTag, result)
  }
}
