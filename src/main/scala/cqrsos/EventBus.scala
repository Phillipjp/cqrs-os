package cqrsos

trait EventBus{
  val queryServices: Seq[QueryService]

  def sendEvent(event: Event): Unit
}
