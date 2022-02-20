package com.wutsi.ecommerce.cart.endpoint

import com.wutsi.ecommerce.cart.delegate.EmptyCartDelegate
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
public class EmptyCartController(
    private val `delegate`: EmptyCartDelegate
) {
    @DeleteMapping("/v1/carts/{merchant-id}/products")
    @PreAuthorize(value = "hasAuthority('cart-read')")
    public fun invoke(@PathVariable(name = "merchant-id") merchantId: Long) {
        delegate.invoke(merchantId)
    }
}
