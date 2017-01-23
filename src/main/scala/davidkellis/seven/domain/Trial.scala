package davidkellis.seven.domain;

import java.time.{LocalTime, ZonedDateTime}

import davidkellis.seven.domain.CoreTypes.FillPriceFn

case class Trial(
                  startTime: ZonedDateTime,
                  endTime: ZonedDateTime,

                  dailyTradingTimes: Seq[LocalTime],
                  fillPriceFn: FillPriceFn
                )
