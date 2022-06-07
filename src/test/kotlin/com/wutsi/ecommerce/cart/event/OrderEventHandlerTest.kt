package com.wutsi.ecommerce.cart.event

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.ecommerce.cart.delegate.EmptyCartDelegate
import com.wutsi.ecommerce.order.WutsiOrderApi
import com.wutsi.ecommerce.order.dto.GetOrderResponse
import com.wutsi.ecommerce.order.dto.Order
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class OrderEventHandlerTest {
    @MockBean
    private lateinit var orderApi: WutsiOrderApi

    @MockBean
    private lateinit var delegage: EmptyCartDelegate

    @Autowired
    private lateinit var eventHandler: OrderEventHandler

    private val order = Order(
        id = "43040394093",
        merchantId = 5555,
        accountId = 333
    )

    @BeforeEach
    fun setUp() {
        doReturn(GetOrderResponse(order)).whenever(orderApi).getOrder(any())
    }

    @Test
    fun onOrderDone() {
        // WHEN
        eventHandler.onOrderDone(order.id)

        // THEN
        verify(delegage).invoke(order.merchantId, order.accountId)
    }
}
