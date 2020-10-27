package cqrsos.api

import cqrsos.Event

trait EventStore {
  def storeEvent(event: Event): Unit

  def getEventLog: Iterable[Event]
}
