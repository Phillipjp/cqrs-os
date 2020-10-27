package cqrsos.api

import cqrsos.Event

trait EventHandler {
  def handleEvent(event: Event): Unit

  def getLastProcessedEvent: Int
}
