package com.wutsi.ecommerce.cart.dto

import kotlin.Int
import kotlin.Long

public data class SearchCartRequest(
    public val merchantId: Long? = null,
    public val accountId: Long? = null,
    public val limit: Int = 30,
    public val offset: Int = 0
)
