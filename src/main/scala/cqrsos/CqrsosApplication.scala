package cqrsos

object CqrsosApplication {

  def main(args: Array[String]): Unit = {

    val eventStore = new InMemoryEventStore()

    val stockLevelEventHandler = new StockLevelEventHandler
    val customerOrdersEventHandler = new CustomerOrdersEventHandler

//    val eventBus = new SynchronousEventBus()
    val eventBus = new AsynchronousEventBus(2, 10)
    eventBus.subscribe(stockLevelEventHandler)
    eventBus.subscribe(customerOrdersEventHandler)

    val commandService= new StockControlCommandService(eventStore, eventBus, stockLevelEventHandler)

    commandService.process(AddStockCommand(1))
    commandService.process(AddStockCommand(1))
    commandService.process(AddStockCommand(2))
    commandService.process(SellStockCommand("cust-1", 2))
    commandService.process(SellStockCommand("cust-2", 1))
    commandService.process(SellStockCommand("cust-2", 3))

    eventBus.shutdown()

    println(stockLevelEventHandler.getStockCount)
    println(customerOrdersEventHandler.getCustomerOrders("cust-1"))
    println(customerOrdersEventHandler.getCustomerOrders("cust-2"))

  }

}
