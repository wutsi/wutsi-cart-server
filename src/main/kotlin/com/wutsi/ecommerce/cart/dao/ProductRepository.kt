package com.wutsi.ecommerce.cart.dao

import com.wutsi.ecommerce.cart.entity.CartEntity
import com.wutsi.ecommerce.cart.entity.ProductEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ProductRepository : CrudRepository<ProductEntity, Long> {
    fun findByCart(cart: CartEntity): List<ProductEntity>
    fun findByCartAndProductId(cart: CartEntity, productId: Long): Optional<ProductEntity>
}
