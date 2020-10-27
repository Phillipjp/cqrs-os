package cqrsos

import java.util.concurrent.{ExecutorService, Executors, LinkedBlockingQueue, TimeUnit}

import cqrsos.api.{EventBus, QueryService}

import scala.collection.mutable.ListBuffer


class AsynchronousEventBus(nThreads: Int, timeout: Int) extends EventBus {

  private val queryServices: ListBuffer[QueryService] = new ListBuffer[QueryService]()

  private val executorService: ExecutorService = Executors.newFixedThreadPool(nThreads)

  private val queue: LinkedBlockingQueue[Event] = new LinkedBlockingQueue()

  override def sendEvent(event: Event): Unit = {

    queryServices.foreach(_ => queue.put(event))
    queryServices.foreach(qs => executorService.execute(new Runnable {
      override def run(): Unit = {
        qs.processEvent(event)
      }
    }))

  }

  override def subscribe(queryService: QueryService): Unit = {
    queryServices.append(queryService)
  }

  override def shutdown(): Unit = {
    executorService.shutdown()
    if (!executorService.awaitTermination(timeout, TimeUnit.SECONDS)) {
      val droppedTasks = executorService.shutdownNow()
      println(s"Executor shutdown before completion. ${droppedTasks.size()} tasks incomplete.")
    }
  }
}
