package cqrsos

import java.io.{FileWriter, StringWriter}

import scala.annotation.tailrec
import scala.io.Source

case class EventProcessor(logLocation: String) {

  val mapper = JsonObjectMapper.objectMapper

  def processEvent(event: Event, dataSore: DataStore): DataStore = {

    val writer = new StringWriter()
    mapper.writeValue(writer, event)
    val jsonEvent = writer.toString

    val fw = new FileWriter(logLocation, true)
    try {
      fw.write(s"$jsonEvent\n")
    }
    finally fw.close()
    event.process(dataSore)
  }

  def replayEvents(dataStore: DataStore): DataStore = {
    val buffer = Source.fromFile(logLocation)
    val lines = buffer.getLines.toList
    buffer.close()

    val events: Seq[Event] = lines.map{ line =>
      if(line.contains("AddStockEvent")){
        mapper.readValue(line, classOf[AddStockEvent])
      }
      else{
        mapper.readValue(line, classOf[SellItemEvent])
      }
    }
    replay(dataStore, events)
  }

  @tailrec
  private def replay(dataStore: DataStore, events: Seq[Event]): DataStore = {
    if(events.isEmpty){
      dataStore
    }
    else {
      val ds = events.head match {
        case e: AddStockEvent => dataStore.handleAddStock(e)
        case e: SellItemEvent => dataStore.handleSellItem(e)
      }
      replay(ds, events.tail)
    }
  }

}
