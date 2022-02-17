package com.wutsi.platform.cart.endpoint

import com.wutsi.platform.cart.dao.CartRepository
import com.wutsi.platform.cart.dao.ProductRepository
import com.wutsi.platform.cart.dto.SaveProductRequest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/SaveProductController.sql"])
public class SaveProductControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @Autowired
    private lateinit var cartDao: CartRepository

    @Autowired
    private lateinit var productDao: ProductRepository

    @Test
    public fun `Create Cart`() {
        // WHEN
        val request = SaveProductRequest(quantity = 3)
        val response = rest.postForEntity(url(100, 1), request, Any::class.java)

        // THEN
        val cart = cartDao.findByMerchantIdAndAccountId(100, USER_ID)
        assertTrue(cart.isPresent)

        val product = productDao.findByCartAndProductId(cart.get(), 1)
        assertTrue(product.isPresent)
        assertEquals(request.quantity, product.get().quantity)
    }

    @Test
    public fun `Update product`() {
        // WHEN
        val request = SaveProductRequest(quantity = 5)
        val response = rest.postForEntity(url(200, 1), request, Any::class.java)

        // THEN
        val cart = cartDao.findByMerchantIdAndAccountId(200, USER_ID)
        assertTrue(cart.isPresent)

        val product = productDao.findByCartAndProductId(cart.get(), 1)
        assertTrue(product.isPresent)
        assertEquals(request.quantity, product.get().quantity)
    }

    @Test
    public fun `Add product`() {
        // WHEN
        val request = SaveProductRequest(quantity = 1)
        val response = rest.postForEntity(url(200, 2), request, Any::class.java)

        // THEN
        val cart = cartDao.findByMerchantIdAndAccountId(200, USER_ID)
        assertTrue(cart.isPresent)

        val product = productDao.findByCartAndProductId(cart.get(), 2)
        assertTrue(product.isPresent)
        assertEquals(request.quantity, product.get().quantity)
    }

    @Test
    public fun `Undelete product`() {
        // WHEN
        val request = SaveProductRequest(quantity = 1)
        val response = rest.postForEntity(url(200, 9), request, Any::class.java)

        // THEN
        val cart = cartDao.findByMerchantIdAndAccountId(200, USER_ID)
        assertTrue(cart.isPresent)

        val product = productDao.findByCartAndProductId(cart.get(), 9)
        assertTrue(product.isPresent)
        assertEquals(request.quantity, product.get().quantity)
    }

    private fun url(merchantId: Long, productId: Long): String =
        "http://localhost:$port/v1/carts/$merchantId/products/$productId"
}
