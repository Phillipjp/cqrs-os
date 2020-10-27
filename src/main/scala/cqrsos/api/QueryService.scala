package cqrsos.api

import cqrsos.Event

trait QueryService {
  def processEvent(event: Event): Unit
}
