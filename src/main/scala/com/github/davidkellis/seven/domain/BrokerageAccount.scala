package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.Time

class BrokerageAccount(val broker: Broker,
                       val portfolio: Portfolio,
                       val openOrders: OrderQueue,
                       val orderHistory: OrderHistory) {
  def buyStock(security: Security, qty: Double, time: Time.Timestamp): Option[Order] = {
    val order = Order(this, Open, Equity, Market, Buy, GoodForDay, security.id, qty, false, time)
    if (broker.placeOrder(order))
      Some(order)
    else
      None
  }
}
