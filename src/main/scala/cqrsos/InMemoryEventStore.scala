package cqrsos

import cqrsos.api.EventStore

import scala.collection.mutable.ListBuffer

class InMemoryEventStore() extends EventStore{

  private val eventLog = ListBuffer[Event]()

  override def storeEvent(event: Event): Unit = {
    eventLog.append(event)
  }

}
