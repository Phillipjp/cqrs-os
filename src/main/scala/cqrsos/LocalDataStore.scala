package cqrsos

case class LocalDataStore(stock: Seq[StockItem]) extends DataStore {



  private def handleEvent(event: Event, handler: StockItem => StockItem, quantity: Int): DataStore = {
    val stockItem = stock.find(_.product == event.product)

    val handledStock = stockItem match {
      case Some(item) =>
        val handledItem = handler(item)
        stock.map { i =>
          if (i == item) {
            handledItem
          }
          else {
            i
          }
        }
      case None => stock :+ StockItem(event.product, quantity)
    }
    this.copy(stock = handledStock)
  }



  def handleAddStock(event: AddStockEvent): DataStore = {
    val handler = (stockItem: StockItem) => {
      stockItem.handleAddStock(event)
    }
    handleEvent(event, handler, event.quantity)
  }

  def handleSellItem(event: SellItemEvent): DataStore = {
    val handler = (stockItem: StockItem) => {
      stockItem.handleSellItem(event)
    }
    handleEvent(event, handler, -1 * event.quantity)
  }

  def getStock: Seq[StockItem] = stock
}
