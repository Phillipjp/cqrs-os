package cqrsos
trait Event{
  val eventNumber: Int
}
case class StockAddedEvent(eventNumber: Int, quantity: Int) extends Event
case class StockSoldEvent(eventNumber: Int, customerId: String, quantity: Int) extends Event
case class StockNotSoldEvent(eventNumber: Int, customerId: String, quantity: Int) extends Event
