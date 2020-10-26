package cqrsos

import java.util.concurrent.{Executors, LinkedBlockingQueue}

object CqrsosApplication {

  def main(args: Array[String]): Unit = {

    val eventStore = new InMemoryEventStore()

    val stockLevelQueryService = new StockLevelQueryService
    val customerOrdersQueryService = new CustomerOrdersQueryService

    val queryServices = Seq(stockLevelQueryService, customerOrdersQueryService)

//    val eventBus = new SynchronousEventBus(queryServices)
    val executorService = Executors.newFixedThreadPool(queryServices.length)
    val eventBus = new AsynchronousEventBus(new LinkedBlockingQueue[Event](), executorService, queryServices)

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

    executorService.shutdown()

  }

}
