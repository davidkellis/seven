package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.Time

class Simulator() {
  def run(trial: Trial): Unit = {
    val tradingDays: Seq[Time.Datestamp] = ???
    val defaultTradingHours: Array[Time.Timestamp] = ???
  }
}
