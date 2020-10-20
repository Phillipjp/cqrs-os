package cqrsos

case class AddStockEvent(timestamp: String, product: Product, quantity: Int) extends Event {

  override def process(dataSore: DataStore): DataStore = {
    dataSore.handleAddStock(this)
  }

  override val eventType: String = "AddStockEvent"
}
