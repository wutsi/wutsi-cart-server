package com.wutsi.platform.cart.`delegate`

import com.wutsi.platform.cart.dao.CartRepository
import com.wutsi.platform.cart.dto.Cart
import com.wutsi.platform.cart.dto.GetCartResponse
import com.wutsi.platform.cart.service.SecurityManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
public class GetCartDelegate(
    private val dao: CartRepository,
    private val securityManager: SecurityManager
) {
    @Cacheable(cacheNames = ["wutsi-cart"], keyGenerator = "cartKeyGenerator")
    public fun invoke(merchantId: Long): GetCartResponse {
        val accountId = securityManager.accountId()
        val cart = dao.findByMerchantIdAndAccountId(merchantId, accountId)

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
