package com.github.davidkellis.seven.domain

import CoreTypes.{Decimal, IntegerId}
import org.joda.time.DateTime

case class EodBar(
                   securityId: IntegerId,
                   date: DateTime,
                   open: Decimal,
                   high: Decimal,
                   low: Decimal,
                   close: Decimal,
                   volume: Long
                 )
