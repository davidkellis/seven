package davidkellis.seven.domain

import java.util

import davidkellis.seven.Time.Timestamp
import java.time.ZonedDateTime

object CoreTypes {
  type Decimal = Double
  type IntegerId = Long
  type ShareQuantity = Int

  type OrderQueue = scala.collection.mutable.Set[Order]

  type FillPriceFn = (Order, ZonedDateTime) => Option[Decimal]

  trait TradingEvent
  case object ReEvaluate extends TradingEvent
  case object PriceChange extends TradingEvent
  case object Open extends TradingEvent
  case object Close extends TradingEvent
}
