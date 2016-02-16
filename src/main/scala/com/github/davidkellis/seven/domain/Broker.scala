package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.domain.CoreTypes.Decimal

import scala.util.Try

trait Broker {
  def placeOrder(order: Order): Try[Order]

  def cancelOrder(order: Order): Try[Order]

  def costOfTransactionFees(order: Order): Decimal

  // when the exchange fills the order, it calls the broker's notifyOrderFilled method to tell the broker that the order was filled
  def notifyOrderFilled(order: Order): Unit
}


// Simulated Scottrade broker
class ScottradeSim(
                    val exchange: Exchange,
                    val equityCommissionPerTrade: Decimal = 7.0,
                    val optionsBaseCommissionPerTrade: Decimal = 7.00,
                    val optionsCommissionPerContract: Decimal = 1.25
                  ) extends Broker {
  def placeOrder(order: Order): Try[Order] = {
    order.id = Some(java.util.UUID.randomUUID)
    order.status = Open
    exchange.placeOrder(order).map(_ => order)
  }

  def cancelOrder(order: Order): Try[Order] = {
    exchange.cancelOrder(order).map(_ => order)
  }

  def costOfTransactionFees(order: Order): Decimal = {
    order match {
      case Order(_, _, _, Equity, _, _, _, _, qty, _, _, _, _, _, _, _, _, _) => equityCommissionPerTrade
      case Order(_, _, _, OptionsContract, _, _, _, _, qty, _, _, _, _, _, _, _, _, _) => optionsBaseCommissionPerTrade + optionsCommissionPerContract * qty
      case _ => throw new Exception("Simulated Scottrade broker doesn't know how to handle an order of this type.")
    }
  }

  def notifyOrderFilled(order: Order): Unit = {
  }
}


// Simulated OptionsHouse broker
class OptionsHouseSim(
                    val exchange: Exchange,
                    val equityCommissionPerTrade: Decimal = 4.95,
                    val optionsBaseCommissionPerTrade: Decimal = 4.95,
                    val optionsCommissionPerContract: Decimal = 0.5
                    ) extends Broker {
  def placeOrder(order: Order): Try[Order] = {
    order.id = Some(java.util.UUID.randomUUID)
    order.status = Open
    exchange.placeOrder(order).map(_ => order)
  }

  def cancelOrder(order: Order): Try[Order] = {
    exchange.cancelOrder(order).map(_ => order)
  }

  def costOfTransactionFees(order: Order): Decimal = {
    order match {
      case Order(_, _, _, Equity, _, _, _, _, qty, _, _, _, _, _, _, _, _, _) => equityCommissionPerTrade
      case Order(_, _, _, OptionsContract, _, _, _, _, qty, _, _, _, _, _, _, _, _, _) => optionsBaseCommissionPerTrade + optionsCommissionPerContract * qty
      case _ => throw new Exception("Simulated OptionsHouse broker doesn't know how to handle an order of this type.")
    }
  }

  def notifyOrderFilled(order: Order): Unit = ???
}