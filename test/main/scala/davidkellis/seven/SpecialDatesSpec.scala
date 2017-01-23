package main.scala.davidkellis.seven

//import org.junit.runner.RunWith
//import org.scalatest.junit.JUnitRunner
import davidkellis.seven.Time._
import davidkellis.seven.SpecialDates._
import org.scalatest.FunSpec

//@RunWith(classOf[JUnitRunner])
class SpecialDatesSpec extends FunSpec {
  describe("isAnyHoliday") {
    it("returns true if the given date is a holiday") {
      assert(isAnyHoliday(date(2013, 3, 29)) === true)    // Good Friday
    }
  }

  describe("easter") {
    it("returns the DateTime that represents the beginning of the day on Easter, given a year") {
      assert(easter(2009) === date(2009, 4, 12))
      assert(easter(2010) === date(2010, 4, 4))
      assert(easter(2011) === date(2011, 4, 24))
      assert(easter(2012) === date(2012, 4, 8))
      assert(easter(2013) === date(2013, 3, 31))
    }
  }

  describe("goodFriday") {
    it("returns the DateTime that represents the beginning of the day of Good Friday, given a year") {
      assert(goodFriday(2009) === date(2009, 4, 10))
      assert(goodFriday(2010) === date(2010, 4, 2))
      assert(goodFriday(2011) === date(2011, 4, 22))
      assert(goodFriday(2012) === date(2012, 4, 6))
      assert(goodFriday(2013) === date(2013, 3, 29))
    }
  }
}
