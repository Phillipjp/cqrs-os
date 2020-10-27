package cqrsos

import cqrsos.api.{EventBus, QueryService}

class SynchronousEventBus(val queryServices: Seq[QueryService]) extends EventBus{

  override def sendEvent(event: Event): Unit = {
    queryServices.foreach(q => q.processEvent(event))
  }

}
