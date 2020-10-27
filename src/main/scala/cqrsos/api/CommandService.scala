package cqrsos.api

import cqrsos.Command

trait CommandService {
  def process(command: Command): Unit

  def replay(replayEventBus: EventBus, replayEventNumber: Int)
}
