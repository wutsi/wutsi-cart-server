package com.wutsi.ecommerce.cart.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.ecommerce.order.event.EventURN
import com.wutsi.ecommerce.order.event.OrderEventPayload
import com.wutsi.platform.core.stream.Event
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class EventHandlerTest {
    @Autowired
    private lateinit var eventHandler: EventHandler

    @MockBean
    private lateinit var orderEventHandler: OrderEventHandler

    @Test
    fun onEvent() {
        // GIVEN
        val orderId = "4034093409"
        val event = Event(
            id = EventURN.ORDER_DONE.urn,
            payload = ObjectMapper().writeValueAsString(
                OrderEventPayload(
                    orderId = orderId
                )
            )
        )

        // WHEN
        eventHandler.onEvent(event)

        // THEN
        orderEventHandler.onOrderDone(orderId)
    }
}
