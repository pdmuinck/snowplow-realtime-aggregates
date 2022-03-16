package org.demuinck

final case class SnowplowStructEventAggregate(dimensions: List[String], timeWindowSeconds: Long) extends SnowplowAggregate {
  val filters = Some(Map("event" -> List("struct")))
}
