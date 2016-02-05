package com.github.davidkellis.seven.strategies

import com.github.davidkellis.seven.Time
import com.github.davidkellis.seven.Time.January
import com.github.davidkellis.seven.data.Dao
import com.github.davidkellis.seven.domain.CoreTypes.TradingEvent
import com.github.davidkellis.seven.domain._
import org.joda.time.DateTime

object BuyAndHold {

  trait CurrentState
  case object NotInvested extends CurrentState
  case object Invested extends CurrentState

  class BuyAndHoldSingle(account: BrokerageAccount, security: Security) extends Strategy {
    var currentState = NotInvested

    def evaluate(time: DateTime, event: TradingEvent): Unit = {
      currentState match {
        case NotInvested =>
          if (canAfford(security, account)) {
            account.buyStock(security, )
          }
        case Invested =>
      }
    }

    private def canAfford(security: Security, account: BrokerageAccount): Boolean = ???
  }

  object Scenarios {
    def runSingleBuyAndHoldTrial(dao: Dao): Unit = {
      val simulator = new Simulator()
      val fillPriceFn = (order: Order) => order.action match {
        case Buy => ???
        case Sell => ???
        case _ => throw new Exception("Unknown order action. Unable to calculate simulated fill price.")
      }
      val exchange = new SimExchange(fillPriceFn)
      val broker = new ScottradeSim(exchange, 7.0)
      val portfolio = new Portfolio(10000.0)
      val account = new BrokerageAccount(broker, portfolio)
      val usCompositeExchange = dao.findExchange("US")
      val appl = dao.findSecurity("AAPL")
      val strategy = new BuyAndHoldSingle(account, appl)
      val trial = new Trial(
        Time.datetime(2000, January, 1),
        Time.datetime(2016, January, 1)
      )
      simulator.run(trial)
    }
  }

}