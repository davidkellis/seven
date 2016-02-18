package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.Time
import com.github.davidkellis.seven.domain.CoreTypes.FillPriceFn
import org.joda.time.DateTime

class Simulator() {
  def run(exchanges: Seq[Exchange], strategy: Strategy, trial: Trial): Unit = {
    val tradingDays = TradingSchedule.tradingDays(trial.startTime.toLocalDate, trial.endTime.toLocalDate, TradingSchedule.defaultTradingSchedule(_))
    val tradingTimes = trial.dailyTradingTimes
    var currentTime = tradingDays.head.toDateTime(tradingTimes.head, Time.EasternTimeZone)
    var previousTime = currentTime
    tradingDays.foreach { localDate =>
      tradingTimes.foreach { localTime =>
        previousTime = currentTime
        currentTime = localDate.toDateTime(localTime, Time.EasternTimeZone)

        // todo: adjust (1) all open orders and order cancellations tracked by the exchanges, as well as (2) the portfolio managed by the strategy
        // for splits and dividends that have gone into effect at some point within the interval (previousTime, currentTime]

        // todo: log the current value of the portfolio managed by the strategy
        strategy.evaluate(previousTime, currentTime, CoreTypes.ReEvaluate)
        // todo: perhaps increment currentTime by some small amount of time here
        fillOrders(exchanges, trial.fillPriceFn, previousTime, currentTime)
      }
    }
  }

  def fillOrders(exchanges: Seq[Exchange], fillPriceFn: FillPriceFn, previousTime: DateTime, currentTime: DateTime): Unit = {
    exchanges.foreach { exchange =>
      exchange.fillOrders(fillPriceFn, previousTime, currentTime)
    }
  }
}
