package org.demuinck.utils

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event

import scala.reflect.runtime.{universe => ru}

object AggregatorUtils {
  def getKey(event: Event, dimensions: List[String]): String = {
    dimensions.map(dim => getDimensionValue(event, dim)).mkString(";")
  }

  def getDimensionValue(event: Event, dimension: String): String = {
    val runTimeMirror = ru.runtimeMirror(event.getClass.getClassLoader)
    val dimensionTerm = ru.typeOf[Event].decl(ru.TermName(dimension)).asTerm
    val im = runTimeMirror.reflect(event)
    val dimensionFieldMirror = im.reflectField(dimensionTerm)
    try {
      dimensionFieldMirror.get.asInstanceOf[Some[String]].value
    } catch {
      case _: Throwable => ""
    }
  }

  def isFilteredEvent(event: Event, filters: Option[Map[String, List[String]]]): Boolean = {
    filters match  {
      case Some(x)  => x.keys.forall(key =>  x(key).contains(getDimensionValue(event, key)))
      case _ => true
    }
  }
}
