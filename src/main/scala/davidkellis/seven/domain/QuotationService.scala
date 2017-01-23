package davidkellis.seven.domain

import davidkellis.seven.domain.CoreTypes.Decimal
import java.time.ZonedDateTime

trait QuotationService {
  def bid(security: Security, time: ZonedDateTime): Option[Decimal]
  def ask(security: Security, time: ZonedDateTime): Option[Decimal]

  def market(buyOrSell: OrderAction, security: Security, time: ZonedDateTime): Option[Decimal] = buyOrSell match {
    case Buy | BuyToCover => ask(security, time)    // In our simplified world, a market buy order would buy at the ask price
    case Sell | SellShort => bid(security, time)    // In our simplified world, a market sell order would sell at the bid price
  }
}

class SimpleQuotationService() extends QuotationService {
  def bid(security: Security, time: ZonedDateTime): Option[Decimal] = ???

  def ask(security: Security, time: ZonedDateTime): Option[Decimal] = ???
}