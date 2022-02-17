package com.wutsi.platform.cart.dao

import com.wutsi.platform.cart.entity.CartEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface CartRepository : CrudRepository<CartEntity, Long> {
    fun findByMerchantIdAndAccountId(merchantId: Long, accountId: Long): Optional<CartEntity>
}
