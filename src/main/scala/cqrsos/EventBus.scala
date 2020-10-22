package cqrsos

trait EventBus{
  def sendEvent(event: Event): Unit
}
