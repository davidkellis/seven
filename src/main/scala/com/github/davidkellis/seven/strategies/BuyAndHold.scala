package com.github.davidkellis.seven.strategies

import com.github.davidkellis.seven.Time
import com.github.davidkellis.seven.domain.{Security, BrokerageAccount, Strategy}

class BuyAndHold(startTime: Time.Timestamp, endTime: Time.Timestamp, security: Security) extends Strategy {
  def trade(account: BrokerageAccount, time: Time.Timestamp): Unit = {
    if (canAfford(security, account))
  }

  private def canAfford(security: Security, account: BrokerageAccount): Boolean = ???
}
