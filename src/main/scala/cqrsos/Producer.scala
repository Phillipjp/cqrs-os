package cqrsos

import java.util.concurrent.LinkedBlockingQueue

class Producer(queue: LinkedBlockingQueue[Event], var event: Event) extends Runnable{


  private def addEvent(event: Event): Unit = {
    queue.put(event)
  }

  override def run(): Unit = {
    addEvent(event)
  }
}
