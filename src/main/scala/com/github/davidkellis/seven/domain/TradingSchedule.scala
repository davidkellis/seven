package com.github.davidkellis.seven.domain

import java.util.TreeSet

import com.github.davidkellis.seven.SpecialDates.{isAnyHoliday}
import com.github.davidkellis.seven.Time
import com.github.davidkellis.seven.Time._
import com.github.davidkellis.seven.util.CacheBuilder
import net.sf.ehcache.Element
import org.joda.time._

object TradingSchedule {
  val firstTradingDay = date(1950, January, 1)
  val lastTradingDay = date(2050, January, 1)
  val defaultStartOfTrading = new LocalTime(8, 30, 0)   // Eastern Time
  val defaultEndOfTrading = new LocalTime(15, 0, 0)     // Eastern Time
  val defaultDailyTradingHours = (defaultStartOfTrading, defaultEndOfTrading)

  val defaultWeeklyTradingHours: Map[DayOfWeek, (LocalTime, LocalTime)] = Map(
    Monday -> defaultDailyTradingHours,
    Tuesday -> defaultDailyTradingHours,
    Wednesday -> defaultDailyTradingHours,
    Thursday -> defaultDailyTradingHours,
    Friday -> defaultDailyTradingHours
  )

  def defaultTradingSchedule(date: LocalDate): MInterval = {
    val tradingHours = defaultWeeklyTradingHours.get(dayOfWeek(date))
    tradingHours.map { tradingHours =>
      val startOfTrading = tradingHours._1
      val endOfTrading = tradingHours._2
      new MInterval(
        intervalBetween(
          date.toDateTime(startOfTrading, EasternTimeZone),
          date.toDateTime(endOfTrading, EasternTimeZone)
        )
      )
    }.getOrElse(MInterval.empty)
  }

  // returns an MInterval spanning the time of the holiday - this MInterval represents the time we take off for the holiday
  def defaultHolidaySchedule(date: LocalDate): MInterval = {
    if (isAnyHoliday(date))
      defaultTradingSchedule(date)
    else
      MInterval.empty
  }

  type TradingSchedule = (LocalDate) => MInterval

  def buildTradingSchedule(normalTradingSchedule: TradingSchedule, holidaySchedule: TradingSchedule): TradingSchedule =
    (date: LocalDate) => {
      val tradingHours = normalTradingSchedule(date)
      val holidayHours = holidaySchedule(date)
      if (holidayHours.overlaps(tradingHours))
        tradingHours - holidayHours
      else
        tradingHours
    }

  // returns true if the trading-schedule has any trading hours scheduled for that date; false otherwise.
  def isTradingDay(date: LocalDate, tradingSchedule: TradingSchedule): Boolean = !tradingSchedule(date).isEmpty

  val allTradingDaysCache = CacheBuilder.buildLruCache(2, "allTradingDays")

  // returns a SortedSet[LocalDate] of all the trading days in the timespan that we're interested in: 1950 - 2050.
  def allTradingDays(tradingSchedule: TradingSchedule): TreeSet[LocalDate] = {
    val cachedTradingDaysSet = Option(allTradingDaysCache.get(tradingSchedule))
    cachedTradingDaysSet match {
      case Some(cachedTradingDaysSetElement) => cachedTradingDaysSetElement.getObjectValue.asInstanceOf[TreeSet[LocalDate]]
      case None =>
        val newTradingDaysSet: TreeSet[LocalDate] = new TreeSet[LocalDate](Time.localDateOrdering)
        tradingDays(firstTradingDay, lastTradingDay, tradingSchedule).foreach(tradingDay => newTradingDaysSet.add(tradingDay))
        allTradingDaysCache.put(new Element(tradingSchedule, newTradingDaysSet))
        newTradingDaysSet
    }
  }

  def tradingDays(startDate: LocalDate, endDate: LocalDate, tradingSchedule: TradingSchedule): Seq[LocalDate] =
    tradingDays(startDate, tradingSchedule).takeWhile(isBeforeOrEqual(_, endDate))

  def tradingDays(startDate: LocalDate, tradingSchedule: TradingSchedule): Seq[LocalDate] =
    infPeriodicalDateSeries(startDate, Days.days(1)).filter(isTradingDay(_, tradingSchedule))

  def nextTradingDay(date: LocalDate, timeIncrement: ReadablePeriod, tradingSchedule: TradingSchedule): LocalDate = {
    val tradingDaysSortedSet = allTradingDays(tradingSchedule)
    val nextDay = date.plus(timeIncrement)
    if (tradingDaysSortedSet.contains(nextDay))
      nextDay
    else {
      Option(tradingDaysSortedSet.higher(nextDay)).getOrElse(tradingDays(nextDay, tradingSchedule).head)
    }
  }
}