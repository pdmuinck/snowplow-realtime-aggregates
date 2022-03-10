package org.demuinck

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import java.time.Duration

case class SnowplowFlinkAggregator() {

  def aggregate(dataStream: DataStream[String], dimensions: List[String], timeWindowSeconds: Int): DataStream[SnowplowCustomAggregate] = {
    val strategy: WatermarkStrategy[Event] = WatermarkStrategy
      .forBoundedOutOfOrderness[Event](Duration.ofSeconds(1))
      .withTimestampAssigner(new SnowplowEventTimestampAssigner)

    dataStream
      .map(ev => parseSnowplowEvent(ev))
      .filter(event => event match {
        case Some(x) => true
        case _ => false
      })
      .map(event => event.get)
      .assignTimestampsAndWatermarks(strategy)
      .map(event => SnowplowCustomAggregate(event, dimensions))
      .keyBy(agg => agg.key)
      .window(TumblingEventTimeWindows.of(Time.seconds(1)))
      .reduce((v1, v2) => new SnowplowCustomAggregate(
        v1.dimensions,
        v1.key,
        v1.count + v2.count,
        Math.min(v1.minTimestamp, v2.minTimestamp),
        Math.max(v1.maxTimeStamp, v2.maxTimeStamp)))
  }

  def parseSnowplowEvent(event: String): Option[Event] = {
    Event.parse(event).toOption match {
      case Some(x) => Option(x)
      case _ => None
    }
  }


}
