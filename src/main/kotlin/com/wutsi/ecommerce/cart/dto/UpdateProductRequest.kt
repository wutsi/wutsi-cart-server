package com.wutsi.ecommerce.cart.dto

import javax.validation.constraints.Min

public data class UpdateProductRequest(
    @get:Min(1)
    public val quantity: Int = 1
)
