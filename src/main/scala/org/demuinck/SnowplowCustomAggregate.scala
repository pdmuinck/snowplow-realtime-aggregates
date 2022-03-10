package org.demuinck

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import scala.reflect.runtime.{universe => ru}

final case class SnowplowCustomAggregate(dimensions: List[String], key:String, count: Long, minTimestamp: Long, maxTimeStamp: Long)

object SnowplowCustomAggregate {
  def apply(event: Event, dimensions: List[String]): SnowplowCustomAggregate = {
    new SnowplowCustomAggregate(dimensions, getKey(event, dimensions), 1L, event.collector_tstamp.getEpochSecond, event.collector_tstamp.getEpochSecond)
  }

  def getKey(event: Event, dimensions: List[String]): String = {
    dimensions.map(dim => dim -> getDimensionValue(event, dim)).mkString(";")
  }

  def getDimensionValue(event: Event, dimension: String): Option[String] = {
    val runTimeMirror = ru.runtimeMirror(event.getClass.getClassLoader)
    val dimensionTerm = ru.typeOf[Event].decl(ru.TermName(dimension)).asTerm
    val im = runTimeMirror.reflect(event)
    val dimensionFieldMirror = im.reflectField(dimensionTerm)
    Option(dimensionFieldMirror.get.toString)
  }
}
