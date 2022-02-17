package com.wutsi.platform.cart.service

import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.stereotype.Service
import java.lang.reflect.Method

/**
 * Cache KeyGenerator for the Cart.
 * The key generated has the following format <code>cart-MERCHANT_ID-ACCOUNT_ID</code>:
 *  - MERCHANT_ID: ID of the merchant
 *  - ACCOUNT_ID: ID of the account
 */
@Service
class CartKeyGenerator(private val securityManager: SecurityManager) : KeyGenerator {
    override fun generate(target: Any, method: Method, vararg arg: Any?): Any {
        val accountId = securityManager.accountId()
        val merchantId = arg[0].toString().toLong()
        return "cart-$merchantId-$accountId"
    }
}
