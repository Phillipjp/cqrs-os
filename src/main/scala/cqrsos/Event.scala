package cqrsos

trait Event
case class StockAddedEvent(quantity: Int) extends Event
case class StockSoldEvent(customerId: String, quantity: Int) extends Event
case class StockNotSoldEvent(customerId: String, quantity: Int) extends Event
