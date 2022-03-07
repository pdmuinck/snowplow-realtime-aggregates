package org.demuinck

import com.amazonaws.services.kinesisanalytics.runtime.KinesisAnalyticsRuntime
import com.typesafe.scalalogging.StrictLogging
import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer

import java.time.Duration
import java.util.Properties


object SnowplowFlinkAggregator extends StrictLogging{

  def main(args: Array[String]): Unit = {
    val properties = KinesisAnalyticsRuntime.getApplicationProperties().get("APPLICATION_CONFIG")
    val groupingSets: List[List[String]] = properties.getProperty("kinesis.agg.groups")
    val sourceStream = properties.getProperty("kinesis.source.stream")
    val kafkaBootstrapServers = properties.getProperty("kafka.destination.bootstrap.servers"),
    val kafkaTopicAccepted = properties.getProperty("kafka.destination.topic.accepted"),
    val kafkaTopicRejected = properties.getProperty("kafka.destination.topic.rejected")

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val consumer = new FlinkKinesisConsumer[String](
      sourceStream,
      new SimpleStringSchema,
      new Properties()
    )

    val producer = new FlinkKafka

    val dataStream: DataStream[String] = env.addSource(consumer)

    groupingSets.map(group => reduce(dataStream, group).addSink)
  }

  def reduce(dataStream: DataStream[String], dimensions: List[String]): DataStream[SnowplowAggregate] = {
    val strategy: WatermarkStrategy[SnowplowAggregate] = WatermarkStrategy
      .forBoundedOutOfOrderness[SnowplowAggregate](Duration.ofSeconds(5))
      .withTimestampAssigner(new SnowplowEventTimestampAssigner)

    dataStream
      .map(event => SnowplowAggregate(event, dimensions))
      .assignTimestampsAndWatermarks(strategy)
      .keyBy(aggregate => aggregate.dimensions.values.map(x => x.getOrElse("")).mkString(";"))
      .window(TumblingEventTimeWindows.of(Time.seconds(5)))
      .reduce((v1, v2) => new SnowplowAggregate(v1.dimensions, v1.count + v2.count, v1.eventTs))
  }



}
