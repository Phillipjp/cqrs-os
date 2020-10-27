package cqrsos.api

import cqrsos.Event

trait EventBus {
  val queryServices: Seq[QueryService]

  def sendEvent(event: Event): Unit
}
