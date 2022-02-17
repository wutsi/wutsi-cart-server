package com.wutsi.platform.cart.delegate

import com.wutsi.platform.cart.dto.Cart
import com.wutsi.platform.cart.dto.Product
import com.wutsi.platform.cart.entity.CartEntity
import com.wutsi.platform.cart.entity.ProductEntity

fun CartEntity.toCart() = Cart(
    created = this.created,
    accountId = this.accountId,
    merchantId = this.merchantId,
    products = this.products.map { it.toProduct() },
)

fun ProductEntity.toProduct() = Product(
    productId = this.productId,
    quantity = this.quantity,
    created = this.created,
    updated = this.updated
)
