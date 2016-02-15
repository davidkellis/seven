package com.github.davidkellis.seven

import scala.math.BigDecimal.RoundingMode

object MathUtil {
  def ceil(decimal: Double): Double = Math.ceil(decimal)
  def ceil(decimal: BigDecimal): BigDecimal = decimal.setScale(0, RoundingMode.CEILING)

  def intCeil(decimal: Double): Int = ceil(decimal).toInt
  def intCeil(decimal: BigDecimal): Int = ceil(decimal).toInt

  def floor(decimal: Double): Double = Math.floor(decimal)
  def floor(decimal: BigDecimal): BigDecimal = decimal.setScale(0, RoundingMode.FLOOR)

  def intFloor(decimal: Double): Int = floor(decimal).toInt
  def intFloor(decimal: BigDecimal): Int = floor(decimal).toInt

  def round(decimal: Double): Double = Math.round(decimal)
  // see section on HALF_UP on http://docs.oracle.com/javase/6/docs/api/index.html?java/math/RoundingMode.html
  // -> on HALF_UP: "this is the rounding mode commonly taught at school"
  def round(decimal: BigDecimal): BigDecimal = decimal.setScale(0, RoundingMode.HALF_UP)

  def intRound(decimal: Double): Int = round(decimal).toInt
  def intRound(decimal: BigDecimal): Int = round(decimal).toInt
}
