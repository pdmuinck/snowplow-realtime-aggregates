package org.demuinck

trait SnowplowAggregate {
  val dimensions: List[String]
  val key: String
  val count: Long
  val minTimestamp: Long
  val maxTimestamp: Long
}
