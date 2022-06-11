package com.wutsi.ecommerce.cart.service

import com.wutsi.platform.core.security.SubjectType
import com.wutsi.platform.core.security.WutsiPrincipal
import com.wutsi.platform.core.tracing.TracingContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SecurityManager(
    private val tracingContext: TracingContext
) {
    fun tenantId(): Long? =
        tracingContext.tenantId()?.toLong()

    fun accountId(): Long? =
        if (principal()?.type == SubjectType.USER)
            principal()?.id?.toLong()
        else
            null

    private fun principal(): WutsiPrincipal? {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: return null

        val principal = authentication.principal
        return principal as WutsiPrincipal
    }
}
