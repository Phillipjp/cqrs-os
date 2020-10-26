package cqrsos

import java.util.concurrent.{ArrayBlockingQueue, BlockingQueue, LinkedBlockingQueue, TimeUnit}

class Consumer(queue: LinkedBlockingQueue[Event], queryService: QueryService) extends Runnable{

  private def getEvent: Event = {
    queue.take()
  }

  override def run(): Unit = {
    val event = getEvent
    queryService.processEvent(event)
  }

}