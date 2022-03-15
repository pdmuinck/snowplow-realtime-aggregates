package org.demuinck

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, OutputTag}
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import org.demuinck.utils.AggregatorUtils

import java.time.Duration

class GroupingSet(val groups: List[Group])

class Group(val fields: List[String], val timeWindowSeconds: Long)

case class SnowplowFlinkAggregator(dataStream: DataStream[Event], aggregates: List[SnowplowCustomAggregate]) {

  def aggregate(): DataStream[SnowplowCustomAggregateResult] = {
    aggregates.map(agg => {
      val outputTag = OutputTag[SnowplowCustomAggregateResult]("side-output")
      dataStream
        .assignTimestampsAndWatermarks(WatermarkStrategy
          .forBoundedOutOfOrderness[Event](Duration.ofSeconds(agg.timeWindowSeconds))
          .withTimestampAssigner(new SnowplowEventTimestampAssigner))
        .keyBy(ev => AggregatorUtils.getKey(ev, agg.dimensions))
        .window(TumblingEventTimeWindows.of(Time.seconds(agg.timeWindowSeconds)))
        .process(new SnowplowEventProcessWindowFunction(outputTag, agg.dimensions, List()))
        .getSideOutput(outputTag)
    }).reduceLeft(_.union(_))
  }
}
