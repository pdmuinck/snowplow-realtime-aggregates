package org.demuinck

trait SnowplowAggregateResult extends SnowplowAggregate {
  val key: String
  val count: Long
  val minTimestamp: Long
  val maxTimestamp: Long
}
