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
    order.account.openOrders += order             // add the order to the account's open orders
    order.account.orderHistory += order           // add the order to the account's order history
    exchange.placeOrder(order).map(_ => order)
  }

  def cancelOrder(order: Order): Try[Order] = {
    // todo: Perhaps we want to track when the cancellation request was made?
    //       If so, we need a new field in the Order class to track when the cancellation was requested.
    exchange.cancelOrder(order).map(_ => order)
  }

  def costOfTransactionFees(order: Order): Decimal = {
    order match {
      case Order(_, _, _, Equity, _, _, _, _, qty, _, _, _, _, _, _, _, _, _) => equityCommissionPerTrade
      case Order(_, _, _, OptionsContract, _, _, _, _, qty, _, _, _, _, _, _, _, _, _) => optionsBaseCommissionPerTrade + optionsCommissionPerContract * qty
      case _ => throw new Exception("Simulated Scottrade broker doesn't know how to handle an order of this type.")
    }
  }

  def notifyOrderFilled(filledOrder: Order): Unit = {
    adjustPortfolioFromFilledOrder(filledOrder)
  }

  // todo: finish implementing this
  private def adjustPortfolioFromFilledOrder(filledOrder: Order): Unit = {
    val account = filledOrder.account
    val portfolio = account.portfolio
    val orderCommissionsAndFees = costOfTransactionFees(filledOrder)

    filledOrder match {
      case Order(_, _, _, _, _, Buy, _, _, _, _, _, _, _, _, _, _, _, _) =>
        // adjust the portfolio for a purchase
        portfolio.addToLongPosition(filledOrder.securityId, filledOrder.filledQuantity.get)
        portfolio.reduceCash(Order.purchaseCost(account.broker, ???, filledOrder, ???))
      case Order(_, _, _, _, _, BuyToCover, _, _, _, _, _, _, _, _, _, _, _, _) =>
      case Order(_, _, _, _, _, Sell, _, _, _, _, _, _, _, _, _, _, _, _) =>
        // adjust the portfolio for a sale
        portfolio.copy(stocks = portfolio.stocks + (securityId -> (sharesHeld - orderQty)),
          cash = cashOnHand + saleProceeds(orderQty, fillPrice, commissionPerTrade, commissionPerShare))
      case Order(_, _, _, _, _, SellShort, _, _, _, _, _, _, _, _, _, _, _, _) =>
      case _ => throw new Exception("Unknown order type. Unable to adjust portfolio from the filled order.")
    }

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
    order.account.openOrders += order             // add the order to the account's open orders
    order.account.orderHistory += order           // add the order to the account's order history
    exchange.placeOrder(order).map(_ => order)
  }

  def cancelOrder(order: Order): Try[Order] = {
    // todo: Perhaps we want to track when the cancellation request was made?
    //       If so, we need a new field in the Order class to track when the cancellation was requested.
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