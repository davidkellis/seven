package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.Time.Timestamp
import com.github.davidkellis.seven.domain.CoreTypes.{OrderHistory, OrderQueue}
import com.github.davidkellis.seven.util.MutableTreeMap

import scala.collection.mutable

class BrokerageAccount(val broker: Broker,
                       val portfolio: Portfolio,
                       val openOrders: OrderQueue = mutable.Set.empty[Order],
                       val orderHistory: OrderHistory = MutableTreeMap.empty[Timestamp, Order]) {
  def buyStock(security: Security, qty: Long, time: Timestamp): Option[Order] = {
    val order = Order(this, Open, Equity, Market, Buy, GoodForDay, security.id, qty, false, time)
    if (broker.placeOrder(order))
      Some(order)
    else
      None
  }
}
