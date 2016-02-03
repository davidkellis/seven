package com.github.davidkellis.seven.domain

import java.util

import com.github.davidkellis.seven.Time.Timestamp

object CoreTypes {
  type Decimal = Double
  type SecurityId = Int

  type OrderQueue = scala.collection.mutable.Set[Order]
  type OrderHistory = util.TreeMap[Timestamp, Order]

  trait TradingEvent
  case object ReEvaluate extends TradingEvent
  case object PriceChange extends TradingEvent
  case object Open extends TradingEvent
  case object Close extends TradingEvent
}
