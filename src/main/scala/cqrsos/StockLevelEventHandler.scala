package cqrsos

import cqrsos.api.EventHandler

class StockLevelEventHandler() extends EventHandler{

  private var stockCount: Int = 0

  override protected var lastProcessedEvent: Int = 0

  override def handleEvent(event: Event): Unit = {
    event match {
      case e: StockAddedEvent => stockCount += e.quantity
      case e: StockSoldEvent => stockCount -= e.quantity
      case e: StockNotSoldEvent =>

    }

    lastProcessedEvent = event.eventNumber

  }

  def getStockCount: Int = stockCount

  override def getLastProcessedEvent: Int = lastProcessedEvent
}
