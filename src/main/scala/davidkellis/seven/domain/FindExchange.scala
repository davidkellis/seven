package davidkellis.seven.domain

import davidkellis.seven.data.Dao.dao

object FindExchange {
  def apply(exchangeLabel: String): Option[Exchange] = {
    dao.findExchanges(Array(exchangeLabel)).headOption
  }
}
