package com.wutsi.platform.cart.entity

import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "T_CART")
data class CartEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val tenantId: Long = -1,
    val merchantId: Long = -1,
    val accountId: Long = -1,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cart")
    val products: MutableList<ProductEntity> = mutableListOf(),

    val created: OffsetDateTime = OffsetDateTime.now()
)
