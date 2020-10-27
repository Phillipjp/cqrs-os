package cqrsos.api

import cqrsos.Event

trait EventHandler {
  protected var lastProcessedEvent: Int

  def handleEvent(event: Event): Unit

  def getLastProcessedEvent: Int
}
