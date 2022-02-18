package com.wutsi.platform.cart.endpoint

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.cart.dao.CartRepository
import com.wutsi.platform.cart.dao.ProductRepository
import com.wutsi.platform.cart.dto.AddProductRequest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/AddProductController.sql"])
public class AddProductControllerTest : AbstractSecuredController() {
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
    public fun createCart() {
        // WHEN
        val request = AddProductRequest(quantity = 3, productId = 1L)
        val response = rest.postForEntity(url(100), request, Any::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        val cart = cartDao.findByMerchantIdAndAccountId(100, USER_ID)
        assertTrue(cart.isPresent)

        val product = productDao.findByCartAndProductId(cart.get(), request.productId)
        assertTrue(product.isPresent)
        assertEquals(request.quantity, product.get().quantity)

        verify(cache).evict(any())
    }

    @Test
    public fun addProduct() {
        // WHEN
        val request = AddProductRequest(quantity = 1, productId = 10L)
        val response = rest.postForEntity(url(100), request, Any::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        val cart = cartDao.findByMerchantIdAndAccountId(100, USER_ID)
        assertTrue(cart.isPresent)

        val product = productDao.findByCartAndProductId(cart.get(), request.productId)
        assertTrue(product.isPresent)
        assertEquals(request.quantity, product.get().quantity)

        verify(cache).evict(any())
    }

    @Test
    public fun incrementProducts() {
        // WHEN
        val request = AddProductRequest(quantity = 3, productId = 2L)
        val response = rest.postForEntity(url(100), request, Any::class.java)

        // THEN
        assertEquals(200, response.statusCodeValue)

        val cart = cartDao.findByMerchantIdAndAccountId(100, USER_ID)
        assertTrue(cart.isPresent)

        val product = productDao.findByCartAndProductId(cart.get(), request.productId)
        assertTrue(product.isPresent)
        assertEquals(5, product.get().quantity)
    }

    private fun url(merchantId: Long): String =
        "http://localhost:$port/v1/carts/$merchantId/products"
}
