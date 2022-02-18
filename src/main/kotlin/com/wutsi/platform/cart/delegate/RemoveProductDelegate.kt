package com.wutsi.platform.cart.`delegate`

import com.wutsi.platform.cart.dao.CartRepository
import com.wutsi.platform.cart.dao.ProductRepository
import com.wutsi.platform.cart.service.SecurityManager
import com.wutsi.platform.core.logging.KVLogger
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class RemoveProductDelegate(
    private val cartDao: CartRepository,
    private val productDao: ProductRepository,
    private val securityManager: SecurityManager,
    private val logger: KVLogger,
) {
    @CacheEvict(cacheNames = ["wutsi-cart"], keyGenerator = "cartKeyGenerator")
    @Transactional
    fun invoke(merchantId: Long, productId: Long) {
        // Account
        val accountId = securityManager.accountId()
        logger.add("account_id", accountId)

        // Cart
        val cart = cartDao.findByMerchantIdAndAccountId(merchantId, accountId)
        if (cart.isEmpty)
            return
        logger.add("cart_id", cart.get().id)

        val opt = productDao.findByCartAndProductId(cart.get(), productId)
        if (opt.isPresent)
            productDao.delete(opt.get())
    }
}
