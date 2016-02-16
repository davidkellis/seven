package com.github.davidkellis.seven.domain

import java.util.UUID

import com.github.davidkellis.seven.domain.CoreTypes.{ShareQuantity, IntegerId, Decimal}
import com.github.davidkellis.seven.Time.{Timestamp, timestamp}
import org.joda.time.DateTime

// modeled after docs at https://content.etrade.com/etrade/estation/pdf/API_Technical_Documentation.pdf

sealed trait OrderStatus
case object Open extends OrderStatus
case object Filled extends OrderStatus
case object Cancelled extends OrderStatus
case object CancelRequested extends OrderStatus
case object Expired extends OrderStatus
case object Rejected extends OrderStatus

sealed trait SecurityType
case object Equity extends SecurityType
case object OptionsContract extends SecurityType
case object FuturesContract extends SecurityType

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
//case object BuyOpen extends OrderAction       // buy at open
//case object SellOpen extends OrderAction      // sell at open
//case object BuyClose extends OrderAction      // buy at close
//case object SellClose extends OrderAction     // sell at close

// OrderTerm is sometimes also referred to as Time In Force
sealed trait OrderTerm
case object GoodUntilCancel extends OrderTerm
case object GoodForDay extends OrderTerm
case object FillOrKill extends OrderTerm

// we are only implementing single-leg orders right now
case class Order(
                  var id: Option[UUID],
                  account: BrokerageAccount,
                  var status: OrderStatus,
                  securityType: SecurityType,
                  priceType: PriceType,
                  action: OrderAction,
                  term: OrderTerm,
                  securityId: IntegerId,
                  quantity: ShareQuantity,
                  allOrNone: Boolean,
                  timePlaced: Timestamp,
                  var filledQuantity: Option[Long],
                  var filledPrice: Option[Decimal],
                  var commission: Option[Decimal],
                  var timeExecuted: Option[Timestamp],
                  var limitPrice: Option[Decimal],
                  var stopPrice: Option[Decimal],
                  var stopLimitPrice: Option[Decimal]
                )

// todo, turn the methods buildMarketBuy, buildMarketSell, into proper methods, with named parameters
object Order {
  def buildMarketBuy(account: BrokerageAccount, securityType: SecurityType, securityId: IntegerId, qty: ShareQuantity, time: DateTime) =
    Order(Some(java.util.UUID.randomUUID), account, Open, securityType, Market, Buy, GoodForDay, securityId, qty, false, timestamp(time), None, None, None, None, None, None, None)

  def buildMarketSell(account: BrokerageAccount, securityType: SecurityType, securityId: IntegerId, qty: ShareQuantity, time: DateTime) =
    Order(Some(java.util.UUID.randomUUID), account, Open, securityType, Market, Sell, GoodForDay, securityId, qty, false, timestamp(time), None, None, None, None, None, None, None)

  def buildLimitBuy(account: BrokerageAccount, securityType: SecurityType, securityId: IntegerId, qty: ShareQuantity, limitPrice: Decimal, time: DateTime) =
    Order(Some(java.util.UUID.randomUUID), account, Open, securityType, Limit, Buy, GoodForDay, securityId, qty, false, timestamp(time), None, None, None, None, Some(limitPrice), None, None)

  def buildLimitSell(account: BrokerageAccount, securityType: SecurityType, securityId: IntegerId, qty: ShareQuantity, limitPrice: Decimal, time: DateTime) =
    Order(Some(java.util.UUID.randomUUID), account, Open, securityType, Limit, Sell, GoodForDay, securityId, qty, false, timestamp(time), None, None, None, None, Some(limitPrice), None, None)

  def markFilled(order: Order, fillPrice: Decimal, commission: Decimal): Unit = {
    order.status = Filled
    order.filledQuantity = Some(order.quantity)
    order.filledPrice = Some(fillPrice)
    order.commission = Some(commission)
  }
}