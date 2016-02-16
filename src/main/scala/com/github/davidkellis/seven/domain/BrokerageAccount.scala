package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.Time.Timestamp
import com.github.davidkellis.seven.domain.CoreTypes.{Decimal, ShareQuantity, OrderHistory, OrderQueue}
import com.github.davidkellis.seven.util.MutableTreeMap
import org.joda.time.DateTime

import scala.collection.mutable
import scala.util.Try

class BrokerageAccount(val broker: Broker,
                       val portfolio: Portfolio,
                       val openOrders: OrderQueue = mutable.Set.empty[Order],
                       val orderHistory: OrderHistory = MutableTreeMap.empty[Timestamp, Order]) {
  def marketBuyStock(security: Security, qty: ShareQuantity, time: DateTime): Try[Order] = {
    val order = Order.buildMarketBuy(this, Equity, security.id, qty, time)
    broker.placeOrder(order)
  }

  def marketSellStock(security: Security, qty: ShareQuantity, time: DateTime): Try[Order] = {
    val order = Order.buildMarketSell(this, Equity, security.id, qty, time)
    broker.placeOrder(order)
  }

  def limitBuyStock(security: Security, qty: ShareQuantity, limitPrice: Decimal, time: DateTime): Try[Order] = {
    val order = Order.buildLimitBuy(this, Equity, security.id, qty, limitPrice, time)
    broker.placeOrder(order)
  }

  def limitSellStock(security: Security, qty: ShareQuantity, limitPrice: Decimal, time: DateTime): Try[Order] = {
    val order = Order.buildLimitSell(this, Equity, security.id, qty, limitPrice, time)
    broker.placeOrder(order)
  }
}
