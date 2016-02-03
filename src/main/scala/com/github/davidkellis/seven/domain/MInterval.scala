package com.github.davidkellis.seven.domain

import org.joda.time.Interval

import com.github.davidkellis.seven.Time.{compareDateTimes, intervalBetween}

object MInterval {
  type MInterval = scala.collection.immutable.IndexedSeq[Interval]

  val emptyMInterval: MInterval = Vector()

  def createMInterval(intervals: IndexedSeq[Interval]): MInterval = intervals.toVector.sortBy(_.getStart)

  // represents the computation: minuend - subtrahend = difference
  def subtractMInterval(minuend: MInterval, subtrahend: MInterval): MInterval = {
    val mintervals = for(m <- minuend; s <- subtrahend) yield subtractInterval(m, s)
    createMInterval(mintervals.flatten)
  }

  /*
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
  def subtractInterval(minuend: Interval, subtrahend: Interval): MInterval = {
    if (subtrahend.overlaps(minuend)) {
      val startMinuend = minuend.getStart
      val endMinuend = minuend.getEnd
      val startSubtrahend = subtrahend.getStart
      val endSubtrahend = subtrahend.getEnd

      compareDateTimes(startMinuend, startSubtrahend) match {
        case -1 =>        // startMinuend < startSubtrahend
          compareDateTimes(endMinuend, endSubtrahend) match {
            case -1 =>    // endMinuend < endSubtrahend
              Vector(intervalBetween(startMinuend, startSubtrahend))
            case 1 =>     // endMinuend > endSubtrahend
              Vector(intervalBetween(startMinuend, startSubtrahend),
                     intervalBetween(endSubtrahend, endMinuend))
            case 0 =>     // endMinuend == endSubtrahend
              Vector(intervalBetween(startMinuend, startSubtrahend))
          }
        case 1 =>         // startMinuend > startSubtrahend
          compareDateTimes(endMinuend, endSubtrahend) match {
            case -1 =>    // endMinuend < endSubtrahend
              emptyMInterval
            case 1 =>     // endMinuend > endSubtrahend
              Vector(intervalBetween(endSubtrahend, endMinuend))
            case 0 =>     // endMinuend == endSubtrahend
              emptyMInterval
          }
        case 0 =>         // startMinuend == startSubtrahend
          compareDateTimes(endMinuend, endSubtrahend) match {
            case -1 =>    // endMinuend < endSubtrahend
              emptyMInterval
            case 1 =>     // endMinuend > endSubtrahend
              Vector(intervalBetween(endSubtrahend, endMinuend))
            case 0 =>     // endMinuend == endSubtrahend
              emptyMInterval
          }
      }
    } else Vector(minuend)
  }

  def overlaps(mInterval1: MInterval, mInterval2: MInterval): Boolean = {
    val intervalPairs = for(i1 <- mInterval1; i2 <- mInterval2) yield (i1, i2)
    intervalPairs.exists(pair => pair._1.overlaps(pair._2))
  }

  def isEmpty(mInterval: MInterval): Boolean = mInterval.isEmpty
}