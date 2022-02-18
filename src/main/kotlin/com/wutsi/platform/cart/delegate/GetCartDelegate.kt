package com.wutsi.platform.cart.`delegate`

import com.wutsi.platform.cart.dao.CartRepository
import com.wutsi.platform.cart.dto.Cart
import com.wutsi.platform.cart.dto.GetCartResponse
import com.wutsi.platform.cart.service.SecurityManager
import com.wutsi.platform.core.logging.KVLogger
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class GetCartDelegate(
    private val dao: CartRepository,
    private val securityManager: SecurityManager,
    private val logger: KVLogger,
) {
    @Cacheable(cacheNames = ["wutsi-cart"], keyGenerator = "cartKeyGenerator")
    fun invoke(merchantId: Long): GetCartResponse {
        // Account
        val accountId = securityManager.accountId()
        logger.add("account_id", accountId)

        // Cart
        val cart = dao.findByMerchantIdAndAccountId(merchantId, accountId)
        if (cart.isPresent)
            logger.add("cart_id", cart.get().id)
        else
            logger.add("cart_id", -1)

        return GetCartResponse(
            cart = cart
                .map { it.toCart() }
                .orElse(
                    Cart(
                        accountId = accountId,
                        merchantId = merchantId,
                    )
                )
        )
    }
}
