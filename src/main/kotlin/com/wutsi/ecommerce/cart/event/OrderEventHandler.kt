package com.wutsi.ecommerce.cart.event

import com.wutsi.ecommerce.cart.delegate.EmptyCartDelegate
import com.wutsi.ecommerce.order.WutsiOrderApi
import com.wutsi.platform.core.logging.KVLogger
import org.springframework.stereotype.Service

@Service
class OrderEventHandler(
    private val orderApi: WutsiOrderApi,
    private val delegate: EmptyCartDelegate,
    private val logger: KVLogger,
) {
    fun onOrderOpened(orderId: String) {
        val order = orderApi.getOrder(orderId).order
        delegate.invoke(order.merchantId, order.accountId)
    }
}
