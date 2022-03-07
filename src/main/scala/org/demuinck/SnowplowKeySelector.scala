package org.demuinck

import org.apache.flink.api.java.functions.KeySelector

class SnowplowKeySelector extends KeySelector[SnowplowAggregate, String] {
  override def getKey(aggregate: SnowplowAggregate): String = {
    aggregate.dimensions.values.map(x => x.getOrElse("")).mkString(";")
  }
}
