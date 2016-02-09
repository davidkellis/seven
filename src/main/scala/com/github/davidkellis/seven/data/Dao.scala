package com.github.davidkellis.seven.data

import com.github.davidkellis.seven.domain._
import CoreTypes.IntegerId
import org.joda.time.DateTime
import scalikejdbc._

import scala.util.DynamicVariable

object Dao {
  private val dynVar = new DynamicVariable[Dao](null)

  /**
   * Run the supplied thunk with a new Dao and automatically close the session at the end.
   * The Dao is stored in a thread-local variable which can be accessed with the implicit
   * function dynamicAdapter.
   */
  def withDao[T](dao: Dao)(f: => T): T = dynVar.withValue(dao)(f)

  /**
   * An implicit function that returns the thread-local adapter in a withAdapater block
   */
  implicit def dynamicDao: Dao = {
    val dao = dynVar.value
    if (dao eq null)
      throw new Exception("No implicit dao has been set; dynamicDao can only be used within a withDao block")
    else dao
  }
}

trait Dao {
  def readOnly[T](f: => T): T

  def findExchanges(exchangeLabels: Seq[String]): Seq[Exchange]
  def findSecurities(symbols: Seq[String], exchanges: Seq[Exchange]): Seq[Security]

//  def findEodBars(securityId: IntegerId, earliestTime: DateTime, latestTime: DateTime): Seq[EodBar]
//  def findMostRecentEodBar(securityId: IntegerId, time: DateTime): Option[EodBar]
//  def findMostRecentEodBarPriorTo(securityId: IntegerId, time: DateTime): Option[EodBar]
}

class PostgresDao() extends Dao {
  def readOnly[T](f: => T): T = {
    DB readOnly { implicit session =>
      f
    }
  }

  def findExchanges(exchangeLabels: Seq[String])(implicit session: DBSession = AutoSession): Seq[Exchange] = {
    sql"select * from exchanges where label in (${exchangeLabels})".
      map(toExchange(_)).list.apply()
  }

  def findSecurities(symbols: Seq[String], exchanges: Seq[Exchange])(implicit session: DBSession = AutoSession): Seq[Security] = {
    val exchangeIds = exchanges.map(_.id)
    sql"select * from securities where symbol in (${symbols}) and exchange_id in (${exchangeIds})".
      map(toSecurity(_)).list.apply()
  }

  def toExchange(rs: WrappedResultSet): Exchange = {
    new Exchange()
  }

  def toSecurity(rs: WrappedResultSet): Security = {
    new Security()
  }
}