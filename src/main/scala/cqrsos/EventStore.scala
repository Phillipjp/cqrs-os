package cqrsos

trait EventStore {
  def storeEvent(event: Event): Unit
}
