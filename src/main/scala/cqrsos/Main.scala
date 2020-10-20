package cqrsos
import java.time.LocalDateTime
object Main {


  def main(args: Array[String]): Unit = {

    val emptyDataStore = LocalDataStore(Seq())
    val eventProcessor = EventProcessor("src/main/resources/event-log.txt")
    val dataStore = eventProcessor.replayEvents(emptyDataStore)
    println("Initial Stock:")
    dataStore.getStock.foreach(println)

    println("Adding 5 medium trousers")
    val ds1 = eventProcessor.processEvent(AddStockEvent(LocalDateTime.now().toString, Product("Trousers", "Medium", 15), 5), dataStore)
    println("Stock:")
    ds1.getStock.foreach(println)
    println("Selling 2 medium trousers")
    val ds2 = eventProcessor.processEvent(SellItemEvent(LocalDateTime.now().toString, Product("Trousers", "Medium", 15), 2), ds1)
    println("Stock:")
    ds2.getStock.foreach(println)
    println("Adding 10 Large trousers")
    val ds3 = eventProcessor.processEvent(AddStockEvent(LocalDateTime.now().toString, Product("Trousers", "Large", 15), 10), ds2)
    println("Stock:")
    ds3.getStock.foreach(println)
  }
}


