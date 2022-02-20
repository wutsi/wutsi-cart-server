package com.wutsi.ecommerce.cart.endpoint

import com.wutsi.ecommerce.cart.delegate.GetCartDelegate
import com.wutsi.ecommerce.cart.dto.GetCartResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
public class GetCartController(
    private val `delegate`: GetCartDelegate
) {
    @GetMapping("/v1/carts/{merchant-id}")
    @PreAuthorize(value = "hasAuthority('cart-read')")
    public fun invoke(@PathVariable(name = "merchant-id") merchantId: Long): GetCartResponse =
        delegate.invoke(merchantId)
}
