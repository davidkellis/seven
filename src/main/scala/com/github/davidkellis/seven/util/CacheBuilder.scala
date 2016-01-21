package com.github.davidkellis.seven.util

import net.sf.ehcache.{CacheManager, Cache}
import net.sf.ehcache.config.CacheConfiguration
import net.sf.ehcache.store.MemoryStoreEvictionPolicy

object CacheBuilder {
  private val singletonCacheManager = CacheManager.create()   // get or create the singleton CacheManager

  // builds least-recently-used cache
  def buildLruCache(threshold: Int, cacheName: String = randomUuid()): Cache = {
    val cacheConfig = new CacheConfiguration()
      .name(cacheName)
      .maxEntriesLocalHeap(threshold)
      .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU)
      .eternal(false)
    val cache = new Cache(cacheConfig)
    singletonCacheManager.addCache(cache)
    cache
  }

  // builds least-frequently-used cache
  def buildLfuCache(threshold: Int, cacheName: String = randomUuid()): Cache = {
    val cacheConfig = new CacheConfiguration()
      .name(cacheName)
      .maxEntriesLocalHeap(threshold)
      .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
      .eternal(false)
    val cache = new Cache(cacheConfig)
    singletonCacheManager.addCache(cache)
    cache
  }

  // builds fifo cache
  def buildFifoCache(threshold: Int, cacheName: String = randomUuid()): Cache = {
    val cacheConfig = new CacheConfiguration()
      .name(cacheName)
      .maxEntriesLocalHeap(threshold)
      .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.FIFO)
      .eternal(false)
    val cache = new Cache(cacheConfig)
    singletonCacheManager.addCache(cache)
  cache
  }

  private def randomUuid(): String = java.util.UUID.randomUUID().toString
}
