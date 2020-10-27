package cqrsos

import java.util.concurrent.{ExecutorService, Executors, LinkedBlockingQueue, TimeUnit}

import cqrsos.api.{EventBus, EventHandler}

import scala.collection.mutable.ListBuffer


class AsynchronousEventBus(nThreads: Int, timeout: Int) extends EventBus {

  private val eventHandlers: ListBuffer[EventHandler] = new ListBuffer[EventHandler]()

  private val executorService: ExecutorService = Executors.newFixedThreadPool(nThreads)

  private val queue: LinkedBlockingQueue[Event] = new LinkedBlockingQueue()

  override def sendEvent(event: Event): Unit = {

    eventHandlers.foreach(_ => queue.put(event))
    eventHandlers.foreach(eventHandler => executorService.execute(new Runnable {
      override def run(): Unit = {
        eventHandler.handleEvent(event)
      }
    }))

  }

  override def subscribe(eventHandler: EventHandler): Unit = {
    eventHandlers.append(eventHandler)
  }

  override def shutdown(): Unit = {
    executorService.shutdown()
    if (!executorService.awaitTermination(timeout, TimeUnit.SECONDS)) {
      val droppedTasks = executorService.shutdownNow()
      println(s"Executor shutdown before completion. ${droppedTasks.size()} tasks incomplete.")
    }
  }
}
