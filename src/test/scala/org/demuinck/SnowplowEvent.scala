package org.demuinck

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event

import java.time.Instant
import java.util.UUID

object SnowplowEvent {

  def minimalEvent(): Event = {
    val validUuid = "6ed5cc14-6bad-4964-889b-efa9e51906d1"
    Event.minimal(UUID.fromString(validUuid), Instant.now(), "Collector", "ETL")
  }

}
