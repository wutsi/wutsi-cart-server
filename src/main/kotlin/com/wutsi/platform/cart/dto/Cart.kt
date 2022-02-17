package com.wutsi.platform.cart.dto

import org.springframework.format.`annotation`.DateTimeFormat
import java.time.OffsetDateTime
import kotlin.Long
import kotlin.collections.List

public data class Cart(
    public val merchantId: Long = 0,
    public val accountId: Long = 0,
    public val products: List<Product> = emptyList(),
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val created: OffsetDateTime = OffsetDateTime.now()
)
