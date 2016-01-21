package com.github.davidkellis.seven

import com.github.davidkellis.seven.Time._
import org.joda.time.LocalDate


object SpecialDates {
  def newYears = date(_, 1, 1)

  // third monday in January in the given year
  def martinLutherKingJrDay = nthWeekdayOfMonth(3, Monday, January, _)

  /**
    * president's day - third monday in February in the given year
    *
    * NOTE: Washington's Birthday was first declared a federal holiday by an 1879 act of Congress.
    * The Monday Holiday Law, enacted in 1968, shifted the date of the commemoration of Washington's Birthday
    * from February 22 to the third Monday in February, but neither that law nor any subsequent law changed
    * the name of the holiday from Washington's Birthday to President's Day. Although the third Monday in
    * February has become popularly known as President's Day, the NYSE's designation of Washington's Birthday
    * as an Exchange holiday (Rule 51) follows the form of the federal holiday outlined above (section 6103(a)
    * of title 5 of the United States Code).
    */
  def presidentsDay = nthWeekdayOfMonth(3, Monday, February, _)

  // last Monday in May
  def memorialDay = lastWeekdayOfMonth(Monday, May, _)

  // July 4th
  def independenceDay = date(_, July, 4)

  // first Monday in September
  def laborDay = nthWeekdayOfMonth(1, Monday, September, _)

  // second Monday in October
  // NOTE: CBOE still observes Columbus day, but NYSE does not (NYSE observed it between 1909-1953).
  def columbusDay = nthWeekdayOfMonth(2, Monday, October, _)

  // fourth Thursday in November
  def thanksgiving = nthWeekdayOfMonth(4, Thursday, November, _)

  def christmas = date(_, December, 25)

  /**
    * This is a non-trivial calculation. See http://en.wikipedia.org/wiki/Computus
    * "Computus (Latin for "computation") is the calculation of the date of Easter in the Christian calendar."
    * Evidently the scientific study of computation (or Computer Science, as we like to call it) was born out
    * of a need to calculate when easter was going to be.
    * See http://www.linuxtopia.org/online_books/programming_books/python_programming/python_ch38.html
    * There is also a clojure version (that doesn't work properly) at: http://www.bitshift.me/calculate-easter-in-clojure/
    * The following code was taken from: http://www.merlyn.demon.co.uk/estralgs.txt
    * function McClendon(YR) {
    * var g, c, x, z, d, e, n
    * g = YR % 19 + 1               // Golden
    * c = ((YR/100)|0) + 1          // Century
    * x = ((3*c/4)|0) - 12          // Solar
    * z = (((8*c+5)/25)|0) - 5      // Lunar
    * d = ((5*YR/4)|0) - x - 10     // Letter ?
    * e = (11*g + 20 + z - x) % 30  // Epact
    * if (e<0) e += 30              // Fix 9006 problem
    * if ( ( (e==25) && (g>11) ) || (e==24) ) e++
    * n = 44 - e
    * if (n<21) n += 30             // PFM
    * return n + 7 - ((d+n)%7)      // Following Sunday
    * }
    */
  def easter(year: Int): LocalDate = {
    val g = year % 19 + 1
    val c = year / 100 + 1
    val x = (3 * c / 4) - 12
    val z = (8 * c + 5) / 25 - 5
    val d = 5 * year / 4 - x - 10
    val e = (11 * g + 20 + z - x) % 30
    val e1 = if (e < 0) {
      e + 30
    } else {
      e
    }
    val e2 = if ((e1 == 25 && g > 11) || e1 == 24) {
      e1 + 1
    } else {
      e1
    }
    val n = 44 - e2
    val n1 = if (n < 21) {
      n + 30
    } else {
      n
    }
    val n2 = (n1 + 7) - ((d + n1) % 7)
    val day = if (n2 > 31) n2 - 31 else n2
    val month = if (n2 > 31) 4 else 3
    date(year, month, day)
  }

  // the Friday before Easter Sunday
  def goodFriday = easter(_).minusDays(2)

  /**
    * holidayFn is a function of an integer year that returns a LocalDate representing the date
    * that the holiday falls on in that year
    * Example: isHoliday(datetimeUtils(2012, 1, 16), martinLutherKingJrDay) => true
    */
  def isHoliday(date: LocalDate, holidayFn: (Int) => LocalDate): Boolean = holidayFn(date.getYear()) == date

  val HolidayLookupFunctions = Array[(Int) => LocalDate](
    newYears,
    martinLutherKingJrDay,
    presidentsDay,
    goodFriday,
    memorialDay,
    independenceDay,
    laborDay,
    //    columbusDay;    // CBOE still observes Columbus day, but NYSE does not (NYSE observed it between 1909-1953).
    thanksgiving,
    christmas
  )

  def isAnyHoliday(date: LocalDate): Boolean = HolidayLookupFunctions.exists(holidayLookupFn => isHoliday(date, holidayLookupFn))

  /**
    * sources:
    * http://www1.nyse.com/pdfs/closings.pdf
    * http://www.cboe.com/publish/RegCir/RG12-150.pdf
    */
  def UnscheduledMarketClosures = Set(
    date(1972, 12, 28), // Closed. Funeral of former President Harry S. Truman.
    date(1973, 1, 25), // Closed for funeral of former President Lyndon B. Johnson.
    date(1977, 7, 14), // Closed due to blackout in New York City.
    date(1985, 9, 27), // Market closed due to Hurricane Gloria.
    date(1994, 4, 27), // Closed for funeral of former President Richard M. Nixon.
    date(2001, 9, 11), // Closed following the terrorist attack on the World Trade Center.
    date(2001, 9, 12), // Closed following the terrorist attack on the World Trade Center.
    date(2001, 9, 13), // Closed following the terrorist attack on the World Trade Center.
    date(2001, 9, 14), // Closed following the terrorist attack on the World Trade Center.
    date(2004, 6, 11), // Closed in observance of the National Day of Mourning for former President Ronald W. Reagan (died June 5, 2004)
    date(2007, 1, 2), // Closed in observance of the National Day of Mourning for former President Gerald R. Ford (died December 26, 2006).
    date(2012, 10, 29), // Closed Monday for Hurricane Sandy
    date(2012, 10, 30) // Closed Tuesday for Hurricane Sandy
  )

  def isDateUnscheduledMarketClosure(date: LocalDate) = UnscheduledMarketClosures.contains(date)

  // see the holiday rules at: http://cfe.cboe.com/aboutcfe/ExpirationCalendar.aspx
  def isMarketHoliday(includeUnscheduledMarketClosures: Boolean, date: LocalDate): Boolean = {
    val todayIsUnscheduledClosure = if (includeUnscheduledMarketClosures) isDateUnscheduledMarketClosure(date) else false
    val todayIsFridayAndSaturdayIsHoliday = isDateFriday(date) && isAnyHoliday(nextDay(date))
    val todayIsMondayAndSundayIsHoliday = isDateMonday(date) && isAnyHoliday(previousDay(date))
    isAnyHoliday(date) || todayIsFridayAndSaturdayIsHoliday || todayIsMondayAndSundayIsHoliday || todayIsUnscheduledClosure
  }

  def isMarketBusinessDay(includeUnscheduledMarketClosures: Boolean, date: LocalDate): Boolean =
    isBusinessDay(date) && !isMarketHoliday(includeUnscheduledMarketClosures, date)
}
