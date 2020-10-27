package cqrsos

import java.util.concurrent.{ExecutorService, Executors, LinkedBlockingQueue, TimeUnit}

import cqrsos.api.{EventBus, QueryService}


class AsynchronousEventBus(val queryServices: Seq[QueryService]) extends EventBus {

  private val queue: LinkedBlockingQueue[Event] = new LinkedBlockingQueue()

  override def sendEvent(event: Event): Unit = {

    val executorService: ExecutorService = Executors.newFixedThreadPool(queryServices.length)
    queryServices.foreach(_ => executorService.execute(new Producer(queue, event)))
    queryServices.foreach(qs => executorService.execute(new Consumer(queue, qs)))
    executorService.shutdown()
    if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
      val droppedTasks = executorService.shutdownNow()
      println(s"Executor shutdown before completion. ${droppedTasks.size()} tasks incomplete.")
    }

  }

}
