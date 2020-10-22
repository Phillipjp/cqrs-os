package cqrsos

trait QueryService{
  def processEvent(event: Event): Unit
}