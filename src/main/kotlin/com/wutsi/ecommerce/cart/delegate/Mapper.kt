package com.wutsi.ecommerce.cart.delegate

import com.wutsi.ecommerce.cart.dto.Cart
import com.wutsi.ecommerce.cart.dto.CartSummary
import com.wutsi.ecommerce.cart.dto.Product
import com.wutsi.ecommerce.cart.entity.CartEntity
import com.wutsi.ecommerce.cart.entity.ProductEntity

fun CartEntity.toCart() = Cart(
    created = this.created,
    updated = this.updated,
    accountId = this.accountId,
    merchantId = this.merchantId,
    products = this.products.map { it.toProduct() },
)

fun CartEntity.toCartSummary() = CartSummary(
    created = this.created,
    updated = this.updated,
    accountId = this.accountId,
    merchantId = this.merchantId,
)

fun ProductEntity.toProduct() = Product(
    productId = this.productId,
    quantity = this.quantity,
    created = this.created,
    updated = this.updated
)
