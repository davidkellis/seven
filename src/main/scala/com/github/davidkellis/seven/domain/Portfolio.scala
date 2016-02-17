package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.domain.CoreTypes.{ShareQuantity, IntegerId, Decimal}

case class CumulativePosition(securityId: IntegerId, longQty: ShareQuantity, shortQty: ShareQuantity)

class Portfolio(var cashOnHand: Decimal,
                var positions: Map[IntegerId, CumulativePosition] = Map.empty[IntegerId, CumulativePosition]) {
  def longQty(securityId: IntegerId): ShareQuantity = positions(securityId).longQty
  def shortQty(securityId: IntegerId): ShareQuantity = positions(securityId).shortQty

  def addCash(cash: Decimal): Unit = {
    this.synchronized {
      cashOnHand += cash
    }
  }

  def reduceCash(cash: Decimal): Unit = {
    this.synchronized {
      cashOnHand -= cash
    }
  }

  def addToLongPosition(securityId: IntegerId, qty: ShareQuantity): Unit = {
    this.synchronized {
      positions.get(securityId) match {
        case Some(currentPosition) =>
          positions = positions + (securityId -> CumulativePosition(securityId, currentPosition.longQty + qty, currentPosition.shortQty))
        case None =>
          positions = positions + (securityId -> CumulativePosition(securityId, qty, 0))
      }
    }
  }

  def addToShortPosition(securityId: IntegerId, qty: ShareQuantity): Unit = {
    this.synchronized {
      positions.get(securityId) match {
        case Some(currentPosition) =>
          positions = positions + (securityId -> CumulativePosition(securityId, currentPosition.longQty, currentPosition.shortQty + qty))
        case None =>
          positions = positions + (securityId -> CumulativePosition(securityId, 0, qty))
      }
    }
  }

  def reduceLongPosition(securityId: IntegerId, qty: ShareQuantity): Unit = {
    this.synchronized {
      positions.get(securityId) match {
        case Some(currentPosition) =>
          if (currentPosition.longQty - qty >= 0) {
            positions = positions + (securityId -> CumulativePosition(securityId, currentPosition.longQty - qty, currentPosition.shortQty))
          } else {
            throw new Exception("Unable to reduce long position; the resulting long quantity would be negative.")
          }
        case None =>
          throw new Exception("Unable to reduce a non-existent long position.")
      }
    }
  }

  def reduceShortPosition(securityId: IntegerId, qty: ShareQuantity): Unit = {
    this.synchronized {
      positions.get(securityId) match {
        case Some(currentPosition) =>
          if (currentPosition.shortQty - qty >= 0) {
            positions = positions + (securityId -> CumulativePosition(securityId, currentPosition.longQty, currentPosition.shortQty - qty))
          } else {
            throw new Exception("Unable to reduce short position; the resulting short quantity would be negative.")
          }
        case None =>
          throw new Exception("Unable to reduce a non-existent short position.")
      }
    }
  }
}
