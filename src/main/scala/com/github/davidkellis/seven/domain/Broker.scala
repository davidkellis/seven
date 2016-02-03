package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.domain.CoreTypes.Decimal

import scala.util.Try

trait Broker {
  def placeOrder(account: BrokerageAccount, order: Order): Try[Unit]

  def cancelOrder(order: Order): Try[Unit]
}

trait SimulatedBroker extends Broker {
  // when the exchange fills the order, it calls the broker's notifyOrderFilled method to tell the broker that the order was filled
  def notifyOrderFilled(order: Order): Unit
}

// Simulated Scottrade broker
class ScottradeSim(val exchange: Exchange, val commissionPerTrade: Decimal = 7.0) extends SimulatedBroker {
  def placeOrder(account: BrokerageAccount, order: Order): Try[Unit] = {
    order.status = Open
    exchange.placeOrder(order)
  }

  def cancelOrder(order: Order): Try[Unit] = {
    exchange.cancelOrder(order)
  }

  def notifyOrderFilled(order: Order): Unit = ???
}