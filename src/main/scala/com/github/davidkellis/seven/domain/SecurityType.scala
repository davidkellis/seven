package com.github.davidkellis.seven.domain

import com.github.davidkellis.seven.domain.CoreTypes.IntegerId

object SecurityType {
  def unapply(securityType: SecurityType): Option[(IntegerId, String, Option[String])] = {
    Some(
      (
        securityType.id,
        securityType.name,
        securityType.marketSector
      )
    )
  }
}

class SecurityType(val id: IntegerId,
                   val name: String,
                   val marketSector: Option[String]) {

}
