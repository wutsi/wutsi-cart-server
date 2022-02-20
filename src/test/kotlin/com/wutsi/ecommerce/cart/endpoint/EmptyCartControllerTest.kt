package com.wutsi.ecommerce.cart.endpoint

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.ecommerce.cart.dao.CartRepository
import com.wutsi.ecommerce.cart.dao.ProductRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
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
    public fun `empty cart`() {
        rest.delete(url(200))

        val cart = cartDao.findByMerchantIdAndAccountId(200, AbstractSecuredController.USER_ID)
        val product = productDao.findByCart(cart.get())
        assertTrue(product.isEmpty())

        verify(cache).evict(any())
    }

    private fun url(merchantId: Long): String =
        "http://localhost:$port/v1/carts/$merchantId/products"
}
