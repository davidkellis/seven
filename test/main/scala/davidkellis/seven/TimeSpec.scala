package main.scala.davidkellis.seven

//import org.junit.runner.RunWith
//import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSpec
import davidkellis.seven.Time._

//@RunWith(classOf[JUnitRunner])
class TimeSpec extends FunSpec {
  describe("timestamp") {
    it("returns a timestamp representation, yyyymmddhhmmss, of a given DateTime") {
      assert(timestamp(datetime(2013, 7, 15, 20, 1, 45)) === 20130715200145L)
    }
  }

  describe("periodicalTimeSeries") {
    it("returns a sequence of DateTimes, [t1, t2, ..., tN], that are separated by a given Period, s.t. startTime = t1 and tN <= endTime") {
      val startTime = datetime(2013, 7, 1, 12, 0, 0)
      val endTime = datetime(2013, 7, 2, 12, 0, 0)

      val timeSeries = periodicalTimeSeries(startTime, endTime, hours(6)).toList
      val expectedTimeSeries = List(
        datetime(2013, 7, 1, 12, 0, 0),
        datetime(2013, 7, 1, 18, 0, 0),
        datetime(2013, 7, 2, 0, 0, 0),
        datetime(2013, 7, 2, 6, 0, 0),
        datetime(2013, 7, 2, 12, 0, 0)
      )
      assert(timeSeries === expectedTimeSeries)

      val timeSeries2 = periodicalTimeSeries(startTime, endTime, hours(7)).toList
      val expectedTimeSeries2 = List(
        datetime(2013, 7, 1, 12, 0, 0),
        datetime(2013, 7, 1, 19, 0, 0),
        datetime(2013, 7, 2, 2, 0, 0),
        datetime(2013, 7, 2, 9, 0, 0)
      )
      assert(timeSeries2 === expectedTimeSeries2)
    }
  }

  describe("infInterspersedIntervals") {
    it("returns a sequence of Intervals, [i1, i2, ..., iN], s.t. the start time of subsequent intervals is separated by a given Period, <separationLength> and each interval spans an amount of time given by <intervalLength>") {
      val startTime = datetime(2013, 7, 1, 12, 0, 0)
      val endTime = datetime(2013, 7, 2, 12, 0, 0)

      val intervals = infInterspersedIntervals(startTime, hours(5), days(1)).take(5).toList
      val expectedIntervals = List(
        intervalBetween(datetime(2013, 7, 1, 12, 0, 0), datetime(2013, 7, 1, 17, 0, 0)),
        intervalBetween(datetime(2013, 7, 2, 12, 0, 0), datetime(2013, 7, 2, 17, 0, 0)),
        intervalBetween(datetime(2013, 7, 3, 12, 0, 0), datetime(2013, 7, 3, 17, 0, 0)),
        intervalBetween(datetime(2013, 7, 4, 12, 0, 0), datetime(2013, 7, 4, 17, 0, 0)),
        intervalBetween(datetime(2013, 7, 5, 12, 0, 0), datetime(2013, 7, 5, 17, 0, 0))
      )
      assert(intervals === expectedIntervals)
    }
  }
}
