package com.wutsi.ecommerce.cart.endpoint

import com.wutsi.ecommerce.cart.`delegate`.RemoveProductDelegate
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.DeleteMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController
import kotlin.Long

@RestController
public class RemoveProductController(
    private val `delegate`: RemoveProductDelegate
) {
    @DeleteMapping("/v1/carts/{merchant-id}/products/{product-id}")
    @PreAuthorize(value = "hasAuthority('cart-manage')")
    public fun invoke(
        @PathVariable(name = "merchant-id") merchantId: Long,
        @PathVariable(name = "product-id") productId: Long
    ) {
        delegate.invoke(merchantId, productId)
    }
}
