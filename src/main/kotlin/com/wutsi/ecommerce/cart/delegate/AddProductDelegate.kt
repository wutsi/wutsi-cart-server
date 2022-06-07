package com.wutsi.ecommerce.cart.delegate

import com.wutsi.ecommerce.cart.dao.CartRepository
import com.wutsi.ecommerce.cart.dao.ProductRepository
import com.wutsi.ecommerce.cart.dto.AddProductRequest
import com.wutsi.ecommerce.cart.entity.CartEntity
import com.wutsi.ecommerce.cart.entity.ProductEntity
import com.wutsi.ecommerce.cart.service.SecurityManager
import com.wutsi.platform.core.logging.KVLogger
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import javax.transaction.Transactional

@Service
class AddProductDelegate(
    private val cartDao: CartRepository,
    private val productDao: ProductRepository,
    private val securityManager: SecurityManager,
    private val logger: KVLogger,
) {
    @CacheEvict(cacheNames = ["wutsi-cart"], keyGenerator = "cartKeyGenerator")
    @Transactional
    fun invoke(merchantId: Long, request: AddProductRequest) {
        // Account
        val accountId = securityManager.accountId()!!
        logger.add("account_id", accountId)

        // Cart
        val cart = getCart(merchantId, accountId)

        // Product
        val product = getProduct(request.productId, cart)
        product.quantity += request.quantity
        productDao.save(product)

        logger.add("cart_id", cart.id)
        logger.add("product_id", product.id)
        logger.add("quantity", product.quantity)
    }

    private fun getCart(merchantId: Long, accountId: Long): CartEntity {
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

    private fun getProduct(productId: Long, cart: CartEntity): ProductEntity {
        val opt = productDao.findByCartAndProductId(cart, productId)
        if (opt.isPresent)
            return opt.get()

        return ProductEntity(
            productId = productId,
            cart = cart,
            quantity = 0
        )
    }
}
