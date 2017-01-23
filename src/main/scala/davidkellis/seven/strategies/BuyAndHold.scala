package davidkellis.seven.strategies

import java.time.{Period, ZonedDateTime}
import java.time.Month._

import davidkellis.seven.Time.{PeriodDuration,datetime}
import davidkellis.seven.{MathUtil, Time}
import davidkellis.seven.domain.CoreTypes.{FillPriceFn, ShareQuantity, TradingEvent}
import davidkellis.seven.domain._

object BuyAndHold {

  trait CurrentState
  case object NotInvested extends CurrentState
  case object Invested extends CurrentState
  case object ExitedPosition extends CurrentState

  class BuyAndHoldSingleStock(quotationService: QuotationService, account: BrokerageAccount, security: Security, holdTime: Period) extends Strategy {
    var currentState: CurrentState = NotInvested
    var entryTime: Option[ZonedDateTime] = None[ZonedDateTime]
    var exitTime: Option[ZonedDateTime] = None[ZonedDateTime]

    def evaluate(previousTime: ZonedDateTime, currentTime: ZonedDateTime, event: TradingEvent): Unit = {
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

    private def securityHasBeenHeldForLongEnough(currentTime: ZonedDateTime, entryTime: ZonedDateTime, holdTime: Period): Boolean = {
      Time.isAfterOrEqual(currentTime, entryTime.plus(holdTime))
    }

    private def maxSharesAccountCanAfford(account: BrokerageAccount, quotationService: QuotationService, security: Security, time: ZonedDateTime): Option[ShareQuantity] = {
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
      val fillPriceFn: FillPriceFn = (order: Order, currentTime: ZonedDateTime) => order.action match {
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
        strategy = new BuyAndHoldSingleStock(simpleQuotationService, account, appl, PeriodDuration.ofYears(1))
        trial = new Trial(
          datetime(2015, JANUARY, 1),
          datetime(2016, JANUARY, 1),
          Array(Time.localtime(12, 0, 0)),
          fillPriceFn
        )
      } yield simulator.run(Seq(exchange), Seq(account), strategy, trial)
    }
  }

}