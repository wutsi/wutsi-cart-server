package com.wutsi.ecommerce.cart.delegate

import com.wutsi.ecommerce.cart.dao.CartRepository
import com.wutsi.ecommerce.cart.dto.GetCartResponse
import com.wutsi.ecommerce.cart.error.ErrorURN
import com.wutsi.ecommerce.cart.service.SecurityManager
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.exception.NotFoundException
import com.wutsi.platform.core.error.exception.UnauthorizedException
import org.springframework.stereotype.Service

@Service
class GetCartDelegate(
    private val dao: CartRepository,
    private val securityManager: SecurityManager,
) : AbstractCartDelegate() {
    fun invoke(merchantId: Long): GetCartResponse {
        val accountId = securityManager.accountId()
            ?: throw UnauthorizedException(
                error = Error(
                    code = ErrorURN.CART_NOT_FOUND.urn
                )
            )
        logger.add("account_id", accountId)

        var response = getFromCache(accountId, merchantId)
        if (response != null) {
            logger.add("cache_hit", true)
            return response
        }

        // Cart
        val cart = dao.findByMerchantIdAndAccountId(merchantId, accountId)
            .orElseThrow {
                NotFoundException(
                    error = Error(
                        code = ErrorURN.CART_NOT_FOUND.urn
                    )
                )
            }
        response = GetCartResponse(
            cart = cart.toCart()
        )
        putInCache(response)

        logger.add("cart_id", cart.id)
        logger.add("cart_product_count", cart.products.size)
        logger.add("cart_product_ids", cart.products.map { it.id })
        return response
    }
}
