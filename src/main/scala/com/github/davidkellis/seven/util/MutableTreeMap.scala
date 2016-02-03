package com.github.davidkellis.seven.util

import java.util

object MutableTreeMap {
  def empty[K, V] = new util.TreeMap[K, V]()
}
