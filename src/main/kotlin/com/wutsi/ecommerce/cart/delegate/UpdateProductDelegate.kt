package com.wutsi.ecommerce.cart.delegate

import com.wutsi.ecommerce.cart.dao.CartRepository
import com.wutsi.ecommerce.cart.dao.ProductRepository
import com.wutsi.ecommerce.cart.dto.UpdateProductRequest
import com.wutsi.ecommerce.cart.error.ErrorURN
import com.wutsi.ecommerce.cart.service.SecurityManager
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.exception.NotFoundException
import com.wutsi.platform.core.logging.KVLogger
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import javax.transaction.Transactional

@Service
class UpdateProductDelegate(
    private val cartDao: CartRepository,
    private val productDao: ProductRepository,
    private val securityManager: SecurityManager,
    private val logger: KVLogger,
) {
    @CacheEvict(cacheNames = ["wutsi-cart"], keyGenerator = "cartKeyGenerator")
    @Transactional
    fun invoke(
        merchantId: Long,
        productId: Long,
        request: UpdateProductRequest
    ) {
        // Account
        val accountId = securityManager.accountId()!!
        logger.add("account_id", accountId)

        // Cart
        val cart = cartDao.findByMerchantIdAndAccountId(merchantId, accountId)
            .orElseThrow {
                NotFoundException(
                    error = Error(
                        code = ErrorURN.CART_NOT_FOUND.urn,
                    )
                )
            }
        logger.add("cart_id", cart.id)

        // Product
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
        logger.add("product_id", product.id)
        logger.add("quantity", product.quantity)

        // Cart
        cart.updated = OffsetDateTime.now()
        cartDao.save(cart)
    }
}
