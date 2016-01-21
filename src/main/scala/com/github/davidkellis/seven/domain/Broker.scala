package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.Time

import scala.collection.mutable

trait Broker {
  // returns true if order was successfully placed; false otherwise
  def placeOrder(account: BrokerageAccount, order: Order): Boolean

  // returns true if order was successfully cancelled; false otherwise
  def cancelOrder(order: Order): Boolean
}

trait SimulatedBroker extends Broker {
  def executeOrders(time: Time.Timestamp): Order
}

// Simulated Scottrade broker
class ScottradeSim() extends SimulatedBroker {
  val unfilledOrders = mutable.ListBuffer.empty[Order]

  def placeOrder(account: BrokerageAccount, order: Order): Boolean = {
    order.status = Open
    unfilledOrders += order
    true
  }

  def cancelOrder(order: Order): Boolean = {
    unfilledOrders -= order
    true
  }

  def executeOrders(time: Time.Timestamp): Unit = ???
}