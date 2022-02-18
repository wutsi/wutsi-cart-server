package com.wutsi.platform.cart.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.cart.dto.GetCartResponse
import com.wutsi.platform.cart.error.ErrorURN
import com.wutsi.platform.core.error.ErrorResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/GetCartController.sql"])
public class GetCartControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @MockBean
    private lateinit var cacheManager: CacheManager

    @MockBean
    private lateinit var cache: Cache

    @BeforeEach
    override fun setUp() {
        super.setUp()

        doReturn(cache).whenever(cacheManager).getCache(any())
    }

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

        verify(cache).put(any(), any())
    }

    @Test
    public fun cartNotFound() {
        val ex = assertThrows<HttpClientErrorException> {
            rest.getForEntity(url(100), GetCartResponse::class.java)
        }

        // THEN
        assertEquals(404, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.CART_NOT_FOUND.urn, response.error.code)
    }

    private fun url(merchantId: Long): String =
        "http://localhost:$port/v1/carts/$merchantId"
}
