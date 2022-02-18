package com.wutsi.platform.cart.`delegate`

import com.wutsi.platform.cart.dao.CartRepository
import com.wutsi.platform.cart.dao.ProductRepository
import com.wutsi.platform.cart.dto.UpdateProductRequest
import com.wutsi.platform.cart.error.ErrorURN
import com.wutsi.platform.cart.service.SecurityManager
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.exception.NotFoundException
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UpdateProductDelegate(
    private val cartDao: CartRepository,
    private val productDao: ProductRepository,
    private val securityManager: SecurityManager
) {
    @CacheEvict(cacheNames = ["wutsi-cart"], keyGenerator = "cartKeyGenerator")
    @Transactional
    fun invoke(
        merchantId: Long,
        productId: Long,
        request: UpdateProductRequest
    ) {
        val accountId = securityManager.accountId()
        val cart = cartDao.findByMerchantIdAndAccountId(merchantId, accountId)
            .orElseThrow {
                NotFoundException(
                    error = Error(
                        code = ErrorURN.CART_NOT_FOUND.urn,
                    )
                )
            }

        val product = productDao.findByCartAndProductId(cart, productId)
            .orElseThrow {
                NotFoundException(
                    error = Error(
                        code = ErrorURN.PRODUCT_NOT_FOUND.urn,
                    )
                )
            }

        product.quantity = request.quantity
        productDao.save(product)
    }
}
