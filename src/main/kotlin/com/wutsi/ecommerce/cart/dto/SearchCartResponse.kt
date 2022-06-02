package com.wutsi.ecommerce.cart.dto

import kotlin.collections.List

public data class SearchCartResponse(
    public val carts: List<CartSummary> = emptyList()
)
