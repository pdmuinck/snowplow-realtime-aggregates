package org.demuinck

final case class SnowplowPageViewAggregate(dimensions: List[String], timeWindowSeconds: Long) extends SnowplowAggregate {
  val filters = Some(Map("event" -> List("page_view")))
}
