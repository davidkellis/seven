package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.Time

class Simulator() {
  def run(trial: Trial): Unit = {
    val tradingDays = TradingSchedule.tradingDays(trial.startTime.toLocalDate, trial.endTime.toLocalDate, TradingSchedule.defaultTradingSchedule(_))
    val defaultTradingHours: Array[Time.Timestamp] = ???
  }
}
