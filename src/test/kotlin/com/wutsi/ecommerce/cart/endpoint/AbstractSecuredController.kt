package com.wutsi.ecommerce.cart.endpoint

import com.wutsi.platform.core.security.SubjectType
import com.wutsi.platform.core.security.SubjectType.USER
import com.wutsi.platform.core.security.spring.SpringAuthorizationRequestInterceptor
import com.wutsi.platform.core.security.spring.jwt.JWTBuilder
import com.wutsi.platform.core.test.TestRSAKeyProvider
import com.wutsi.platform.core.test.TestTokenProvider
import com.wutsi.platform.core.test.TestTracingContext
import com.wutsi.platform.core.tracing.TracingContext
import com.wutsi.platform.core.tracing.spring.SpringTracingRequestInterceptor
import org.junit.jupiter.api.BeforeEach
import org.springframework.web.client.RestTemplate

abstract class AbstractSecuredController {
    companion object {
        const val USER_ID = 1L
        const val TENANT_ID = 1L
    }

    private lateinit var tracingContext: TracingContext

//    @MockBean
//    protected lateinit var tenantApi: WutsiTenantApi

    protected lateinit var rest: RestTemplate

    @BeforeEach
    open fun setUp() {
        tracingContext = TestTracingContext(tenantId = TENANT_ID.toString())

//        val tenant = Tenant(
//            id = 1,
//            name = "test",
//            logos = listOf(
//                Logo(type = "PICTORIAL", url = "http://www.goole.com/images/1.png")
//            ),
//            countries = listOf("CM"),
//            languages = listOf("en", "fr"),
//            currency = "XAF",
//            domainName = "www.wutsi.com",
//        )
//        doReturn(GetTenantResponse(tenant)).whenever(tenantApi).getTenant(any())

        rest = createResTemplate()
    }

    protected fun createResTemplate(
        scope: List<String> = listOf(
            "cart-manage",
            "cart-read",
        ),
        subjectId: Long = USER_ID,
        subjectType: SubjectType = USER,
        admin: Boolean = false
    ): RestTemplate {
        val rest = RestTemplate()

        val tokenProvider = TestTokenProvider(
            JWTBuilder(
                subject = subjectId.toString(),
                subjectType = subjectType,
                scope = scope,
                keyProvider = TestRSAKeyProvider(),
                admin = admin
            ).build()
        )

        rest.interceptors.add(SpringTracingRequestInterceptor(tracingContext))
        rest.interceptors.add(SpringAuthorizationRequestInterceptor(tokenProvider))
        return rest
    }
}
