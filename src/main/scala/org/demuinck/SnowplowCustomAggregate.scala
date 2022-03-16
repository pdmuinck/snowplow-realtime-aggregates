package org.demuinck

final case class SnowplowCustomAggregate(
                                          dimensions: List[String],
                                          timeWindowSeconds: Long,
                                          filters: Option[Map[String, List[String]]]) extends SnowplowAggregate
