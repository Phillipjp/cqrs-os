package cqrsos

class SynchronousEventBus(queryServices: Seq[QueryService]) extends EventBus{

  override def sendEvent(event: Event): Unit = {
    queryServices.foreach(q => q.processEvent(event))
  }

}
