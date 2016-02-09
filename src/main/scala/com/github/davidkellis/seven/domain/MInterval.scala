package com.github.davidkellis.seven.domain

import org.joda.time.Interval

import com.github.davidkellis.seven.Time.{compareDateTimes, intervalBetween}

object MInterval {
  def empty: MInterval = new MInterval(Array.empty[Interval])
}

class MInterval(var intervals: Array[Interval]) {
  intervals = intervals.sortBy(_.getStart)

  def this(interval: Interval) {
    this(Array(interval))
  }

  def overlaps(that: MInterval): Boolean = {
    val intervalPairs = for(i1 <- this.intervals; i2 <- that.intervals) yield (i1, i2)
    intervalPairs.exists(pair => pair._1.overlaps(pair._2))
  }

  def isEmpty: Boolean = intervals.isEmpty

  // represents the computation: minuend (represented by this) - subtrahend = difference
  def -(subtrahend: MInterval): MInterval = {
    val mIntervals = for(m <- this.intervals; s <- subtrahend.intervals) yield subtractInterval(m, s)
    new MInterval(mIntervals.flatMap(_.intervals))
  }

  /**
   * Returns a vector of intervals (a.k.a. a vinterval), each representing a portion of the remaining interval after
   *   the subtrahend interval has been subtracted from the minuend interval.
   *   In other words, (subtract-interval b c) === c - b.
   * The returned vector will consist of 0, 1, or 2 interval objects.
   *   An empty return vector represents an empty interval.
   * Note (source: http://en.wikipedia.org/wiki/Subtraction):
   *   Since subtraction is not a commutative operator, the two operands are named.
   *   The traditional names for the parts of the formula
   *   c − b = a
   *   are minuend (c) − subtrahend (b) = difference (a).
   */
  private def subtractInterval(minuend: Interval, subtrahend: Interval): MInterval = {
    if (subtrahend.overlaps(minuend)) {
      val startMinuend = minuend.getStart
      val endMinuend = minuend.getEnd
      val startSubtrahend = subtrahend.getStart
      val endSubtrahend = subtrahend.getEnd

      compareDateTimes(startMinuend, startSubtrahend) match {
        case -1 =>        // startMinuend < startSubtrahend
          compareDateTimes(endMinuend, endSubtrahend) match {
            case -1 =>    // endMinuend < endSubtrahend
              new MInterval(intervalBetween(startMinuend, startSubtrahend))
            case 1 =>     // endMinuend > endSubtrahend
              new MInterval(Array(
                intervalBetween(startMinuend, startSubtrahend),
                intervalBetween(endSubtrahend, endMinuend)
              ))
            case 0 =>     // endMinuend == endSubtrahend
              new MInterval(intervalBetween(startMinuend, startSubtrahend))
          }
        case 1 =>         // startMinuend > startSubtrahend
          compareDateTimes(endMinuend, endSubtrahend) match {
            case -1 =>    // endMinuend < endSubtrahend
              MInterval.empty
            case 1 =>     // endMinuend > endSubtrahend
              new MInterval(intervalBetween(endSubtrahend, endMinuend))
            case 0 =>     // endMinuend == endSubtrahend
              MInterval.empty
          }
        case 0 =>         // startMinuend == startSubtrahend
          compareDateTimes(endMinuend, endSubtrahend) match {
            case -1 =>    // endMinuend < endSubtrahend
              MInterval.empty
            case 1 =>     // endMinuend > endSubtrahend
              new MInterval(intervalBetween(endSubtrahend, endMinuend))
            case 0 =>     // endMinuend == endSubtrahend
              MInterval.empty
          }
      }
    } else new MInterval(minuend)
  }
}