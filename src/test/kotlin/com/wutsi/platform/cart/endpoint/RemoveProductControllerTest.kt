package com.wutsi.platform.cart.endpoint

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.wutsi.platform.cart.dao.CartRepository
import com.wutsi.platform.cart.dao.ProductRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertFalse

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/RemoveProductController.sql"])
public class RemoveProductControllerTest : AbstractSecuredController() {
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
    public fun `Remove from Cart that doesn't exist`() {
        rest.delete(url(100, 1))
        verify(cache).evict(any())
    }

    @Test
    public fun `Remove Product from Cart`() {
        rest.delete(url(200, 1))

        val cart = cartDao.findByMerchantIdAndAccountId(200, USER_ID)
        val product = productDao.findByCartAndProductId(cart.get(), 1)
        assertFalse(product.isPresent)

        verify(cache).evict(any())
    }

    @Test
    public fun `Remove Delete Product from Cart`() {
        rest.delete(url(200, 9))

        val cart = cartDao.findByMerchantIdAndAccountId(200, USER_ID)
        val product = productDao.findByCartAndProductId(cart.get(), 9)
        assertFalse(product.isPresent)

        verify(cache).evict(any())
    }

    @Test
    public fun `Remove inexisting Product from Cart`() {
        rest.delete(url(200, 999))

        val cart = cartDao.findByMerchantIdAndAccountId(200, USER_ID)
        val product = productDao.findByCartAndProductId(cart.get(), 999)
        assertFalse(product.isPresent)

        verify(cache).evict(any())
    }

    private fun url(merchantId: Long, productId: Long): String =
        "http://localhost:$port/v1/carts/$merchantId/products/$productId"
}
