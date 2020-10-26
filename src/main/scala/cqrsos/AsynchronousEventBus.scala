package cqrsos

import java.util.concurrent.{ExecutorService, LinkedBlockingQueue}


class AsynchronousEventBus(queue: LinkedBlockingQueue[Event], executorService: ExecutorService, queryServices: Seq[QueryService]) extends EventBus {

  override def sendEvent(event: Event): Unit = {

    queryServices.foreach( _ => executorService.execute( new Producer(queue, event)))
    queryServices.foreach( qs => executorService.execute(new Consumer(queue, qs)))

  }
}
