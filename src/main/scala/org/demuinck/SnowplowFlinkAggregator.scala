package org.demuinck

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import com.snowplowanalytics.snowplow.analytics.scalasdk.Event

import java.time.{Duration}

case class SnowplowFlinkAggregator() {

  def aggregate(dataStream: DataStream[String], dimensions: List[String], eventTypes: List[String], timeWindowSeconds: Int): DataStream[SnowplowCustomAggregate] = {
    dataStream
      .map(event => Event.parse(event).toOption)
      .filter(event => event match {
        case Some(x) => true
        case _ => false
      })
      .map(event => event.get)
      .assignTimestampsAndWatermarks(WatermarkStrategy
        .forBoundedOutOfOrderness[Event](Duration.ofSeconds(timeWindowSeconds))
        .withTimestampAssigner(new SnowplowEventTimestampAssigner))
      .map(event => SnowplowCustomAggregate(event, dimensions, eventTypes))
      .filter(agg => agg match {
        case Some(x) => true
        case _ => false
      })
      .map(agg => agg.get)
      .keyBy(agg => agg.key)
      .window(TumblingEventTimeWindows.of(Time.seconds(timeWindowSeconds)))
      .reduce((v1, v2) => new SnowplowCustomAggregate(
        v1.dimensions,
        v1.key,
        v1.count + v2.count,
        Math.min(v1.minTimestamp, v2.minTimestamp),
        Math.max(v1.maxTimestamp, v2.maxTimestamp),
        v1.eventTypes))
  }
}
