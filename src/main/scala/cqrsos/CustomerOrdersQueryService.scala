package cqrsos

import cqrsos.api.QueryService

import scala.collection.mutable

class CustomerOrdersQueryService extends QueryService {

  private val customerOrders: mutable.Map[String, Seq[Order]] = mutable.Map[String, Seq[Order]]()

  override def processEvent(event: Event): Unit = {
    event match {
      case _: StockAddedEvent =>
      case e: StockSoldEvent => updateCustomerOrders(e.customerId, e.quantity, sold = true)
      case e: StockNotSoldEvent => updateCustomerOrders(e.customerId, e.quantity, sold = false)
    }
  }

  private def updateCustomerOrders(customerId: String, quantity: Int, sold: Boolean): Unit =
    customerOrders.get(customerId) match {
      case Some(orders) => customerOrders(customerId) = orders :+ Order(customerId, quantity, sold)
      case None => customerOrders(customerId) = Seq(Order(customerId, quantity, sold))
    }


  def getCustomerOrders(customerId: String): Seq[Order] = customerOrders.getOrElse(customerId, Seq())
}
