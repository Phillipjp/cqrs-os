package cqrsos.api

import cqrsos.Event

trait EventBus {
  def subscribe(eventHandler: EventHandler): Unit

  def sendEvent(event: Event): Unit

  def shutdown(): Unit
}
