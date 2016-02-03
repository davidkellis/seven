package com.github.davidkellis.seven.domain

import java.util

import com.github.davidkellis.seven.Time
import com.github.davidkellis.seven.Time.Datestamp
import com.github.davidkellis.seven.data.Dao

class EodBarQuery(dao: Dao) {

  type PriceHistory = util.NavigableMap[Datestamp, EodBar]   // a price history is a collection of (Datestamp -> EodBar) pairs

  def loadPriceHistoryFromBars(eodBars: Seq[EodBar]): PriceHistory = {
    val priceHistory: PriceHistory = new util.TreeMap[Datestamp, EodBar]()
    eodBars.foreach { eodBar => priceHistory.put(Time.datestamp(eodBar.date), eodBar) }
    priceHistory
  }

}
