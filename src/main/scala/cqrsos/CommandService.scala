package cqrsos

trait CommandService {
  def process(command: Command): Unit
}
