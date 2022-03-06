package org.demuinck

import com.typesafe.scalalogging.StrictLogging
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

import java.time.Duration


object SnowplowFlinkAggregator extends StrictLogging{

  def reduce(dataStream: DataStream[PageViewsAggregate]): DataStream[PageViewsAggregate] = {
    val strategy: WatermarkStrategy[PageViewsAggregate] = WatermarkStrategy
      .forBoundedOutOfOrderness[PageViewsAggregate](Duration.ofSeconds(5))
      .withTimestampAssigner(new SnowplowEventTimestampAssigner)

    dataStream
      .assignTimestampsAndWatermarks(strategy)
      .keyBy(new PageViewsAggregateKeySelector)
      .window(TumblingEventTimeWindows.of(Time.seconds(5)))
      .reduce((v1, v2) => new PageViewsAggregate(new VisitorKey(v1.key.appId, v1.key.referrerUrlPath, v1.key.deviceType), v1.pageViews + v2.pageViews, v1.eventTs))
  }


}
