package cqrsos

trait Command
case class AddStockCommand(quantity: Int) extends Command
case class SellStockCommand(customerId: String, quantity: Int) extends Command
