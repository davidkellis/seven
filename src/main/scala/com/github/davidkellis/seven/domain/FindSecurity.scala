package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.data.Dao.dynamicDao

object FindSecurity {
  def apply(ticker: String, exchange: Exchange): Option[Security] = {
    dynamicDao.findSecurities(Array(ticker), Array(exchange)).headOption
  }
}
