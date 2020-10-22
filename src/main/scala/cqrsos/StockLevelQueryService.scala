package cqrsos

class StockLevelQueryService() extends QueryService{

  private var stockCount: Int = 0
  override def processEvent(event: Event): Unit = {
    event match {
      case e: StockAddedEvent => stockCount += e.quantity
      case e: StockSoldEvent => stockCount -= e.quantity
      case e: StockNotSoldEvent =>

    }

  }

  def getStockCount: Int = stockCount

}
