package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.domain.CoreTypes.{ShareQuantity, IntegerId, Decimal}

case class CumulativePosition(security: Security, longQty: ShareQuantity, shortQty: ShareQuantity)

class Portfolio(var cashOnHand: Decimal,
                var positions: Map[IntegerId, CumulativePosition] = Map.empty[IntegerId, CumulativePosition]) {
  def longQty(securityId: IntegerId): ShareQuantity = positions(securityId).longQty
  def shortQty(securityId: IntegerId): ShareQuantity = positions(securityId).shortQty
}
