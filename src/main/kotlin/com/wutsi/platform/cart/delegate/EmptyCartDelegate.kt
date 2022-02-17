package com.wutsi.platform.cart.`delegate`

import com.wutsi.platform.cart.dao.CartRepository
import com.wutsi.platform.cart.dao.ProductRepository
import com.wutsi.platform.cart.service.SecurityManager
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
public class EmptyCartDelegate(
    private val cartDao: CartRepository,
    private val productDao: ProductRepository,
    private val securityManager: SecurityManager
) {
    @CacheEvict(cacheNames = ["wutsi-cart"], keyGenerator = "cartKeyGenerator")
    @Transactional
    public fun invoke(merchantId: Long) {
        val accountId = securityManager.accountId()
        val cart = cartDao.findByMerchantIdAndAccountId(merchantId, accountId)
        if (cart.isEmpty)
            return

        val products = productDao.findByCart(cart.get())
        productDao.deleteAll(products)
    }
}
