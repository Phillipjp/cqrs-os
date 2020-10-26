package cqrsos
import java.time.LocalDateTime
trait Event{
  val date: LocalDateTime
}
case class StockAddedEvent(date: LocalDateTime, quantity: Int) extends Event
case class StockSoldEvent(date: LocalDateTime, customerId: String, quantity: Int) extends Event
case class StockNotSoldEvent(date: LocalDateTime, customerId: String, quantity: Int) extends Event
