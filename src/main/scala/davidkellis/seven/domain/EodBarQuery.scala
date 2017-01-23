package davidkellis.seven.domain

import java.util

import davidkellis.seven.Time
import davidkellis.seven.Time.Datestamp
import davidkellis.seven.data.Dao

class EodBarQuery(dao: Dao) {

  type PriceHistory = util.NavigableMap[Datestamp, EodBar]   // a price history is a collection of (Datestamp -> EodBar) pairs

  def loadPriceHistoryFromBars(eodBars: Seq[EodBar]): PriceHistory = {
    val priceHistory: PriceHistory = new util.TreeMap[Datestamp, EodBar]()
    eodBars.foreach { eodBar => priceHistory.put(Time.datestamp(eodBar.date), eodBar) }
    priceHistory
  }

}
