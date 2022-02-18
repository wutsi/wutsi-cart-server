package com.wutsi.platform.cart.endpoint

import com.wutsi.platform.cart.dao.CartRepository
import com.wutsi.platform.cart.dao.ProductRepository
import com.wutsi.platform.cart.dto.AddProductRequest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
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
