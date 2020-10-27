package cqrsos

import cqrsos.api.{EventBus, EventHandler}

import scala.collection.mutable.ListBuffer

class SynchronousEventBus() extends EventBus{

  private val eventHandlers: ListBuffer[EventHandler] = new ListBuffer[EventHandler]()

  override def sendEvent(event: Event): Unit = {
    eventHandlers.foreach(eventHand => eventHand.handleEvent(event))
  }

  override def subscribe(eventHandler: EventHandler): Unit = {
    eventHandlers.append(eventHandler)
  }

  override def shutdown(): Unit = {}

}
