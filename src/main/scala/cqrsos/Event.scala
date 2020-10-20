package cqrsos

trait Event {

  val eventType: String
  val timestamp: String
  val product: Product

  def process(dataSore: DataStore): DataStore

}
