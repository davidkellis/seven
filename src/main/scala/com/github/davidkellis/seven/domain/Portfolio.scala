package com.github.davidkellis.seven.domain

import CoreTypes.{SecurityId, Decimal}

case class CumulativePosition(security: Security, longQty: Long, shortQty: Long)

class Portfolio(cashOnHand: Decimal,
                positions: Map[SecurityId, CumulativePosition] = Map.empty[SecurityId, CumulativePosition]) {

}
