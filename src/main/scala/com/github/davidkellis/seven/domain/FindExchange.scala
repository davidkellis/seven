package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.data.Dao.dao

object FindExchange {
  def apply(exchangeLabel: String): Option[Exchange] = {
    dao.findExchanges(Array(exchangeLabel)).headOption
  }
}
