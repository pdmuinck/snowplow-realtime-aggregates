package org.demuinck

import com.typesafe.scalalogging.StrictLogging
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

import java.time.Duration


object SnowplowFlinkAggregator extends StrictLogging{

  def reduce(dataStream: DataStream[String]): DataStream[SnowplowAggregate] = {
    val strategy: WatermarkStrategy[SnowplowAggregate] = WatermarkStrategy
      .forBoundedOutOfOrderness[SnowplowAggregate](Duration.ofSeconds(5))
      .withTimestampAssigner(new SnowplowEventTimestampAssigner)

    val snowplowKeySelector = new SnowplowKeySelector

    dataStream
      .map(event => SnowplowAggregate(event, List("app_id", "dvce_type")))
      .assignTimestampsAndWatermarks(strategy)
      .keyBy(snowplowKeySelector)
      .window(TumblingEventTimeWindows.of(Time.seconds(5)))
      .reduce((v1, v2) => new SnowplowAggregate(v1.dimensions, v1.count + v2.count, v1.eventTs))
  }



}
