package com.wutsi.ecommerce.cart.dto

import javax.validation.constraints.Min
import kotlin.Int

public data class UpdateProductRequest(
    @get:Min(1)
    public val quantity: Int = 1
)
