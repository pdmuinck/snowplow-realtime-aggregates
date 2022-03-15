package org.demuinck

class SnowplowCustomAggregateResult(
                                   val key: String,
                                   val dimensions: List[String],
                                   val count: Long,
                                   val minTstamp: Long,
                                   val maxTstamp: Long,
                                   val filters: List[String]
                                   )
