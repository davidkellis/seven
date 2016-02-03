package com.github.davidkellis.seven.domain

import CoreTypes.{Decimal, SecurityId}
import org.joda.time.DateTime

case class EodBar(
                   securityId: SecurityId,
                   date: DateTime,
                   open: Decimal,
                   high: Decimal,
                   low: Decimal,
                   close: Decimal,
                   volume: Long
                 )
