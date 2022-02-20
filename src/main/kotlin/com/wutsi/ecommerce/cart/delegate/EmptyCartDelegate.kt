package com.wutsi.ecommerce.cart.delegate

import com.wutsi.ecommerce.cart.dao.CartRepository
import com.wutsi.ecommerce.cart.dao.ProductRepository
import com.wutsi.ecommerce.cart.service.SecurityManager
import com.wutsi.platform.core.logging.KVLogger
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class EmptyCartDelegate(
    private val cartDao: CartRepository,
    private val productDao: ProductRepository,
    private val securityManager: SecurityManager,
    private val logger: KVLogger,
) {
    @CacheEvict(cacheNames = ["wutsi-cart"], keyGenerator = "cartKeyGenerator")
    @Transactional
    fun invoke(merchantId: Long) {
        // Account
        val accountId = securityManager.accountId()
        logger.add("account_id", accountId)

        // Cart
        val cart = cartDao.findByMerchantIdAndAccountId(merchantId, accountId)
        if (cart.isEmpty)
            return
        logger.add("cart_id", cart.get().id)

        val products = productDao.findByCart(cart.get())
        productDao.deleteAll(products)
    }
}
