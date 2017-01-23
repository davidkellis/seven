package davidkellis.seven.domain

import davidkellis.seven.domain.CoreTypes.{Decimal, ShareQuantity, FillPriceFn, IntegerId}
import java.time.ZonedDateTime

import scala.collection.mutable
import scala.util.{Success, Try}

object Exchange {
  def unapply(exchange: Exchange): Option[(Long, String, String, Boolean, Option[Long])] = {
    Some(
      (
        exchange.id,
        exchange.label,
        exchange.name,
        exchange.isCompositeExchange,
        exchange.compositeExchangeId
      )
    )
  }
}
class Exchange(val id: Long,
               val label: String,
               val name: String,
               val isCompositeExchange: Boolean,
               val compositeExchangeId: Option[Long]) {
  val orderQueue = mutable.ListBuffer.empty[Order]
  val orderCancellations = mutable.Set.empty[Order]
  var ordersWaitingToBeFilled = mutable.Set.empty[Order]

  def placeOrder(order: Order): Try[Unit] = {
    this.synchronized {
      orderQueue += order
    }
    Success()
  }

  def cancelOrder(order: Order): Try[Unit] = {
    this.synchronized {
      orderCancellations += order
    }
    Success()
  }

  def fillOrders(fillPriceFn: FillPriceFn, previousTime: ZonedDateTime, currentTime: ZonedDateTime): Try[Unit] = {
    // 1. pull the orders out of the two queues and clear the queues
    val (queuedOrders, ordersToCancel) = this.synchronized {
      val orderQueueClone = orderQueue.clone
      val orderCancellationsClone = orderCancellations.clone
      orderQueue.clear
      orderCancellations.clear
      (orderQueueClone, orderCancellationsClone)
    }

    // 2. cancel orders
    val newOrders = queuedOrders.filterNot(ordersToCancel.contains(_))
    this.synchronized {
      ordersWaitingToBeFilled --= ordersToCancel
    }

    // 3. fill new orders or put them in the consolidated order book
    val (filledOrders, unfilledOrders) = newOrders.partition(tryFill(fillPriceFn, _, currentTime))
    this.synchronized {
      ordersWaitingToBeFilled ++= unfilledOrders
    }

    Success()
  }

  private def tryFill(fillPriceFn: FillPriceFn, order: Order, currentTime: ZonedDateTime): Boolean = {
    if (isOrderFillable(fillPriceFn, order, currentTime)) {       // if the order is fillable, then fill it, and continue
      val fillPrice = fillPriceFn(order, currentTime).get         // isOrderFillable implies that this expression returns a Decimal

      Order.markFilled(order, fillPrice, order.account.broker.costOfTransactionFees(order), currentTime)

      order.account.broker.notifyOrderFilled(order)

      true
    } else {
      false
    }
  }

  private def isOrderFillable(fillPriceFn: FillPriceFn, order: Order, currentTime: ZonedDateTime): Boolean = {
    order match {
      case Order(_, _, Open, _, Market, Buy, _, securityId, _, _, _, _, _, _, _, _, _, _) =>
        isMarketBuyFillable(fillPriceFn, order, currentTime)
      case Order(_, _, Open, _, Market, Sell, _, securityId, _, _, _, _, _, _, _, _, _, _) =>
        isMarketSellFillable(fillPriceFn, order, currentTime)
      case Order(_, _, Open, _, Limit, Buy, _, securityId, _, _, _, _, _, _, _, Some(limitPrice), _, _) =>
        isLimitBuyFillable(fillPriceFn, order, currentTime)
      case Order(_, _, Open, _, Limit, Sell, _, securityId, _, _, _, _, _, _, _, Some(limitPrice), _, _) =>
        isLimitSellFillable(fillPriceFn, order, currentTime)
      case _ => throw new Exception("Unknown order type. Unable to determine whether order is fillable or not.")
    }
  }

  private def isMarketBuyFillable(fillPriceFn: FillPriceFn, order: Order, currentTime: ZonedDateTime): Boolean = {
    val costOpt = Order.purchaseCost(order.account.broker, fillPriceFn, order, currentTime)

    // todo: I think this requirement should be removed because http://www.21stcenturyinvestoreducation.com/page/tce/courses/course-101/005/001-cash-vs-margin.html
    //       says even cash accounts can temporarily have a negative cash balance as long as the necessary funds are deposited within 3 business days after
    //       the purchase.
    // todo: this condition should be that the post-purchase cash balance should be within reasonable margin requirements.
    costOpt.map(_ <= order.account.portfolio.cashOnHand).getOrElse(false)    // this condition is only applicable to cash-only accounts; this is allowed in margin accounts
  }

  private def isMarketSellFillable(fillPriceFn: FillPriceFn, order: Order, currentTime: ZonedDateTime): Boolean = {
    val proceedsOpt = Order.saleProceeds(order.account.broker, fillPriceFn, order, currentTime)
    proceedsOpt.map(_ >= 0.0).getOrElse(false)
  }

  private def isLimitBuyFillable(fillPriceFn: FillPriceFn, order: Order, currentTime: ZonedDateTime): Boolean = {
    val fillPrice = fillPriceFn(order, currentTime)
    fillPrice.map(_ <= order.limitPrice.get).getOrElse(false) && isMarketBuyFillable(fillPriceFn, order, currentTime)
  }

  private def isLimitSellFillable(fillPriceFn: FillPriceFn, order: Order, currentTime: ZonedDateTime): Boolean = {
    val fillPrice = fillPriceFn(order, currentTime)
    fillPrice.map(_ >= order.limitPrice.get).getOrElse(false) && isMarketSellFillable(fillPriceFn, order, currentTime)
  }
}