package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.Time

// modeled after docs at https://content.etrade.com/etrade/estation/pdf/API_Technical_Documentation.pdf

sealed trait OrderStatus
case object Open extends OrderStatus
case object Executed extends OrderStatus
case object Cancelled extends OrderStatus
case object CancelRequested extends OrderStatus
case object Expired extends OrderStatus
case object Rejected extends OrderStatus

sealed trait OrderKind
case object Equity extends OrderKind
case object Option extends OrderKind
case object Spread extends OrderKind

sealed trait PriceType
case object Market extends PriceType
case object Limit extends PriceType
case object Stop extends PriceType
case object StopLimit extends PriceType

sealed trait OrderAction
case object Buy extends OrderAction           // open long position
case object Sell extends OrderAction          // close long position
case object BuyToCover extends OrderAction    // close short position
case object SellShort extends OrderAction     // open long position
case object BuyOpen extends OrderAction       // buy at open
case object SellOpen extends OrderAction      // sell at open
case object BuyClose extends OrderAction      // buy at close
case object SellClose extends OrderAction     // sell at close

// OrderTerm is sometimes also referred to as Time In Force
sealed trait OrderTerm
case object GoodUntilCancel extends OrderTerm
case object GoodForDay extends OrderTerm
case object FillOrKill extends OrderTerm

// we are only implementing single-leg orders right now
case class Order(
  account: BrokerageAccount,
  var status: OrderStatus,
  kind: OrderKind,
  priceType: PriceType,
  action: OrderAction,
  term: OrderTerm,
  securityId: Int,
  quantity: Double,
  allOrNone: Boolean,
  timePlaced: Time.Timestamp,
  var filledQuantity: Option[Double],
  var executedPrice: Option[Double],
  var commission: Option[Double],
  var timeExecuted: Option[Time.Timestamp],
  var limitPrice: Option[Double],
  var stopPrice: Option[Double],
  var stopLimitPrice: Option[Double]
)

object Order {
  val buildMarketBuy = Order(_, Open, _, Market, Buy, GoodForDay, _, _, false, _, None, None, None, None, None, None, None)
}