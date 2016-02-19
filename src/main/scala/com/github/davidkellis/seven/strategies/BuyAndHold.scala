package com.github.davidkellis.seven.strategies

import com.github.davidkellis.seven.{MathUtil, Time}
import com.github.davidkellis.seven.Time.January
import com.github.davidkellis.seven.data.Dao
import com.github.davidkellis.seven.domain.CoreTypes.{FillPriceFn, ShareQuantity, TradingEvent}
import com.github.davidkellis.seven.domain._
import org.joda.time.{Period, DateTime}

import Dao.dao

object BuyAndHold {

  trait CurrentState
  case object NotInvested extends CurrentState
  case object Invested extends CurrentState
  case object ExitedPosition extends CurrentState

  class BuyAndHoldSingleStock(quotationService: QuotationService, account: BrokerageAccount, security: Security, holdTime: Period) extends Strategy {
    var currentState: CurrentState = NotInvested
    var entryTime: Option[DateTime] = None[DateTime]
    var exitTime: Option[DateTime] = None[DateTime]

    def evaluate(previousTime: DateTime, currentTime: DateTime, event: TradingEvent): Unit = {
      currentState match {
        case NotInvested =>
          val qtyOpt = maxSharesAccountCanAfford(account, quotationService, security, currentTime)
          qtyOpt.foreach { qty =>
            account.marketBuyStock(security, qty, currentTime)
            entryTime = Some(currentTime)
            currentState = NotInvested
          }
        case Invested =>
          if (securityHasBeenHeldForLongEnough(currentTime, entryTime.get, holdTime)) {
            account.marketSellStock(security, account.portfolio.longQty(security.id), currentTime)
            exitTime = Some(currentTime)
            currentState = ExitedPosition
          }
      }
    }

    private def securityHasBeenHeldForLongEnough(currentTime: DateTime, entryTime: DateTime, holdTime: Period): Boolean = {
      Time.isAfterOrEqual(currentTime, entryTime.plus(holdTime))
    }

    private def maxSharesAccountCanAfford(account: BrokerageAccount, quotationService: QuotationService, security: Security, time: DateTime): Option[ShareQuantity] = {
      for {
        sharePrice <- quotationService.market(Buy, security, time)
        imaginaryMarketBuyOrder = Order.buildMarketBuy(account, Equity, security.id, MathUtil.intFloor(account.portfolio.cashOnHand / sharePrice), time)
        cashAfterTransactionFees = account.portfolio.cashOnHand - account.broker.costOfTransactionFees(imaginaryMarketBuyOrder)
      } yield MathUtil.intFloor(cashAfterTransactionFees / sharePrice)
    }
  }

  object Scenarios {
    def runSingleBuyAndHoldTrial(): Unit = {
      val simulator = new Simulator()
      val fillPriceFn: FillPriceFn = (order: Order, currentTime: DateTime) => order.action match {
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
        simpleQuotationService = new SimpleQuotationService()
        strategy = new BuyAndHoldSingleStock(simpleQuotationService, account, appl, Time.years(1))
        trial = new Trial(
          Time.datetime(2015, January, 1),
          Time.datetime(2016, January, 1),
          Array(Time.localtime(12, 0, 0)),
          fillPriceFn
        )
      } yield simulator.run(Seq(exchange), Seq(account), strategy, trial)
    }
  }

}