package com.github.davidkellis.seven.domain

import java.util

import com.github.davidkellis.seven.Time.Timestamp
import org.joda.time.DateTime

object CoreTypes {
  type Decimal = Double
  type IntegerId = Long
  type ShareQuantity = Int

  type OrderQueue = scala.collection.mutable.Set[Order]

  type FillPriceFn = (Order, DateTime) => Option[Decimal]

  trait TradingEvent
  case object ReEvaluate extends TradingEvent
  case object PriceChange extends TradingEvent
  case object Open extends TradingEvent
  case object Close extends TradingEvent
}
