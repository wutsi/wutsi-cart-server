package com.wutsi.platform.cart.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.cart.dao.CartRepository
import com.wutsi.platform.cart.dao.ProductRepository
import com.wutsi.platform.cart.dto.UpdateProductRequest
import com.wutsi.platform.cart.error.ErrorURN
import com.wutsi.platform.core.error.ErrorResponse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/UpdateProductController.sql"])
public class UpdateProductControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @Autowired
    private lateinit var cartDao: CartRepository

    @Autowired
    private lateinit var productDao: ProductRepository

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
    public fun update() {
        // WHEN
        val request = UpdateProductRequest(quantity = 10)
        val response = rest.postForEntity(url(100, 1), request, Any::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        val cart = cartDao.findByMerchantIdAndAccountId(100, USER_ID)
        assertTrue(cart.isPresent)

        val product = productDao.findByCartAndProductId(cart.get(), 1)
        assertTrue(product.isPresent)
        assertEquals(request.quantity, product.get().quantity)

        verify(cache).evict(any())
    }

    @Test
    public fun updateWith0() {
        // WHEN
        val request = UpdateProductRequest(quantity = 0)
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url(200, 1), request, Any::class.java)
        }

        // THEN
        assertEquals(400, ex.rawStatusCode)
    }

    @Test
    public fun cartNotFound() {
        // WHEN
        val request = UpdateProductRequest(quantity = 1)
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url(9999, 1), request, Any::class.java)
        }

        // THEN
        assertEquals(404, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.CART_NOT_FOUND.urn, response.error.code)
    }

    @Test
    public fun productNotFound() {
        val request = UpdateProductRequest(quantity = 1)
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url(100, 99999), request, Any::class.java)
        }

        // THEN
        assertEquals(404, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.PRODUCT_NOT_FOUND.urn, response.error.code)
    }

    private fun url(merchantId: Long, productId: Long): String =
        "http://localhost:$port/v1/carts/$merchantId/products/$productId"
}
