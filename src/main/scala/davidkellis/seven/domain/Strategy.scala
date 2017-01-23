package davidkellis.seven.domain

import davidkellis.seven.domain.CoreTypes.TradingEvent
import java.time.ZonedDateTime

trait Strategy {
  def evaluate(previousTime: ZonedDateTime, currentTime: ZonedDateTime, event: TradingEvent): Unit
}
