package com.github.davidkellis.seven.domain

import CoreTypes.{IntegerId, Decimal}

case class CumulativePosition(security: Security, longQty: Long, shortQty: Long)

class Portfolio(cashOnHand: Decimal,
                positions: Map[IntegerId, CumulativePosition] = Map.empty[IntegerId, CumulativePosition]) {

}
