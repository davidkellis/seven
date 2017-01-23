package davidkellis.seven.domain

import davidkellis.seven.domain.CoreTypes.{Decimal, ShareQuantity, OrderQueue}
import java.time.ZonedDateTime

import scala.collection.mutable
import scala.util.Try

class BrokerageAccount(val broker: Broker,
                       val portfolio: Portfolio,
                       val openOrders: OrderQueue = mutable.Set.empty[Order],
                       val orderHistory: mutable.ListBuffer[Order] = mutable.ListBuffer.empty[Order]) {
  def marketBuyStock(security: Security, qty: ShareQuantity, time: ZonedDateTime): Try[Order] = {
    val order = Order.buildMarketBuy(this, Equity, security.id, qty, time)
    broker.placeOrder(order)
  }

  def marketSellStock(security: Security, qty: ShareQuantity, time: ZonedDateTime): Try[Order] = {
    val order = Order.buildMarketSell(this, Equity, security.id, qty, time)
    broker.placeOrder(order)
  }

  def limitBuyStock(security: Security, qty: ShareQuantity, limitPrice: Decimal, time: ZonedDateTime): Try[Order] = {
    val order = Order.buildLimitBuy(this, Equity, security.id, qty, limitPrice, time)
    broker.placeOrder(order)
  }

  def limitSellStock(security: Security, qty: ShareQuantity, limitPrice: Decimal, time: ZonedDateTime): Try[Order] = {
    val order = Order.buildLimitSell(this, Equity, security.id, qty, limitPrice, time)
    broker.placeOrder(order)
  }
}
