package cqrsos

import cqrsos.api.{CommandService, EventBus, EventStore}

class StockControlCommandService(eventStore: EventStore, eventBus: EventBus, stockLevelEventHandler: StockLevelEventHandler) extends CommandService{

  private var currentEventNumber: Int = 0

  override def process(command: Command): Unit = {
    val event = command match {
      case c: AddStockCommand =>
        val e = StockAddedEvent(currentEventNumber, c.quantity)
        currentEventNumber += 1
        e

      case c: SellStockCommand =>
        val e = if(stockLevelEventHandler.getStockCount >= c.quantity){
          StockSoldEvent(currentEventNumber, c.customerId, c.quantity)
        }
        else{
          StockNotSoldEvent(currentEventNumber, c.customerId, c.quantity)
        }
        currentEventNumber += 1
        e
    }

    eventStore.storeEvent(event)

    eventBus.sendEvent(event)
  }

  override def replay(replayEventBus: EventBus, replayEventNumber: Int): Unit = {

    val replayEvents = eventStore.getEventLog
      .toSeq
      .filter(_.eventNumber >= replayEventNumber)
      .sortWith((e1, e2) => e1.eventNumber < e2.eventNumber)

    replayEvents.foreach(event => replayEventBus.sendEvent(event))

  }
}
