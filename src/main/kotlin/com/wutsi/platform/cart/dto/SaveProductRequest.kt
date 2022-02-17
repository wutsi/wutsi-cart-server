package com.wutsi.platform.cart.dto

import javax.validation.constraints.Min
import kotlin.Int

public data class SaveProductRequest(
    @get:Min(1)
    public val quantity: Int = 1
)
