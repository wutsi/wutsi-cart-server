package com.wutsi.platform.cart.`delegate`

import com.wutsi.platform.cart.dao.CartRepository
import com.wutsi.platform.cart.dao.ProductRepository
import com.wutsi.platform.cart.dto.SaveProductRequest
import com.wutsi.platform.cart.entity.CartEntity
import com.wutsi.platform.cart.entity.ProductEntity
import com.wutsi.platform.cart.service.SecurityManager
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import javax.transaction.Transactional

@Service
public class SaveProductDelegate(
    private val cartDao: CartRepository,
    private val productDao: ProductRepository,
    private val securityManager: SecurityManager
) {
    @CacheEvict(cacheNames = ["wutsi-cart"], keyGenerator = "cartKeyGenerator")
    @Transactional
    public fun invoke(
        merchantId: Long,
        productId: Long,
        request: SaveProductRequest
    ) {
        val cart = getCart(merchantId)

        val product = getProduct(productId, cart)
        product.quantity = request.quantity
        productDao.save(product)
    }

    fun getCart(merchantId: Long): CartEntity {
        val accountId = securityManager.accountId()
        val opt = cartDao.findByMerchantIdAndAccountId(merchantId, accountId)
        if (opt.isPresent)
            return opt.get()

        return cartDao.save(
            CartEntity(
                merchantId = merchantId,
                accountId = accountId,
                tenantId = securityManager.tenantId(),
                created = OffsetDateTime.now()
            )
        )
    }

    fun getProduct(productId: Long, cart: CartEntity): ProductEntity {
        val opt = productDao.findByCartAndProductId(cart, productId)
        if (opt.isPresent)
            return opt.get()

        return ProductEntity(
            productId = productId,
            cart = cart,
            quantity = 1
        )
    }
}
