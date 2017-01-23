package davidkellis.seven.domain

object Sector {
  def unapply(sector: Sector): Option[(Long, String)] = {
    Some(
      (
        sector.id,
        sector.name
      )
    )
  }
}

class Sector(val id: Long,
             val name: String) {
}
