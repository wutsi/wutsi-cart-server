package com.wutsi.ecommerce.cart.error

enum class ErrorURN(val urn: String) {
    PRODUCT_NOT_FOUND("urn:wutsi:error:cart:product-not-found"),
    CART_NOT_FOUND("urn:wutsi:error:cart:cart-not-found"),
}
