package com.wutsi.platform.cart.endpoint

import com.wutsi.platform.cart.dto.GetCartResponse
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/GetCartController.sql"])
public class GetCartControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @Test
    public fun get() {
        val response = rest.getForEntity(url(200), GetCartResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        val cart = response.body!!.cart
        assertEquals(200L, cart.merchantId)
        assertEquals(USER_ID, cart.accountId)
        assertEquals(2, cart.products.size)
        assertEquals(1L, cart.products[0].productId)
        assertEquals(2L, cart.products[1].productId)
    }

    @Test
    public fun cartNotFound() {
        val response = rest.getForEntity(url(100), GetCartResponse::class.java)

        assertEquals(200, response.statusCodeValue)

        val cart = response.body!!.cart
        assertEquals(100L, cart.merchantId)
        assertEquals(USER_ID, cart.accountId)
        assertEquals(0, cart.products.size)
    }

    private fun url(merchantId: Long): String =
        "http://localhost:$port/v1/carts/$merchantId"
}
