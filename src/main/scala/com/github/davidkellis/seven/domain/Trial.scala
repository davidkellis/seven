package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.domain.CoreTypes.{FillPriceFn, Decimal}
import org.joda.time.DateTime

case class Trial(
                  startTime: DateTime,
                  endTime: DateTime,

                  fillPriceFn: FillPriceFn
                )
