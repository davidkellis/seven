package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.domain.CoreTypes.{FillPriceFn, SecurityId}
import org.joda.time.DateTime

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.{Success, Try}

trait Exchange {
  def placeOrder(order: Order): Try[Unit]

  def cancelOrder(order: Order): Try[Unit]

  def fillOrders(time: DateTime): Try[Unit]
}

object OrderBook {
  def empty(): OrderBook = new OrderBook()
}
class OrderBook() {
  def add(order: Order): Unit = ???
}

class ConsolidatedOrderBook() {
  var orderBooks = mutable.Map.empty[SecurityId, OrderBook]

  def add(order: Order): Unit = {
    val orderBook = getOrderBook(order.securityId).getOrElse(addNewOrderBook(order.securityId))
    orderBook.add(order)
  }

  private def getOrderBook(securityId: SecurityId): Option[OrderBook] = orderBooks.get(securityId)

  private def addNewOrderBook(securityId: SecurityId): OrderBook = {
    val newOrderBook = OrderBook.empty
    orderBooks += (securityId -> newOrderBook)
    newOrderBook
  }
}


class SimExchange(fillPriceFn: FillPriceFn) extends Exchange {
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

  def fillOrders(time: DateTime): Try[Unit] = {
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