package com.github.davidkellis.seven.data

import com.github.davidkellis.seven.domain.{CoreTypes, Exchange, Security}
import CoreTypes.SecurityId
import com.github.davidkellis.seven.domain.{Exchange, Security}
import org.joda.time.DateTime

trait Dao {
  def findExchanges(exchangeLabels: Seq[String]): Seq[Exchange]
  def findSecurities(exchanges: Seq[Exchange], symbols: Seq[String]): Seq[Security]

  def queryEodBar(securityId: SecurityId, time: DateTime): Option[Bar]
  def queryEodBarPriorTo(time: DateTime, securityId: SecurityId): Option[Bar]
  def queryEodBars(securityId: SecurityId): Seq[Bar]
  def queryEodBars(securityId: SecurityId, earliestTime: DateTime, latestTime: DateTime): Seq[Bar]
  def findOldestEodBar(securityId: SecurityId): Option[Bar]
  def findMostRecentEodBar(securityId: SecurityId): Option[Bar]

  def queryCorporateActions(securityIds: IndexedSeq[Int]): IndexedSeq[CorporateAction]
  def queryCorporateActions(securityIds: IndexedSeq[Int], startTime: DateTime, endTime: DateTime): IndexedSeq[CorporateAction]

  def queryQuarterlyReport(time: DateTime, securityId: SecurityId): Option[QuarterlyReport]
  def queryQuarterlyReportPriorTo(time: DateTime, securityId: SecurityId): Option[QuarterlyReport]
  def queryQuarterlyReports(securityId: SecurityId): Seq[QuarterlyReport]
  def queryQuarterlyReports(securityId: SecurityId, earliestTime: DateTime, latestTime: DateTime): Seq[QuarterlyReport]

  def queryAnnualReport(time: DateTime, securityId: SecurityId): Option[AnnualReport]
  def queryAnnualReportPriorTo(time: DateTime, securityId: SecurityId): Option[AnnualReport]
  def queryAnnualReports(securityId: SecurityId): Seq[AnnualReport]
  def queryAnnualReports(securityId: SecurityId, earliestTime: DateTime, latestTime: DateTime): Seq[AnnualReport]
}

class PostgresDao() extends Dao {

}