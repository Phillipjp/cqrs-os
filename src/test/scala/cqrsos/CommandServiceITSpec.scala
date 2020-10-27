package cqrsos

import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers


class CommandServiceITSpec extends AnyFlatSpecLike with Matchers {

  it should "update the stock level and customer order event handlers using a synchronous event bus" in {

    // Given
    val eventStore = new InMemoryEventStore()

    val stockLevelEventHandler = new StockLevelEventHandler
    val customerOrdersEventHandler = new CustomerOrdersEventHandler

    val eventBus = new SynchronousEventBus
    eventBus.subscribe(stockLevelEventHandler)
    eventBus.subscribe(customerOrdersEventHandler)

    val commandService= new StockControlCommandService(eventStore, eventBus, stockLevelEventHandler)

    // When
    commandService.process(AddStockCommand(1))
    commandService.process(AddStockCommand(1))
    commandService.process(AddStockCommand(2))
    commandService.process(SellStockCommand("cust-1", 2))
    commandService.process(SellStockCommand("cust-2", 1))
    commandService.process(SellStockCommand("cust-2", 3))

    eventBus.shutdown()

    // Then
    stockLevelEventHandler.getStockCount shouldBe 1
    customerOrdersEventHandler.getCustomerOrders("cust-1") shouldBe Seq(Order("cust-1",2,true))
    customerOrdersEventHandler.getCustomerOrders("cust-2") shouldBe Seq(Order("cust-2",1,true), Order("cust-2",3,false))
  }

  it should "update the stock level and customer order event handlers using an asynchronous event bus" in {

    // Given
    val eventStore = new InMemoryEventStore()

    val stockLevelEventHandler = new StockLevelEventHandler
    val customerOrdersEventHandler = new CustomerOrdersEventHandler

    val eventBus = new AsynchronousEventBus(2, 10)
    eventBus.subscribe(stockLevelEventHandler)
    eventBus.subscribe(customerOrdersEventHandler)

    val commandService= new StockControlCommandService(eventStore, eventBus, stockLevelEventHandler)

    // When
    commandService.process(AddStockCommand(1))
    commandService.process(AddStockCommand(1))
    commandService.process(AddStockCommand(2))
    commandService.process(SellStockCommand("cust-1", 2))
    commandService.process(SellStockCommand("cust-2", 1))
    commandService.process(SellStockCommand("cust-2", 3))

    eventBus.shutdown()

    // Then
    stockLevelEventHandler.getStockCount shouldBe 1
    customerOrdersEventHandler.getCustomerOrders("cust-1") shouldBe Seq(Order("cust-1",2,true))
    customerOrdersEventHandler.getCustomerOrders("cust-2") shouldBe Seq(Order("cust-2",1,true), Order("cust-2",3,false))
  }

  it should "replay events from the beginning to the latest event" in {

    // Given
    val eventStore = new InMemoryEventStore()

    val stockLevelEventHandler = new StockLevelEventHandler
    val customerOrdersEventHandler = new CustomerOrdersEventHandler

    val eventBus = new SynchronousEventBus
    eventBus.subscribe(stockLevelEventHandler)

    val commandService= new StockControlCommandService(eventStore, eventBus, stockLevelEventHandler)

    // When
    commandService.process(AddStockCommand(1))
    commandService.process(AddStockCommand(1))
    commandService.process(AddStockCommand(2))
    commandService.process(SellStockCommand("cust-1", 2))
    commandService.process(SellStockCommand("cust-2", 1))
    commandService.process(SellStockCommand("cust-2", 3))

    val replayEventBus = new SynchronousEventBus
    replayEventBus.subscribe(customerOrdersEventHandler)

    commandService.replay(replayEventBus, customerOrdersEventHandler.getLastProcessedEvent)

    replayEventBus.shutdown()
    eventBus.shutdown()

    // Then
    stockLevelEventHandler.getStockCount shouldBe 1
    customerOrdersEventHandler.getCustomerOrders("cust-1") shouldBe Seq(Order("cust-1",2,true))
    customerOrdersEventHandler.getCustomerOrders("cust-2") shouldBe Seq(Order("cust-2",1,true), Order("cust-2",3,false))

  }

}
