package org.demuinck.utils

import com.snowplowanalytics.snowplow.analytics.scalasdk.Event

import scala.reflect.runtime.{universe => ru}

object AggregatorUtils {
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
