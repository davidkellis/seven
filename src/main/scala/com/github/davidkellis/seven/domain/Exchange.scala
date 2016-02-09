package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.domain.CoreTypes.{FillPriceFn, IntegerId}
import org.joda.time.DateTime

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.{Success, Try}

object OrderBook {
  def empty(): OrderBook = new OrderBook()
}
class OrderBook() {
  def add(order: Order): Unit = ???
}

class ConsolidatedOrderBook() {
  var orderBooks = mutable.Map.empty[IntegerId, OrderBook]

  def add(order: Order): Unit = {
    val orderBook = getOrderBook(order.securityId).getOrElse(addNewOrderBook(order.securityId))
    orderBook.add(order)
  }

  private def getOrderBook(securityId: IntegerId): Option[OrderBook] = orderBooks.get(securityId)

  private def addNewOrderBook(securityId: IntegerId): OrderBook = {
    val newOrderBook = OrderBook.empty
    orderBooks += (securityId -> newOrderBook)
    newOrderBook
  }
}

object Exchange {
  def unapply(exchange: Exchange): Option[(Long, String, String, Boolean, Option[Long])] = {
    Some(
      (
        exchange.id,
        exchange.label,
        exchange.name,
        exchange.isCompositeExchange,
        exchange.compositeExchangeId
      )
    )
  }
}
class Exchange(val id: Long,
               val label: String,
               val name: String,
               val isCompositeExchange: Boolean,
               val compositeExchangeId: Option[Long]) {
  var orderQueue = ListBuffer.empty[Order]
  var orderCancellations = mutable.Set.empty[Order]
  var orderBook = new ConsolidatedOrderBook()

  def placeOrder(order: Order): Try[Unit] = {
    orderQueue += order
    Success()
  }

  def cancelOrder(order: Order): Try[Unit] = {
    orderCancellations += order
    Success()
  }

  def fillOrders(time: DateTime, fillPriceFn: FillPriceFn): Try[Unit] = {
    // 1. cancel orders

    // 2. fill orders


    moveOrdersToOrderBook(time)

    // todo:

    Success()
  }

  private def moveOrdersToOrderBook(time: DateTime): Unit = {
    val newOrders = orderQueue.synchronized {
      val clone = orderQueue.clone
      orderQueue.clear
      clone
    }
    newOrders.foreach(orderBook.add(_))
  }
}