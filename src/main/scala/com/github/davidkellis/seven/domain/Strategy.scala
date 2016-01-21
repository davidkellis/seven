package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.Time

trait Strategy {
  def trade(account: BrokerageAccount, time: Time.Timestamp): Unit
}
