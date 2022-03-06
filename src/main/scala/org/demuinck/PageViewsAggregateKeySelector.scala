package org.demuinck

import org.apache.flink.api.java.functions.KeySelector

class PageViewsAggregateKeySelector extends KeySelector[PageViewsAggregate, String] {
  override def getKey(value: PageViewsAggregate): String = {
    value.key.appId + "," + value.key.referrerUrlPath + "," + value.key.deviceType
  }
}
