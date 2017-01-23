package davidkellis.seven.domain

import java.time.ZonedDateTime

import davidkellis.seven.domain.CoreTypes.{Decimal, IntegerId}

case class EodBar(
                   securityId: IntegerId,
                   date: ZonedDateTime,
                   open: Decimal,
                   high: Decimal,
                   low: Decimal,
                   close: Decimal,
                   volume: Long
                 )
