package com.wutsi.ecommerce.cart.delegate

import com.wutsi.ecommerce.cart.dao.CartRepository
import com.wutsi.ecommerce.cart.dao.ProductRepository
import com.wutsi.ecommerce.cart.service.SecurityManager
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import javax.transaction.Transactional

@Service
class EmptyCartDelegate(
    private val cartDao: CartRepository,
    private val productDao: ProductRepository,
    private val securityManager: SecurityManager,
) : AbstractCartDelegate() {
    @Transactional
    fun invoke(merchantId: Long, customerId: Long? = null) {
        logger.add("merchant_id", merchantId)

        // Account
        val accountId = customerId
            ?: securityManager.accountId()
            ?: return
        logger.add("account_id", accountId)

        // Cart
        val cart = cartDao.findByMerchantIdAndAccountId(merchantId, accountId)
            .orElse(null)
            ?: return
        logger.add("cart_id", cart.id)

        val products = productDao.findByCart(cart)
        logger.add("product_count", products.size)
        if (products.isNotEmpty()) {
            // Empty
            productDao.deleteAll(products)

            // Update the cart
            cart.updated = OffsetDateTime.now()
            cartDao.save(cart)

            // Update cache
            evictFromCache(cart)
        }
    }
}
