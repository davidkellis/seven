package com.github.davidkellis.seven.domain

object Industry {
  def unapply(industry: Industry): Option[(Long, String)] = {
    Some(
      (
        industry.id,
        industry.name
      )
    )
  }
}

class Industry(val id: Long,
               val name: String) {
}
