package cqrsos

trait DataStore {

  def getStock: Seq[StockItem]

  def handleAddStock(event: AddStockEvent): DataStore

  def handleSellItem(event: SellItemEvent): DataStore

}
