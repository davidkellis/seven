package com.github.davidkellis.seven

import org.joda.time._
import org.joda.time.format.{ISOPeriodFormat}

import scala.util.Random

object Time {
  implicit def dateTimeOrdering: Ordering[DateTime] = Ordering.fromLessThan(isBefore(_, _))
  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.fromLessThan[LocalDate](_.isBefore(_))

  type Timestamp = Long
  type Datestamp = Int

  sealed trait DayOfWeek
  case object Monday extends DayOfWeek
  case object Tuesday extends DayOfWeek
  case object Wednesday extends DayOfWeek
  case object Thursday extends DayOfWeek
  case object Friday extends DayOfWeek
  case object Saturday extends DayOfWeek
  case object Sunday extends DayOfWeek

  def dayOfWeek(dayOfWeekInt: Int): DayOfWeek = dayOfWeekInt match {
    case 1 => Monday
    case 2 => Tuesday
    case 3 => Wednesday
    case 4 => Thursday
    case 5 => Friday
    case 6 => Saturday
    case 7 => Sunday
    case _ => throw new Exception("Day of week must be in range 1-7 inclusive.")
  }

  def dayOfWeek(date: LocalDate): DayOfWeek = dayOfWeek(intDayOfWeek(date))
  def dayOfWeek(time: DateTime): DayOfWeek = dayOfWeek(intDayOfWeek(time))

  def isDateMonday(date: LocalDate) = dayOfWeek(date) == Monday
  def isDateTuesday(date: LocalDate) = dayOfWeek(date) == Tuesday
  def isDateWednesday(date: LocalDate) = dayOfWeek(date) == Wednesday
  def isDateThursday(date: LocalDate) = dayOfWeek(date) == Thursday
  def isDateFriday(date: LocalDate) = dayOfWeek(date) == Friday
  def isDateSaturday(date: LocalDate) = dayOfWeek(date) == Saturday
  def isDateSunday(date: LocalDate) = dayOfWeek(date) == Sunday


  def intDayOfWeek(dayOfWeek: DayOfWeek): Int = dayOfWeek match {
    case Monday => 1
    case Tuesday => 2
    case Wednesday => 3
    case Thursday => 4
    case Friday => 5
    case Saturday => 6
    case Sunday => 7
  }
  def intDayOfWeek(t: DateTime): Int = t.getDayOfWeek()
  def intDayOfWeek(t: LocalDate): Int = t.getDayOfWeek()

  sealed trait Month
  case object January extends Month
  case object February extends Month
  case object March extends Month
  case object April extends Month
  case object May extends Month
  case object June extends Month
  case object July extends Month
  case object August extends Month
  case object September extends Month
  case object October extends Month
  case object November extends Month
  case object December extends Month

  def intMonth(month: Month): Int = month match {
    case January => 1
    case February => 2
    case March => 3
    case April => 4
    case May => 5
    case June => 6
    case July => 7
    case August => 8
    case September => 9
    case October => 10
    case November => 11
    case December => 12
  }

  def findTimeZone(timeZoneName: String): DateTimeZone = DateTimeZone.forID(timeZoneName)

  val EasternTimeZone = findTimeZone("US/Eastern")
//  val CentralTimeZone = findTimeZone("US/Central")
//  val PacificTimeZone = findTimeZone("US/Pacific")

  def currentTime(timeZone: DateTimeZone = EasternTimeZone): DateTime = DateTime.now(timeZone)

  def date(time: DateTime): LocalDate = new LocalDate(time.getYear, time.getMonthOfYear, time.getDayOfMonth)
  def date(timestamp: Timestamp): LocalDate = date(datetime(timestamp))
  def date(year: Int): LocalDate = new LocalDate(year, 1, 1)
//  def date(year: Int, month: Int): LocalDate = new LocalDate(year, month, 1)
  def date(year: Int, month: Int, day: Int): LocalDate = new LocalDate(year, month, day)
  def date(year: Int, month: Month, day: Int): LocalDate = new LocalDate(year, intMonth(month), day)

  def datestamp(date: LocalDate): Datestamp = date.toString("yyyyMMdd").toInt
  def datestamp(time: Timestamp): Datestamp = time.toString.substring(0, 8).toInt
  def datestamp(time: DateTime): Datestamp = time.toString("yyyyMMdd").toInt

  def datetime(year: Int, month: Int, day: Int): DateTime = datetime(year, month, day, 0, 0, 0)
  def datetime(year: Int, month: Month, day: Int): DateTime = datetime(year, intMonth(month), day, 0, 0, 0)
  def datetime(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): DateTime =
    new DateTime(year, month, day, hour, minute, second, EasternTimeZone)
  def datetime(year: Int, month: Month, day: Int, hour: Int, minute: Int, second: Int): DateTime =
    new DateTime(year, intMonth(month), day, hour, minute, second, EasternTimeZone)
  def datetime(date: LocalDate): DateTime = datetime(date.getYear, date.getMonthOfYear, date.getDayOfMonth)
  def datetime(date: LocalDate, hour: Int, minute: Int, second: Int): DateTime = datetime(date.getYear, date.getMonthOfYear, date.getDayOfMonth).withTime(hour, minute, second, 0)
  def datetime(datestamp: Datestamp): DateTime = {
    val ds = datestamp.toString
    val year = ds.substring(0, 4).toInt
    val month = ds.substring(4, 6).toInt
    val day = ds.substring(6, 8).toInt
    datetime(year, month, day)
  }
  def datetime(timestamp: Timestamp): DateTime = {
    val ts = timestamp.toString
    val year = ts.substring(0, 4).toInt
    val month = ts.substring(4, 6).toInt
    val day = ts.substring(6, 8).toInt
    val hour = ts.substring(8, 10).toInt
    val minute = ts.substring(10, 12).toInt
    val second = ts.substring(12, 14).toInt
    datetime(year, month, day, hour, minute, second)
  }

  def timestamp(date: LocalDate): Timestamp = datetime(date).toString("yyyyMMddHHmmss").toLong
  def timestamp(datetime: ReadableDateTime): Timestamp = datetime.toString("yyyyMMddHHmmss").toLong

  def midnight(date: LocalDate): DateTime = datetime(date).withTimeAtStartOfDay()
  def midnight(datetime: DateTime): DateTime = datetime.withTimeAtStartOfDay()

  def years(n: Int): Period = Years.years(n).toPeriod
  def months(n: Int): Period = Months.months(n).toPeriod
  def weeks(n: Int): Period = Weeks.weeks(n).toPeriod
  def days(n: Int): Period = Days.days(n).toPeriod
  def hours(n: Int): Period = Hours.hours(n).toPeriod
  def minutes(n: Int): Period = Minutes.minutes(n).toPeriod
  def seconds(n: Int): Period = Seconds.seconds(n).toPeriod
  def millis(n: Long): Period = new Period(n)

  def periodBetween(i1: ReadableInstant, i2: ReadableInstant): Period = new Period(i1, i2)
  def periodBetween(t1: ReadablePartial, t2: ReadablePartial): Period = new Period(t1, t2)

  def durationBetween(t1: DateTime, t2: DateTime): Duration = new Duration(t1, t2)

  def intervalBetween(t1: DateTime, t2: DateTime): Interval = new Interval(t1, t2)

  def formatPeriod(period: Period): String = period.toString(ISOPeriodFormat.standard)
  def parsePeriod(periodString: String): Period = Period.parse(periodString, ISOPeriodFormat.standard)

  // t1 <= instant < t2
  def isInstantBetween(instant: DateTime, t1: DateTime, t2: DateTime): Boolean = intervalBetween(t1, t2).contains(instant)

  // t1 <= instant <= t2
  def isInstantBetweenInclusive(instant: DateTime, t1: DateTime, t2: DateTime): Boolean = !isBefore(instant, t1) && !isAfter(instant, t2)

  def maxDateTime(t1: DateTime, t2: DateTime): DateTime = if (t1.isAfter(t2)) t1 else t2

  def minDateTime(t1: DateTime, t2: DateTime): DateTime = if (t1.isBefore(t2)) t1 else t2

  // Returns a random datetimeUtils between t1 (inclusive) and t2 (exclusive)
  def randomDateTime(t1: DateTime, t2: DateTime): DateTime = {
    val r = new Random().nextDouble()
    val duration = durationBetween(t1, t2)
    val millisecondOffset = (r * duration.getMillis()).toLong
    t1.plus(millisecondOffset)
  }

  def isIntervalEmpty(interval: Interval): Boolean = {
    interval.getStart() == interval.getEnd()
  }

  // t1 < t2
  def isBefore(t1: DateTime, t2: DateTime): Boolean = t1.isBefore(t2)

  // t1 > t2
  def isAfter(t1: DateTime, t2: DateTime): Boolean = t1.isAfter(t2)

  def compareDateTimes(t1: DateTime, t2: DateTime): Int = t1.compareTo(t2)
  def compareDateTimes(d1: LocalDate, d2: LocalDate): Int = d1.compareTo(d2)

  def isBeforeOrEqual(t1: DateTime, t2: DateTime): Boolean = compareDateTimes(t1, t2) <= 0
  def isBeforeOrEqual(d1: LocalDate, d2: LocalDate): Boolean = compareDateTimes(d1, d2) <= 0

  def isAfterOrEqual(t1: DateTime, t2: DateTime): Boolean = compareDateTimes(t1, t2) >= 0
  def isAfterOrEqual(d1: LocalDate, d2: LocalDate): Boolean = compareDateTimes(d1, d2) >= 0

  sealed trait TimeDirection
  case object Before extends TimeDirection
  case object After extends TimeDirection
  def offsetDateTime(t: DateTime, direction: TimeDirection, magnitude: Period): DateTime = direction match {
    case Before => t.minus(magnitude)
    case After => t.plus(magnitude)
    case _ => t
  }

  def offsetInterval(interval: Interval,
                     startOffsetDirection: TimeDirection,
                     startOffsetMagnitude: Period,
                     endOffsetDirection: TimeDirection,
                     endOffsetMagnitude: Period): Interval = {
    val adjustedStart = offsetDateTime(interval.getStart, startOffsetDirection, startOffsetMagnitude)
    val adjustedEnd = offsetDateTime(interval.getEnd, endOffsetDirection, endOffsetMagnitude)
    intervalBetween(adjustedStart, adjustedEnd)
  }

  // returns an infinite Stream of [t f(t) f(f(t)) f(f(f(t))) ...]
  def timeSeries(startTime: DateTime, nextTimeFn: (DateTime) => DateTime): Stream[DateTime] = Stream.iterate(startTime)(nextTimeFn)

  // returns an infinite Stream of [d f(d) f(f(d)) f(f(f(d))) ...]
  def dateSeries(startDate: LocalDate, nextDateFn: (LocalDate) => LocalDate): Stream[LocalDate] = Stream.iterate(startDate)(nextDateFn)

  // returns an infinite Stream of [t t+p t+2p t+3p ...]
  def infPeriodicalTimeSeries(startTime: DateTime, period: ReadablePeriod): Stream[DateTime] = timeSeries(startTime, (t: DateTime) => t.plus(period))

  // returns an infinite Stream of [d d+p d+2p d+3p ...]
  def infPeriodicalTimeSeries(startDate: LocalDate, period: ReadablePeriod): Stream[LocalDate] = dateSeries(startDate, (d: LocalDate) => d.plus(period))

  // returns a sequence of DateTimes, [t1, t2, ..., tN], that are separated by a given Period, s.t. startTime = t1 and tN <= endTime
  def periodicalTimeSeries(startTime: DateTime, endTime: DateTime, period: ReadablePeriod): Stream[DateTime] =
    infPeriodicalTimeSeries(startTime, period).takeWhile(isBeforeOrEqual(_, endTime))

  // returns a sequence of LocalDate, [t1, t2, ..., tN], that are separated by a given Period, s.t. startTime = t1 and tN <= endTime
  def periodicalDateSeries(startDate: LocalDate, endDate: LocalDate, period: ReadablePeriod): Stream[LocalDate] =
    infPeriodicalTimeSeries(startDate, period).takeWhile(isBeforeOrEqual(_, endDate))

  // returns an infinite sequence
  // returns a sequence of Intervals, [i1, i2, ..., iN], s.t. the start time of subsequent intervals is separated
  // by a given Period, <separationLength> and each interval spans an amount of time given by <intervalLength>
  def infInterspersedIntervals(startTime: DateTime, intervalLength: Period, separationLength: Period): Stream[Interval] = {
    val startTimes = infPeriodicalTimeSeries(startTime, separationLength)
    startTimes.map(t => intervalBetween(t, t.plus(intervalLength)))
  }

  def interspersedIntervals2(firstIntervalStartTime: DateTime,
                             lastIntervalStartTime: DateTime,
                             intervalLength: Period,
                             separationLength: Period): Stream[Interval] = {
    val startTimes = infPeriodicalTimeSeries(firstIntervalStartTime, separationLength).takeWhile(isBeforeOrEqual(_, lastIntervalStartTime))
    startTimes.map(t => intervalBetween(t, t.plus(intervalLength)))
  }

  def interspersedIntervals(startTimeInterval: Interval, intervalLength: Period, separationLength: Period): Stream[Interval] =
    interspersedIntervals2(startTimeInterval.getStart, startTimeInterval.getEnd, intervalLength, separationLength)

  def daysInMonth(month: Int, year: Int): Int = datetime(year, month, 1).dayOfMonth().getMaximumValue()
  def daysInMonth(month: Month, year: Int): Int = datetime(year, month, 1).dayOfMonth().getMaximumValue()

  // returns (intMonth, intYear) representing the month and year following the given month and year
  def nextMonth(month: Int, year: Int): (Int, Int) = {
    if (month == 12)
      (1, year + 1)
    else
      (month + 1, year)
  }

  // returns (intMonth, intYear) representing the month and year preceeding the given month and year
  def previousMonth(month: Int, year: Int): (Int, Int) = {
    if (month == 1)
      (12, year - 1)
    else
      (month - 1, year)
  }

  def addMonths(baseMonth: Int, baseYear: Int, monthOffset: Int): (Int, Int) = {
    if (monthOffset >= 0) {
      1.to(monthOffset).foldLeft((baseMonth, baseYear)) {(monthYearPair, i) => nextMonth(monthYearPair._1, monthYearPair._2) }
    } else {
      1.to(-monthOffset).foldLeft((baseMonth, baseYear)) {(monthYearPair, i) => previousMonth(monthYearPair._1, monthYearPair._2) }
    }
  }

  def firstDayOfMonth(year: Int, month: Month): LocalDate = date(year, month, 1)
  def firstDayOfMonth(year: Int, month: Int): LocalDate = date(year, month, 1)
  def lastDayOfMonth(year: Int, month: Month): LocalDate = date(year, month, daysInMonth(month, year))
  def lastDayOfMonth(year: Int, month: Int): LocalDate = date(year, month, daysInMonth(year, month))

  def nextDay(date: LocalDate): LocalDate = date.plusDays(1)
  def previousDay(date: LocalDate): LocalDate = date.minusDays(1)

  /**
    * returns the number of days that must be added to the first day of the given month to arrive at the first
    * occurrence of the <desired-weekday> in that month; put another way, it returns the number of days
    * that must be added to the first day of the given month to arrive at the <desired-weekday> in the first
    * week of that month.
    * The return value will be an integer in the range [0, 6].
    * NOTE: the return value is the result of the following expression:
    * (desired-weekday - dayOfWeek(year, month, 1) + 7) mod 7
    * desired-weekday is an integer indicating the desired day of the week, s.t. 1=Monday, 2=Tue., ..., 6=Sat., 7=Sun.
    * month is an integer indicating the month, s.t. 1=Jan., 2=Feb., ..., 11=Nov., 12=Dec.
    * year is an integer indicating the year (e.g. 1999, 2010, 2012, etc.)
    * Example:
    * offsetOfFirstWeekdayInMonth(Monday, 2, 2012)    ; monday
    * > 5
    * offsetOfFirstWeekdayInMonth(Wednesday, 2, 2012)    ; wednesday
    * > 0
    * offsetOfFirstWeekdayInMonth(Friday, 2, 2012)    ; friday
    * > 2
    */
  def offsetOfFirstWeekdayInMonth(desiredWeekday: DayOfWeek, month: Int, year: Int): Int =
    (intDayOfWeek(desiredWeekday) - intDayOfWeek(datetime(year, month, 1)) + 7) % 7

  /**
    * The return value will be an integer in the range [0, 6].
    * desired_weekday is an integer indicating the desired day of the week, s.t. 1=Monday, 2=Tue., ..., 6=Sat., 7=Sun.
    * current_weekday is an integer indicating the desired day of the week, s.t. 1=Monday, 2=Tue., ..., 6=Sat., 7=Sun.
    */
  def offsetOfFirstWeekdayAtOrAfterWeekday(desiredWeekday: DayOfWeek, currentWeekday: DayOfWeek): Int =
    (intDayOfWeek(desiredWeekday) - intDayOfWeek(currentWeekday) + 7) % 7

  /**
    * The return value will be an integer in the range [-6, 0].
    * desired_weekday is an integer indicating the desired day of the week, s.t. 1=Monday, 2=Tue., ..., 6=Sat., 7=Sun.
    * current_weekday is an integer indicating the desired day of the week, s.t. 1=Monday, 2=Tue., ..., 6=Sat., 7=Sun.
    */
  def offsetOfFirstWeekdayAtOrBeforeWeekday(desiredWeekday: DayOfWeek, currentWeekday: DayOfWeek): Int =
    -((intDayOfWeek(currentWeekday) - intDayOfWeek(desiredWeekday) + 7) % 7)

  /**
    * The return value will be an integer in the range [1, 7].
    * desired_weekday is an integer indicating the desired day of the week, s.t. 1=Monday, 2=Tue., ..., 6=Sat., 7=Sun.
    * current_weekday is an integer indicating the desired day of the week, s.t. 1=Monday, 2=Tue., ..., 6=Sat., 7=Sun.
    * Example:
    * offset_of_first_weekday_after_weekday(2, 2) => 7
    * offset_of_first_weekday_after_weekday(5, 2) => 3
    * offset_of_first_weekday_after_weekday(3, 6) => 4
    */
  def offsetOfFirstWeekdayAfterWeekday(desiredWeekday: DayOfWeek, currentWeekday: DayOfWeek): Int = {
    val offset = offsetOfFirstWeekdayAtOrAfterWeekday(desiredWeekday, currentWeekday)
    if (offset == 0) 7
    else offset
  }

  /**
    * The return value will be an integer in the range [-7, -1].
    * desired_weekday is an integer indicating the desired day of the week, s.t. 1=Monday, 2=Tue., ..., 6=Sat., 7=Sun.
    * current_weekday is an integer indicating the desired day of the week, s.t. 1=Monday, 2=Tue., ..., 6=Sat., 7=Sun.
    * Example:
    * offset_of_first_weekday_before_weekday(2, 2) => -7
    * offset_of_first_weekday_before_weekday(5, 2) => -4
    * offset_of_first_weekday_before_weekday(3, 6) => -3
    */
  def offsetOfFirstWeekdayBeforeWeekday(desiredWeekday: DayOfWeek, currentWeekday: DayOfWeek): Int = {
    val offset = offsetOfFirstWeekdayAtOrBeforeWeekday(desiredWeekday, currentWeekday)
    if (offset == 0) -7
    else offset
  }

  /**
    * desired_weekday is an integer indicating the desired day of the week, s.t. 1=Monday, 2=Tue., ..., 6=Sat., 7=Sun.
    * Example:
    * first_weekday_after_date(DayOfWeek::Friday, Date.new(2012, 2, 18)) => #<Date: 2012-02-24 ((2455982j,0s,0n),+0s,2299161j)>
    * first_weekday_after_date(DayOfWeek::Friday, Date.new(2012, 2, 24)) => #<Date: 2012-03-02 ((2455989j,0s,0n),+0s,2299161j)>
    */
  def firstWeekdayAfterDate(desiredWeekday: DayOfWeek, date: LocalDate): LocalDate = {
    val offset = offsetOfFirstWeekdayAfterWeekday(desiredWeekday, dayOfWeek(date))
    date.plusDays(offset)
  }

  /**
    * desired_weekday is an integer indicating the desired day of the week, s.t. 1=Monday, 2=Tue., ..., 6=Sat., 7=Sun.
    * Example:
    * first_weekday_at_or_after_date(DayOfWeek::Friday, Date.new(2012, 2, 18)) => #<Date: 2012-02-24 ((2455982j,0s,0n),+0s,2299161j)>
    * first_weekday_at_or_after_date(DayOfWeek::Friday, Date.new(2012, 2, 24)) => #<Date: 2012-02-24 ((2455982j,0s,0n),+0s,2299161j)>
    */
  def firstWeekdayAtOrAfterDate(desiredWeekday: DayOfWeek, date: LocalDate): LocalDate = {
    val offset = offsetOfFirstWeekdayAtOrAfterWeekday(desiredWeekday, dayOfWeek(date))
    date.plusDays(offset)
  }

  /**
    * desired_weekday is an integer indicating the desired day of the week, s.t. 1=Monday, 2=Tue., ..., 6=Sat., 7=Sun.
    * Example:
    * first_weekday_before_date(DayOfWeek::Friday, Date.new(2012, 3, 2)) => #<Date: 2012-02-24 ((2455982j,0s,0n),+0s,2299161j)>
    * first_weekday_before_date(DayOfWeek::Wednesday, Date.new(2012, 3, 2)) => #<Date: 2012-02-29 ((2455987j,0s,0n),+0s,2299161j)>
    */
  def firstWeekdayBeforeDate(desiredWeekday: DayOfWeek, date: LocalDate): LocalDate = {
    val offset = offsetOfFirstWeekdayBeforeWeekday(desiredWeekday, dayOfWeek(date))
    date.plusDays(offset)
  }

  def firstWeekdayAtOrBeforeDate(desiredWeekday: DayOfWeek, date: LocalDate): LocalDate = {
    val offset = offsetOfFirstWeekdayAtOrBeforeWeekday(desiredWeekday, dayOfWeek(date))
    date.plusDays(offset)
  }

  /**
    * returns a Date representing the nth weekday after the given date
    * desired-weekday is an integer indicating the desired day of the week, s.t. 1=Monday, 2=Tue., ..., 6=Sat., 7=Sun.
    * Example:
    * nth_weekday_at_or_after_date(1, DayOfWeek::Friday, Date.new(2012, 2, 3)) => #<Date: 2012-02-03 ((2455961j,0s,0n),+0s,2299161j)>
    * nth_weekday_at_or_after_date(2, DayOfWeek::Friday, Date.new(2012, 2, 3)) => #<Date: 2012-02-10 ((2455968j,0s,0n),+0s,2299161j)>
    */
  def nthWeekdayAtOrAfterDate(n: Int, desiredWeekday: DayOfWeek, date: LocalDate): LocalDate = {
    val weekOffsetInDays = 7 * (n - 1)
    firstWeekdayAtOrAfterDate(desiredWeekday, date.plusDays(weekOffsetInDays))
  }

  def nthWeekdayAtOrBeforeDate(n: Int, desiredWeekday: DayOfWeek, date: LocalDate): LocalDate = {
    val weekOffsetInDays = 7 * (n - 1)
    firstWeekdayAtOrBeforeDate(desiredWeekday, date.minusDays(weekOffsetInDays))
  }

  /**
    * returns a Date representing the nth weekday after the given date
    * desired-weekday is an integer indicating the desired day of the week, s.t. 1=Monday, 2=Tue., ..., 6=Sat., 7=Sun.
    * Example:
    * nth_weekday_after_date(1, DayOfWeek::Friday, Date.new(2012, 2, 18)) => #<Date: 2012-02-24 ((2455982j,0s,0n),+0s,2299161j)>
    * nth_weekday_after_date(2, DayOfWeek::Friday, Date.new(2012, 2, 18)) => #<Date: 2012-03-02 ((2455989j,0s,0n),+0s,2299161j)>
    * nth_weekday_after_date(4, DayOfWeek::Wednesday, Date.new(2012, 2, 18)) => #<Date: 2012-03-14 ((2456001j,0s,0n),+0s,2299161j)>
    */
  def nthWeekdayAfterDate(n: Int, desiredWeekday: DayOfWeek, date: LocalDate): LocalDate = {
    val weekOffsetInDays = 7 * (n - 1)
    firstWeekdayAfterDate(desiredWeekday, date.plusDays(weekOffsetInDays))
  }

  /**
    * returns a Date representing the nth weekday after the given date
    * desired-weekday is an integer indicating the desired day of the week, s.t. 1=Monday, 2=Tue., ..., 6=Sat., 7=Sun.
    * Example:
    * nth_weekday_before_date(1, DayOfWeek::Friday, Date.new(2012, 3, 2)) => #<Date: 2012-02-24 ((2455982j,0s,0n),+0s,2299161j)>
    * nth_weekday_before_date(2, DayOfWeek::Friday, Date.new(2012, 3, 2)) => #<Date: 2012-02-17 ((2455975j,0s,0n),+0s,2299161j)>
    * nth_weekday_before_date(4, DayOfWeek::Wednesday, Date.new(2012, 3, 2)) => #<Date: 2012-02-08 ((2455966j,0s,0n),+0s,2299161j)>
    */
  def nthWeekdayBeforeDate(n: Int, desiredWeekday: DayOfWeek, date: LocalDate): LocalDate = {
    val weekOffsetInDays = 7 * (n - 1)
    firstWeekdayBeforeDate(desiredWeekday, date.minusDays(weekOffsetInDays))
  }

  /**
    * returns a LocalDate representing the nth weekday in the given month.
    * Example:
    * nthWeekdayOfMonth(3, DateTimeConstants.MONDAY, 1, 2012)   ; returns the 3rd monday in January 2012.
    * => #<LocalDate 2012-01-16>
    * nthWeekdayOfMonth(3, DateTimeConstants.MONDAY, 2, 2012)   ; returns the 3rd monday in February 2012.
    * => #<LocalDate 2012-02-20>
    */
  def nthWeekdayOfMonth(n: Int, desiredWeekday: DayOfWeek, month: Month, year: Int): LocalDate =
    nthWeekdayAtOrAfterDate(n, desiredWeekday, firstDayOfMonth(year, month))

  /**
    * Returns a LocalDate representing the last weekday in the given month.
    * source: http://www.irt.org/articles/js050/
    * formula:
    * daysInMonth - (DayOfWeek(daysInMonth,month,year) - desiredWeekday + 7)%7
    * Example:
    * lastWeekdayOfMonth DayOfWeek.Monday Month.February 2012;;
    * val it : NodaTime.LocalDate = Monday, February 27, 2012 {...}
    */
  def lastWeekdayOfMonth(desiredWeekday: DayOfWeek, month: Month, year: Int): LocalDate =
    nthWeekdayAtOrBeforeDate(1, desiredWeekday, lastDayOfMonth(year, month))

  def firstMondayAfter = firstWeekdayAfterDate(Monday, _)

  def firstMondayAtOrAfter = firstWeekdayAtOrAfterDate(Monday, _)

  def firstMondayBefore = firstWeekdayBeforeDate(Monday, _)

  def firstMondayAtOrBefore = firstWeekdayAtOrBeforeDate(Monday, _)

  def previousBusinessDay(date: LocalDate) = {
    if (dayOfWeek(date) == Monday)
      date.minusDays(3)
    else
      date.minusDays(1)
  }

  def nextBusinessDay(date: LocalDate) = {
    if (dayOfWeek(date) == Friday)
      date.plusDays(3)
    else
      date.plusDays(1)
  }

  def isBusinessDay(date: LocalDate): Boolean = intDayOfWeek(date) < intDayOfWeek(Saturday)
