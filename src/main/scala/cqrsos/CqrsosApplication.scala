package cqrsos

object CqrsosApplication {

  def main(args: Array[String]): Unit = {

    val eventStore = new InMemoryEventStore()

    val stockLevelQueryService = new StockLevelQueryService
    val customerOrdersQueryService = new CustomerOrdersQueryService

    val eventBus = new SynchronousEventBus(Seq(stockLevelQueryService, customerOrdersQueryService))

    val commandService= new StockControlCommandService(eventStore, eventBus, stockLevelQueryService)

    commandService.process(AddStockCommand(1))
    commandService.process(AddStockCommand(1))
    commandService.process(AddStockCommand(2))
    commandService.process(SellStockCommand("cust-1", 2))
    commandService.process(SellStockCommand("cust-2", 1))
    commandService.process(SellStockCommand("cust-2", 3))

    println(stockLevelQueryService.getStockCount)
    println(customerOrdersQueryService.getCustomerOrders("cust-1"))
    println(customerOrdersQueryService.getCustomerOrders("cust-2"))

  }

}
