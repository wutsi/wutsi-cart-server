package com.wutsi.ecommerce.cart.service

import org.springframework.stereotype.Service

/**
 * Cache KeyGenerator for the Cart.
 * The key generated has the following format <code>cart-MERCHANT_ID-ACCOUNT_ID</code>:
 *  - MERCHANT_ID: ID of the merchant
 *  - ACCOUNT_ID: ID of the account
 */
@Service
class CacheKeyGenerator {
    fun generate(accountId: Long, merchantId: Long): String =
        "cart-$merchantId-$accountId"
}
