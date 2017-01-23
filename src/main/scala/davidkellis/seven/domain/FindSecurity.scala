package davidkellis.seven.domain

import davidkellis.seven.data.Dao.dao

object FindSecurity {
  def apply(ticker: String, exchange: Exchange): Option[Security] = {
    dao.findSecurities(Array(ticker), Array(exchange)).headOption
  }
}
