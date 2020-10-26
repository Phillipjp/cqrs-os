package cqrsos

import java.util.concurrent.{ExecutorService, LinkedBlockingQueue}


class AsynchronousEventBus(queue: LinkedBlockingQueue[Event], executorService: ExecutorService, queryServices: Seq[QueryService]) extends EventBus {

  override def sendEvent(event: Event): Unit = {

    queryServices.foreach(_ => executorService.execute( new Producer(queue, event)))
    queryServices.map(qs => new Consumer(queue, qs)).foreach( consumer => executorService.execute(consumer))

  }
}
