package org.demuinck

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event
import scala.reflect.runtime.{universe => ru}

class SnowplowAggregate(val dimensions: Map[String, Option[String]], val count: Long, val eventTs: Long)

object SnowplowAggregate {

  def apply(event: String, dimensions: List[String]): SnowplowAggregate = {
    Event.parse(event).toOption match {
      case Some(x) => new SnowplowAggregate(
        dimensions.map(dim => dim -> getDimensionValue(x, dim)).toMap,
        1L,
        x.collector_tstamp.getEpochSecond)
      case _ => new SnowplowAggregate(Map(), 0L, 0L)
    }
  }

  def getDimensionValue(event: Event, dimension: String): Option[String] = {
    val runTimeMirror = ru.runtimeMirror(event.getClass.getClassLoader)
    val dimensionTerm = ru.typeOf[Event].decl(ru.TermName(dimension)).asTerm
    val im = runTimeMirror.reflect(event)
    val dimensionFieldMirror = im.reflectField(dimensionTerm)
    Option(dimensionFieldMirror.get.toString)
  }
}
