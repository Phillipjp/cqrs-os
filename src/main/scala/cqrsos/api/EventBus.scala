package cqrsos.api

import cqrsos.Event

trait EventBus {
  def subscribe(queryService: QueryService): Unit

  def sendEvent(event: Event): Unit

  def shutdown(): Unit
}
