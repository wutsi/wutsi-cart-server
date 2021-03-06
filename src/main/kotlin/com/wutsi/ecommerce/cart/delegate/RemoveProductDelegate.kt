package com.wutsi.ecommerce.cart.delegate

import com.wutsi.ecommerce.cart.dao.CartRepository
import com.wutsi.ecommerce.cart.dao.ProductRepository
import com.wutsi.ecommerce.cart.service.SecurityManager
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import javax.transaction.Transactional

@Service
class RemoveProductDelegate(
    private val cartDao: CartRepository,
    private val productDao: ProductRepository,
    private val securityManager: SecurityManager,
) : AbstractCartDelegate() {
    @Transactional
    fun invoke(merchantId: Long, productId: Long) {
        // Account
        val accountId = securityManager.accountId()!!
        logger.add("account_id", accountId)

        // Cart
        val cart = cartDao.findByMerchantIdAndAccountId(merchantId, accountId).orElse(null)
            ?: return
        logger.add("cart_id", cart.id)

        val opt = productDao.findByCartAndProductId(cart, productId)
        if (opt.isPresent) {
            // Delete the product
            productDao.delete(opt.get())

            // Update the cart
            cart.updated = OffsetDateTime.now()
            cartDao.save(cart)

            // Clear cache
            evictFromCache(cart)
        }
    }
}
