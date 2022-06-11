package com.wutsi.ecommerce.cart.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.ecommerce.order.event.EventURN
import com.wutsi.ecommerce.order.event.OrderEventPayload
import com.wutsi.platform.core.stream.Event
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class EventHandler(
    private val objectMapper: ObjectMapper,
    private val orderEventHandler: OrderEventHandler,
) {
    @EventListener
    fun onEvent(event: Event) {
        if (EventURN.ORDER_OPENED.urn == event.type) {
            val payload = objectMapper.readValue(event.payload, OrderEventPayload::class.java)
            orderEventHandler.onOrderOpened(payload.orderId)
        }
    }
}
