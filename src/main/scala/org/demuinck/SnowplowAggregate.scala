package org.demuinck

trait SnowplowAggregate {
  val dimensions: List[String]
  val timeWindowSeconds: Long
  val filters: Option[Map[String, List[String]]]
}
