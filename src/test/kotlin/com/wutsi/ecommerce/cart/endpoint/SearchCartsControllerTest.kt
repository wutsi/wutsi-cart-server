package com.wutsi.ecommerce.cart.endpoint

import com.wutsi.ecommerce.cart.dto.SearchCartRequest
import com.wutsi.ecommerce.cart.dto.SearchCartResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/SearchCartsController.sql"])
class SearchCartsControllerTest : AbstractSecuredController() {
    @LocalServerPort
    val port: Int = 0

    private lateinit var url: String

    @BeforeEach
    override fun setUp() {
        super.setUp()

        url = "http://localhost:$port/v1/carts/search"
    }

    @Test
    fun `search by merchant-id`() {
        // WHEN
        val request = SearchCartRequest(
            merchantId = 100
        )
        val response = rest.postForEntity(url, request, SearchCartResponse::class.java)

        // THEN
        val carts = response.body!!.carts
        assertEquals(2, carts.size)
        assertEquals(10, carts[0].accountId)
        assertEquals(11, carts[1].accountId)
    }

    @Test
    fun `search by account-id`() {
        // WHEN
        val request = SearchCartRequest(
            accountId = 13
        )
        val response = rest.postForEntity(url, request, SearchCartResponse::class.java)

        // THEN
        val carts = response.body!!.carts
        assertEquals(1, carts.size)
        assertEquals(110, carts[0].merchantId)
    }
}
