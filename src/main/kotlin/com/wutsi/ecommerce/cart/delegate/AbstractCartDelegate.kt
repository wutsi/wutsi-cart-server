package com.wutsi.ecommerce.cart.delegate

import com.wutsi.ecommerce.cart.dto.GetCartResponse
import com.wutsi.ecommerce.cart.entity.CartEntity
import com.wutsi.ecommerce.cart.service.CacheKeyGenerator
import com.wutsi.platform.core.logging.KVLogger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.Cache
import org.springframework.stereotype.Service

@Service
class AbstractCartDelegate {
    @Autowired
    protected lateinit var logger: KVLogger

    @Autowired
    private lateinit var cache: Cache

    @Autowired
    private lateinit var cacheKeyGenerator: CacheKeyGenerator

    fun evictFromCache(cart: CartEntity) {
        val key = cacheKeyGenerator.generate(cart.accountId, cart.merchantId)
        logger.add("cache_key", key)
        try {
            cache.evict(key)
        } catch (ex: Exception) {
            LoggerFactory.getLogger(this::class.java).warn("Unable to evict from cache $key", ex)
        }
    }

    fun putInCache(response: GetCartResponse) {
        val key = cacheKeyGenerator.generate(response.cart.accountId, response.cart.merchantId)
        logger.add("cache_key", key)
        try {
            cache.put(key, response)
        } catch (ex: Exception) {
            LoggerFactory.getLogger(this::class.java).warn("Unable to put into cache $key", ex)
        }
    }

    fun getFromCache(accountId: Long, merchantId: Long): GetCartResponse? {
        val key = cacheKeyGenerator.generate(accountId, merchantId)
        logger.add("cache_key", key)
        try {
            return cache.get(key, GetCartResponse::class.java)
        } catch (ex: Exception) {
            LoggerFactory.getLogger(this::class.java).warn("Unable to resolve from cache $key", ex)
            return null
        }
    }
}
