package org.demuinck

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, OutputTag}
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import org.demuinck.utils.AggregatorUtils

import java.time.Duration

case class SnowplowFlinkAggregator(dataStream: DataStream[Event], aggregates: List[SnowplowAggregate]) {

  def aggregate(): DataStream[SnowplowCustomAggregateResult] = {
    aggregates.map(agg => {
      val outputTag = OutputTag[SnowplowCustomAggregateResult]("side-output")
      dataStream
        .filter(event => AggregatorUtils.isFilteredEvent(event, agg.filters))
        .assignTimestampsAndWatermarks(WatermarkStrategy
          .forBoundedOutOfOrderness[Event](Duration.ofSeconds(agg.timeWindowSeconds))
          .withTimestampAssigner(new SnowplowEventTimestampAssigner))
        .keyBy(ev => AggregatorUtils.getKey(ev, agg.dimensions))
        .window(TumblingEventTimeWindows.of(Time.seconds(agg.timeWindowSeconds)))
        .process(new SnowplowEventProcessWindowFunction(outputTag, agg))
        .getSideOutput(outputTag)
    }).reduceLeft(_.union(_))
  }
}
