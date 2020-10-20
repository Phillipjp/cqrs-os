package cqrsos

case class SellItemEvent(timestamp: String, product: Product, quantity: Int) extends Event {

  override def process(dataSore: DataStore): DataStore = {
    dataSore.handleSellItem(this)
  }

  override val eventType: String = "SellItemEvent"
}
