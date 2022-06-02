package com.wutsi.ecommerce.cart.`delegate`

import com.wutsi.ecommerce.cart.dto.SearchCartRequest
import com.wutsi.ecommerce.cart.dto.SearchCartResponse
import com.wutsi.ecommerce.cart.entity.CartEntity
import com.wutsi.ecommerce.cart.service.SecurityManager
import com.wutsi.platform.core.logging.KVLogger
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.persistence.Query

@Service
class SearchCartsDelegate(
    private var em: EntityManager,
    private val securityManager: SecurityManager,
    private val logger: KVLogger,
) {
    fun invoke(request: SearchCartRequest): SearchCartResponse {
        logger.add("offset", request.offset)
        logger.add("limit", request.limit)
        logger.add("account_id", request.accountId)
        logger.add("merchant_id", request.merchantId)

        val carts = search(request)
        logger.add("count", carts.size)
        return SearchCartResponse(
            carts = carts.map { it.toCartSummary() }
        )
    }

    fun search(request: SearchCartRequest): List<CartEntity> {
        val query = em.createQuery(sql(request))
        parameters(request, query)
        return query
            .setFirstResult(request.offset)
            .setMaxResults(request.limit)
            .resultList as List<CartEntity>
    }

    private fun sql(request: SearchCartRequest): String {
        val select = select()
        val where = where(request)
        val orderBy = orderBy()
        return if (where.isNullOrEmpty())
            select
        else
            "$select WHERE $where ORDER BY $orderBy"
    }

    private fun select(): String =
        "SELECT C FROM CartEntity C"

    private fun where(request: SearchCartRequest): String {
        val criteria = mutableListOf<String>()

        criteria.add("C.tenantId = :tenant_id")

        if (request.accountId != null)
            criteria.add("C.accountId = :account_id")
        if (request.merchantId != null)
            criteria.add("C.merchantId = :merchant_id")

        // Ensure that cart is not empty
        criteria.add("C.products IS NOT EMPTY")

        return criteria.joinToString(separator = " AND ")
    }

    private fun orderBy(): String =
        "C.updated DESC"

    private fun parameters(request: SearchCartRequest, query: Query) {
        query.setParameter("tenant_id", securityManager.tenantId())

        if (request.accountId != null)
            query.setParameter("account_id", request.accountId)

        if (request.merchantId != null)
            query.setParameter("merchant_id", request.merchantId)
    }
}
