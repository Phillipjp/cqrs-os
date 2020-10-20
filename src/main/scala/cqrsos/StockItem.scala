package cqrsos

case class StockItem(product: Product, stockCount: Int) {


  def handleAddStock(event: AddStockEvent): StockItem = {
    this.copy(stockCount = stockCount + event.quantity)
  }

  def handleSellItem(event: SellItemEvent): StockItem = {
    this.copy(stockCount = stockCount - event.quantity)
  }

}
