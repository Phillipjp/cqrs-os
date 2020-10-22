package cqrsos

class StockControlCommandService(eventStore: EventStore, eventBus: EventBus, stockLevelQueryService: StockLevelQueryService) extends CommandService{
  override def process(command: Command): Unit = {
    val event = command match {
      case c: AddStockCommand => StockAddedEvent(c.quantity)
      case c: SellStockCommand =>
        if(stockLevelQueryService.getStockCount >= c.quantity){
          StockSoldEvent(c.customerId, c.quantity)
        }
        else{
          StockNotSoldEvent(c.customerId, c.quantity)
        }

    }

    eventStore.storeEvent(event)

    eventBus.sendEvent(event)
  }
}
