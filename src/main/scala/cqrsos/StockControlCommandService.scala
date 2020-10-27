package cqrsos

import java.time.LocalDateTime

import cqrsos.api.{CommandService, EventBus, EventStore}

class StockControlCommandService(eventStore: EventStore, eventBus: EventBus, stockLevelQueryService: StockLevelQueryService) extends CommandService{
  override def process(command: Command): Unit = {
    val now = LocalDateTime.now()
    val event = command match {
      case c: AddStockCommand => StockAddedEvent(now, c.quantity)
      case c: SellStockCommand =>
        if(stockLevelQueryService.getStockCount >= c.quantity){
          StockSoldEvent(now, c.customerId, c.quantity)
        }
        else{
          StockNotSoldEvent(now, c.customerId, c.quantity)
        }

    }

    eventStore.storeEvent(event)

    eventBus.sendEvent(event)
  }
}
