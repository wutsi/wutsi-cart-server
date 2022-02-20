package com.wutsi.ecommerce.cart.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.OffsetDateTime

public data class Product(
    public val productId: Long = 0,
    public val quantity: Int = 1,
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val created: OffsetDateTime = OffsetDateTime.now(),
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val updated: OffsetDateTime = OffsetDateTime.now()
)
