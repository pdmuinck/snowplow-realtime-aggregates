package org.demuinck

class SnowplowCustomAggregateResult(
                                     val dimensions: List[String],
                                     val timeWindowSeconds: Long,
                                     val filters: Option[Map[String, List[String]]],
                                     val key: String,
                                     val count: Long,
                                     val minTimestamp: Long,
                                     val maxTimestamp: Long,
                                   ) extends SnowplowAggregateResult
