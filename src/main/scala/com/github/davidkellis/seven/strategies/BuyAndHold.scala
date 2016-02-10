package com.github.davidkellis.seven.strategies

import com.github.davidkellis.seven.Time
import com.github.davidkellis.seven.Time.January
import com.github.davidkellis.seven.data.Dao
import com.github.davidkellis.seven.domain.CoreTypes.{ShareQuantity, TradingEvent}
import com.github.davidkellis.seven.domain._
import org.joda.time.DateTime

import Dao.dynamicDao

object BuyAndHold {

  trait CurrentState
  case object NotInvested extends CurrentState
  case object Invested extends CurrentState

  class BuyAndHoldSingleStock(account: BrokerageAccount, quotationService: QuotationService, security: Security) extends Strategy {
    var currentState = NotInvested

    def evaluate(time: DateTime, event: TradingEvent): Unit = {
      currentState match {
        case NotInvested =>
          val qty = maxSharesAccountCanAfford(account, quotationService, security, time)
          account.marketBuyStock(security, qty, time)
        case Invested =>
      }
    }

    // todo, complete this implementation
    private def maxSharesAccountCanAfford(account: BrokerageAccount, quotationService: QuotationService, security: Security, time: DateTime): ShareQuantity = {
      for {
        price <- quotationService.market(Buy, security, time)
      } yield Math.floor(account.portfolio.cashOnHand / price)
    }
  }

  object Scenarios {
    def runSingleBuyAndHoldTrial(): Unit = {
      val simulator = new Simulator()
      val fillPriceFn = (order: Order) => order.action match {
        case Buy => ???
        case Sell => ???
        case _ => throw new Exception("Unknown order action. Unable to calculate simulated fill price.")
      }
      for {
        exchange <- FindExchange("US")
        broker = new ScottradeSim(exchange, 7.0)
        portfolio = new Portfolio(10000.0)
        account = new BrokerageAccount(broker, portfolio)
        appl <- FindSecurity("AAPL", exchange)
        strategy = new BuyAndHoldSingleStock(account, appl)
        trial = new Trial(
          Time.datetime(2000, January, 1),
          Time.datetime(2016, January, 1),
          fillPriceFn
        )
      } yield simulator.run(trial)

//      val exchange = FindExchange("US")
//      val broker = new ScottradeSim(exchange, 7.0)
//      val portfolio = new Portfolio(10000.0)
//      val account = new BrokerageAccount(broker, portfolio)
//      val appl = dynamicDao.findSecurity("AAPL")
//      val strategy = new BuyAndHoldSingle(account, appl)
//      val trial = new Trial(
//        Time.datetime(2000, January, 1),
//        Time.datetime(2016, January, 1)
//      )
//      simulator.run(trial)
    }
  }

}