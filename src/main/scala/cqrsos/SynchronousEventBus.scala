package cqrsos

import cqrsos.api.{EventBus, QueryService}

import scala.collection.mutable.ListBuffer

class SynchronousEventBus() extends EventBus{

  private val queryServices: ListBuffer[QueryService] = new ListBuffer[QueryService]()

  override def sendEvent(event: Event): Unit = {
    queryServices.foreach(q => q.processEvent(event))
  }

  override def subscribe(queryService: QueryService): Unit = {
    queryServices.append(queryService)
  }

  override def shutdown(): Unit = {}

}
