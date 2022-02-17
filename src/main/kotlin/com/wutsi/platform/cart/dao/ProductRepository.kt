package com.wutsi.platform.cart.dao

import com.wutsi.platform.cart.entity.CartEntity
import com.wutsi.platform.cart.entity.ProductEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ProductRepository : CrudRepository<ProductEntity, Long> {
    fun findByCart(cart: CartEntity): List<ProductEntity>
    fun findByCartAndProductId(cart: CartEntity, productId: Long): Optional<ProductEntity>
}
