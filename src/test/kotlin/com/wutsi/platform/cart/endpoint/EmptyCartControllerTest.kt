package com.wutsi.platform.cart.endpoint

import com.wutsi.platform.cart.dao.CartRepository
import com.wutsi.platform.cart.dao.ProductRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/EmptyCartController.sql"])
public class EmptyCartControllerTest : AbstractSecuredController() {
    @LocalServerPort
    public val port: Int = 0

    @Autowired
    private lateinit var cartDao: CartRepository

    @Autowired
    private lateinit var productDao: ProductRepository

    @Test
    public fun `empty cart`() {
        rest.delete(url(200))

        val cart = cartDao.findByMerchantIdAndAccountId(200, AbstractSecuredController.USER_ID)
        val product = productDao.findByCart(cart.get())
        assertTrue(product.isEmpty())
    }

    private fun url(merchantId: Long): String =
        "http://localhost:$port/v1/carts/$merchantId/products"
}
