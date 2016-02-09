package com.github.davidkellis.seven.domain

import CoreTypes.IntegerId
import com.github.davidkellis.seven.Time.Datestamp

object Security {
  def unapply(security: Security): Option[(IntegerId, IntegerId, Option[IntegerId], Option[IntegerId], Option[IntegerId], String, String, String, String, Option[Int], Option[Datestamp], Option[Datestamp])] = {
    Some(
      (
        security.id,
        security.exchangeId,
        security.securityTypeId,
        security.industryId,
        security.sectorId,
        security.name,
        security.symbol,
        security.figi,
        security.bbgidComposite,
        security.csiNumber,
        security.startDate,
        security.endDate
      )
    )
  }
}

class Security(val id: IntegerId,
               val exchangeId: IntegerId,
               val securityTypeId: Option[IntegerId],
               val industryId: Option[IntegerId],
               val sectorId: Option[IntegerId],
               val name: String,
               val symbol: String,
               val figi: String,
               val bbgidComposite: String,
               val csiNumber: Option[Int],
               val startDate: Option[Datestamp],
               val endDate: Option[Datestamp]) {

}
