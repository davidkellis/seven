package com.github.davidkellis.seven.domain

import CoreTypes.{Decimal}
import org.joda.time.DateTime

case class Trial(
                  startTime: DateTime,
                  endTime: DateTime,

                  schedule: Schedule,
                  purchaseFillPrice: PriceQuoteFn,
                  saleFillPrice: PriceQuoteFn
                )
