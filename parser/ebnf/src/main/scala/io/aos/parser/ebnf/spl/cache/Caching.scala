package io.aos.parser.ebnf.spl.cache

import com.google.common.cache.{CacheBuilder, CacheLoader}

import grizzled.slf4j.Logging

/**
 * A trait which adds caching ability to classes
 */
trait Caching extends Logging {
  val DefaultCacheSize = 1000

  def cached[T, U](f: T => U, cacheSize: Long = DefaultCacheSize): (T => U) = {
    val cache = CacheBuilder.newBuilder.maximumSize(cacheSize).asInstanceOf[CacheBuilder[T, U]].build[T, U](
      new CacheLoader[T, U] {
        override def load(key: T): U = {
          logger.debug("Cache miss for key [%s]".format(key))
          return f(key)
        }
      })

    return { k =>
      {
        logger.debug("Checking cache for key [%s]".format(k))
        cache.get(k)
      }
    }
  }

}
