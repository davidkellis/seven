package davidkellis.seven

import java.util.Locale
import java.time._
import java.time.DayOfWeek._
import java.time.Month._
import java.time.format._
import java.time.temporal.TemporalAmount

import org.threeten.extra._
//import java.time.format.{ISOPeriodFormat}

import scala.util.Random

object Time {
  class PeriodDuration(var years: Int, var months: Int, var days: Int, var hours: Int, var minutes: Int, var seconds: Long, var millis: Long) {
    override def toString(): String = f""

    def plus(t: LocalDate): LocalDate = {
      var t2 = t
      t2 = if (years > 0) t2.plusYears(years) else t2
      t2 = if (months > 0) t2.plusMonths(months) else t2
      t2 = if (days > 0) t2.plusDays(days) else t2
      t2
    }
    def plus(t: ZonedDateTime): ZonedDateTime = {
      var t2 = t
      t2 = if (years > 0) t2.plusYears(years) else t2
      t2 = if (months > 0) t2.plusMonths(months) else t2
      t2 = if (days > 0) t2.plusDays(days) else t2
      t2 = if (hours > 0) t2.plusHours(hours) else t2
      t2 = if (minutes > 0) t2.plusMinutes(minutes) else t2
      t2 = if (seconds > 0) t2.plusSeconds(seconds) else t2
      t2 = if (millis > 0) t2.plusNanos(millis * 1000000) else t2
      t2
    }

    def minus(t: LocalDate): LocalDate = {
      var t2 = t
      t2 = if (years > 0) t2.minusYears(years) else t2
      t2 = if (months > 0) t2.minusMonths(months) else t2
      t2 = if (days > 0) t2.minusDays(days) else t2
      t2
    }
    def minus(t: ZonedDateTime): ZonedDateTime = {
      var t2 = t
      t2 = if (years > 0) t2.minusYears(years) else t2
      t2 = if (months > 0) t2.minusMonths(months) else t2
      t2 = if (days > 0) t2.minusDays(days) else t2
      t2 = if (hours > 0) t2.minusHours(hours) else t2
      t2 = if (minutes > 0) t2.minusMinutes(minutes) else t2
      t2 = if (seconds > 0) t2.minusSeconds(seconds) else t2
      t2 = if (millis > 0) t2.minusNanos(millis * 1000000) else t2
      t2
    }
  }
  object PeriodDuration {
    def apply(years: Int, months: Int, days: Int, hours: Long, minutes: Long, seconds: Long, millis: Long): PeriodDuration =
      PeriodDuration(years, months, days, hours, minutes, seconds, millis)
    def ofYears(years: Int): PeriodDuration = PeriodDuration(years, 0, 0, 0, 0, 0, 0)
    def ofMonths(months: Int): PeriodDuration = PeriodDuration(0, months, 0, 0, 0, 0, 0)
    def ofDays(days: Int): PeriodDuration = PeriodDuration(0, 0, days, 0, 0, 0, 0)
    def ofHours(hours: Int): PeriodDuration = PeriodDuration(0, 0, 0, hours, 0, 0, 0)
    def ofMinutes(minutes: Int): PeriodDuration = PeriodDuration(0, 0, 0, 0, minutes, 0, 0)
    def ofSeconds(seconds: Int): PeriodDuration = PeriodDuration(0, 0, 0, 0, 0, seconds, 0)
    def ofMilliseconds(millis: Int): PeriodDuration = PeriodDuration(0, 0, 0, 0, 0, 0, millis)

//    // startInclusive <= endExclusive
//    def between(startInclusive: Instant, endExclusive: Instant): PeriodDuration = {
//      if (startInclusive.isAfter(endExclusive)) throw new DateTimeException("Unable to determine period between two instants.")
//      startInclusive.
//    }
  }

  implicit def dateTimeOrdering: Ordering[ZonedDateTime] = Ordering.fromLessThan(isBefore(_, _))
  implicit def localDateOrdering: Ordering[LocalDate] = Ordering.fromLessThan[LocalDate](_.isBefore(_))

  type Timestamp = Long
  type Datestamp = Int

  def dayOfWeek(dayOfWeekInt: Int): DayOfWeek = dayOfWeekInt match {
    case 1 => MONDAY
    case 2 => TUESDAY
    case 3 => WEDNESDAY
    case 4 => THURSDAY
    case 5 => FRIDAY
    case 6 => SATURDAY
    case 7 => SUNDAY
    case _ => throw new Exception("Day of week must be in range 1-7 inclusive.")
  }

  def dayOfWeek(date: LocalDate): DayOfWeek = date.getDayOfWeek
  def dayOfWeek(time: ZonedDateTime): DayOfWeek = time.getDayOfWeek

  def isDateMonday(date: LocalDate) = dayOfWeek(date) == MONDAY
  def isDateTuesday(date: LocalDate) = dayOfWeek(date) == TUESDAY
  def isDateWednesday(date: LocalDate) = dayOfWeek(date) == WEDNESDAY
  def isDateThursday(date: LocalDate) = dayOfWeek(date) == THURSDAY
  def isDateFriday(date: LocalDate) = dayOfWeek(date) == FRIDAY
  def isDateSaturday(date: LocalDate) = dayOfWeek(date) == SATURDAY
  def isDateSunday(date: LocalDate) = dayOfWeek(date) == SUNDAY


  def intDayOfWeek(dayOfWeek: DayOfWeek): Int = dayOfWeek match {
    case MONDAY => 1
    case TUESDAY => 2
    case WEDNESDAY => 3
    case THURSDAY => 4
    case FRIDAY => 5
    case SATURDAY => 6
    case SUNDAY => 7
  }
  def intDayOfWeek(t: ZonedDateTime): Int = intDayOfWeek(t.getDayOfWeek)
  def intDayOfWeek(t: LocalDate): Int = intDayOfWeek(t.getDayOfWeek)

  def intMonth(month: Month): Int = month match {
    case JANUARY => 1
    case FEBRUARY => 2
    case MARCH => 3
    case APRIL => 4
    case MAY => 5
    case JUNE => 6
    case JULY => 7
    case AUGUST => 8
    case SEPTEMBER => 9
    case OCTOBER => 10
    case NOVEMBER => 11
    case DECEMBER => 12
  }

  def findTimeZone(timeZoneName: String): ZoneId = ZoneId.of(timeZoneName)

  val EasternTimeZone = findTimeZone("US/Eastern")  // "US/Central", "US/Pacific"

  def currentTime(timeZone: ZoneId = EasternTimeZone): ZonedDateTime = ZonedDateTime.now(timeZone)

  def year(time: ZonedDateTime): Int = time.getYear
  def monthInt(time: ZonedDateTime): Int = time.getMonthValue
  def month(time: ZonedDateTime): Month = time.getMonth
  def dayOfMonth(time: ZonedDateTime): Int = time.getDayOfMonth

  def date(time: ZonedDateTime): LocalDate = LocalDate.of(year(time), monthInt(time), dayOfMonth(time))
  def date(timestamp: Timestamp): LocalDate = date(datetime(timestamp))
  def date(year: Int): LocalDate = new LocalDate(year, 1, 1)
  //  def date(year: Int, month: Int): LocalDate = new LocalDate(year, month, 1)
  def date(year: Int, month: Int, day: Int): LocalDate = new LocalDate(year, month, day)
  def date(year: Int, month: Month, day: Int): LocalDate = new LocalDate(year, month, day)

  val yyyyMMdd = DateTimeFormatter.BASIC_ISO_DATE
  def datestamp(date: LocalDate): Datestamp = yyyyMMdd.format(date).toInt
  def datestamp(time: Timestamp): Datestamp = time.toString.substring(0, 8).toInt
  def datestamp(time: ZonedDateTime): Datestamp = datestamp(date(time))

  def datetime(year: Int, month: Int, day: Int): ZonedDateTime = datetime(year, month, day, 0, 0, 0)
  def datetime(year: Int, month: Month, day: Int): ZonedDateTime = datetime(year, intMonth(month), day, 0, 0, 0)
  def datetime(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): ZonedDateTime =
    new ZonedDateTime(year, month, day, hour, minute, second, EasternTimeZone)
  def datetime(year: Int, month: Month, day: Int, hour: Int, minute: Int, second: Int): ZonedDateTime =
    new ZonedDateTime(year, intMonth(month), day, hour, minute, second, EasternTimeZone)
  def datetime(date: LocalDate): ZonedDateTime = datetime(date.getYear, date.getMonth, date.getDayOfMonth)
  def datetime(date: LocalDate, hour: Int, minute: Int, second: Int): ZonedDateTime =
    datetime(date.getYear, date.getMonth, date.getDayOfMonth, hour, minute, second)
  def datetime(date: LocalDate, time: LocalTime): ZonedDateTime =
    datetime(date.getYear, date.getMonth, date.getDayOfMonth, time.getHour, time.getMinute, time.getSecond)
  def datetime(datestamp: Datestamp): ZonedDateTime = {
    val ds = datestamp.toString
    val year = ds.substring(0, 4).toInt
    val month = ds.substring(4, 6).toInt
    val day = ds.substring(6, 8).toInt
    datetime(year, month, day)
  }
  def datetime(timestamp: Timestamp): ZonedDateTime = {
    val ts = timestamp.toString
    val year = ts.substring(0, 4).toInt
    val month = ts.substring(4, 6).toInt
    val day = ts.substring(6, 8).toInt
    val hour = ts.substring(8, 10).toInt
    val minute = ts.substring(10, 12).toInt
    val second = ts.substring(12, 14).toInt
    datetime(year, month, day, hour, minute, second)
  }

  def localtime(hour: Int, minute: Int, second: Int): LocalTime = new LocalTime(hour, minute, second)

  val yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
  def timestamp(date: LocalDate): Timestamp = yyyyMMddHHmmss.format(datetime(date)).toLong
  def timestamp(datetime: ZonedDateTime): Timestamp = yyyyMMddHHmmss.format(datetime).toLong

  def midnight(date: LocalDate): ZonedDateTime = datetime(date, 0, 0, 0)
  def midnight(datetime: ZonedDateTime): ZonedDateTime = datetime.withHour(0).withMinute(0).withSecond(0)

  def years(n: Int): PeriodDuration = PeriodDuration.ofYears(n)
  def months(n: Int): PeriodDuration = PeriodDuration.ofMonths(n)
  def weeks(n: Int): PeriodDuration = PeriodDuration.ofDays(n * 7)
  def days(n: Int): PeriodDuration = PeriodDuration.ofDays(n)
  def hours(n: Int): PeriodDuration = PeriodDuration.ofHours(n)
  def minutes(n: Int): PeriodDuration = PeriodDuration.ofMinutes(n)
  def seconds(n: Int): PeriodDuration = PeriodDuration.ofSeconds(n)
  def millis(n: Int): PeriodDuration = PeriodDuration.ofMilliseconds(n)

//  def periodBetween(i1: Instant, i2: Instant): PeriodDuration = PeriodDuration.between(i1, i2)

  def durationBetween(t1: ZonedDateTime, t2: ZonedDateTime): Duration = Duration.between(t1, t2)

  def intervalBetween(t1: Instant, t2: Instant): Interval = Interval.of(t1, t2)
  def intervalBetween(t1: ZonedDateTime, t2: ZonedDateTime): Interval = intervalBetween(t1.toInstant, t2.toInstant)

  def formatPeriod(period: PeriodDuration): String = period.toString
  def parsePeriod(periodString: String): PeriodDuration = ???

  // t1 <= instant < t2
  def isInstantBetween(instant: ZonedDateTime, t1: ZonedDateTime, t2: ZonedDateTime): Boolean =
    intervalBetween(t1, t2).contains(instant.toInstant)

  // t1 <= instant <= t2
  def isInstantBetweenInclusive(instant: ZonedDateTime, t1: ZonedDateTime, t2: ZonedDateTime): Boolean =
    !isBefore(instant, t1) && !isAfter(instant, t2)

  def maxDateTime(t1: ZonedDateTime, t2: ZonedDateTime): ZonedDateTime = if (t1.isAfter(t2)) t1 else t2

  def minDateTime(t1: ZonedDateTime, t2: ZonedDateTime): ZonedDateTime = if (t1.isBefore(t2)) t1 else t2

  // Returns a random datetimeUtils between t1 (inclusive) and t2 (exclusive)
  def randomDateTime(t1: ZonedDateTime, t2: ZonedDateTime): ZonedDateTime = {
    val r = new Random().nextDouble()
    val duration = durationBetween(t1, t2)
    val millisecondOffset = (r * duration.toMillis).toLong
    t1.plus(Duration.ofMillis(millisecondOffset))
  }

  def isIntervalEmpty(interval: Interval): Boolean = {
    interval.getStart() == interval.getEnd()
  }

  // t1 < t2
  def isBefore(t1: ZonedDateTime, t2: ZonedDateTime): Boolean = t1.isBefore(t2)

  // t1 > t2
  def isAfter(t1: ZonedDateTime, t2: ZonedDateTime): Boolean = t1.isAfter(t2)

  def compareDateTimes(t1: ZonedDateTime, t2: ZonedDateTime): Int = t1.compareTo(t2)
  def compareDateTimes(d1: LocalDate, d2: LocalDate): Int = d1.compareTo(d2)
  def compareDateTimes(i1: Instant, i2: Instant): Int = i1.compareTo(i2)

  // returns true if t1 <= t2
  def isBeforeOrEqual(t1: ZonedDateTime, t2: ZonedDateTime): Boolean = compareDateTimes(t1, t2) <= 0
  def isBeforeOrEqual(d1: LocalDate, d2: LocalDate): Boolean = compareDateTimes(d1, d2) <= 0

  // returns true if t1 >= t2
  def isAfterOrEqual(t1: ZonedDateTime, t2: ZonedDateTime): Boolean = compareDateTimes(t1, t2) >= 0
  def isAfterOrEqual(d1: LocalDate, d2: LocalDate): Boolean = compareDateTimes(d1, d2) >= 0

  sealed trait TimeDirection
  case object Before extends TimeDirection
  case object After extends TimeDirection

  def offsetDateTime(t: Instant, direction: TimeDirection, magnitude: Duration): Instant = direction match {
    case Before => t.minus(magnitude)
    case After => t.plus(magnitude)
    case _ => t
  }

  def offsetInterval(interval: Interval,
                     startOffsetDirection: TimeDirection,
                     startOffsetMagnitude: Duration,
                     endOffsetDirection: TimeDirection,
                     endOffsetMagnitude: Duration): Interval = {
    val adjustedStart = offsetDateTime(interval.getStart, startOffsetDirection, startOffsetMagnitude)
    val adjustedEnd = offsetDateTime(interval.getEnd, endOffsetDirection, endOffsetMagnitude)
    intervalBetween(adjustedStart, adjustedEnd)
  }

  // returns an infinite Stream of [t, f(t), f(f(t)), f(f(f(t))), ...]
  def timeSeries(startTime: ZonedDateTime, nextTimeFn: (ZonedDateTime) => ZonedDateTime): Stream[ZonedDateTime] =
    Stream.iterate(startTime)(nextTimeFn)

  // returns an infinite Stream of [d, f(d), f(f(d)), f(f(f(d))), ...]
  def dateSeries(startDate: LocalDate, nextDateFn: (LocalDate) => LocalDate): Stream[LocalDate] =
    Stream.iterate(startDate)(nextDateFn)

  // returns an infinite Stream of [t t+p t+2p t+3p ...]
  def infPeriodicalTimeSeries(startTime: ZonedDateTime, period: PeriodDuration): Stream[ZonedDateTime] = timeSeries(startTime, period.plus(_))

  // returns an infinite Stream of [d d+p d+2p d+3p ...]
  def infPeriodicalDateSeries(startDate: LocalDate, period: PeriodDuration): Stream[LocalDate] = dateSeries(startDate, period.plus(_))

  // returns a sequence of DateTimes, [t1, t2, ..., tN], that are separated by a given Duration, s.t. startTime = t1 and tN <= endTime
  def periodicalTimeSeries(startTime: ZonedDateTime, endTime: ZonedDateTime, period: PeriodDuration): Stream[ZonedDateTime] =
    infPeriodicalTimeSeries(startTime, period).takeWhile(isBeforeOrEqual(_, endTime))

  // returns a sequence of LocalDate, [t1, t2, ..., tN], that are separated by a given Duration, s.t. startTime = t1 and tN <= endTime
  def periodicalDateSeries(startDate: LocalDate, endDate: LocalDate, period: PeriodDuration): Stream[LocalDate] =
    infPeriodicalDateSeries(startDate, period).takeWhile(isBeforeOrEqual(_, endDate))

  // returns an infinite sequence
  // returns a sequence of Intervals, [i1, i2, ..., iN], s.t. the start time of subsequent intervals is separated
  // by a given Duration, <separationLength> and each interval spans an amount of time given by <intervalLength>
  def infInterspersedIntervals(startTime: ZonedDateTime, intervalLength: PeriodDuration, separationLength: PeriodDuration): Stream[Interval] = {
    val startTimes = infPeriodicalTimeSeries(startTime, separationLength)
    startTimes.map(t => intervalBetween(t, intervalLength.plus(t)))
  }

  def interspersedIntervals(firstIntervalStartTime: ZonedDateTime,
                             lastIntervalStartTime: ZonedDateTime,
                             intervalLength: PeriodDuration,
                             separationLength: PeriodDuration): Stream[Interval] = {
    val startTimes = infPeriodicalTimeSeries(firstIntervalStartTime, separationLength).takeWhile(isBeforeOrEqual(_, lastIntervalStartTime))
    startTimes.map(t => intervalBetween(t, intervalLength.plus(t)))
  }

//  def interspersedIntervals(startTimeInterval: Interval, intervalLength: PeriodDuration, separationLength: PeriodDuration): Stream[Interval] =
//    interspersedIntervals2(startTimeInterval.getStart, startTimeInterval.getEnd, intervalLength, separationLength)

  def daysInMonth(month: Int, year: Int): Int = YearMonth.of(year, month).lengthOfMonth()
  def daysInMonth(month: Month, year: Int): Int = YearMonth.of(year, month).lengthOfMonth()

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
      1.to(monthOffset).foldLeft((baseMonth, baseYear)) { (monthYearPair, i) => nextMonth(monthYearPair._1, monthYearPair._2) }
    } else {
      1.to(-monthOffset).foldLeft((baseMonth, baseYear)) { (monthYearPair, i) => previousMonth(monthYearPair._1, monthYearPair._2) }
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
    * lastWeekdayOfMonth DayOfWeek.MONDAY Month.February 2012;;
    * val it : NodaTime.LocalDate = MONDAY, February 27, 2012 {...}
    */
  def lastWeekdayOfMonth(desiredWeekday: DayOfWeek, month: Month, year: Int): LocalDate =
    nthWeekdayAtOrBeforeDate(1, desiredWeekday, lastDayOfMonth(year, month))

  def firstMondayAfter = firstWeekdayAfterDate(MONDAY, _)
  def firstMondayAtOrAfter = firstWeekdayAtOrAfterDate(MONDAY, _)

  def firstMondayBefore = firstWeekdayBeforeDate(MONDAY, _)
  def firstMondayAtOrBefore = firstWeekdayAtOrBeforeDate(MONDAY, _)

  def previousBusinessDay(date: LocalDate) = {
    if (dayOfWeek(date) == MONDAY)
      date.minusDays(3)
    else
      date.minusDays(1)
  }

  def nextBusinessDay(date: LocalDate) = {
    if (dayOfWeek(date) == FRIDAY)
      date.plusDays(3)
    else
      date.plusDays(1)
  }

  def isBusinessDay(date: LocalDate): Boolean = intDayOfWeek(date) < intDayOfWeek(SATURDAY)
}