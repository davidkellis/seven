package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.data.Dao.dynamicDao

object FindExchange {
  def apply(exchangeLabel: String): Option[Exchange] = {
    dynamicDao.findExchanges(Array(exchangeLabel)).headOption
  }
}
