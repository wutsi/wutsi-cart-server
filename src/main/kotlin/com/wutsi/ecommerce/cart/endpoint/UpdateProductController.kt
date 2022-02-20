package com.wutsi.ecommerce.cart.endpoint

import com.wutsi.ecommerce.cart.delegate.UpdateProductDelegate
import com.wutsi.ecommerce.cart.dto.UpdateProductRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
public class UpdateProductController(
    private val `delegate`: UpdateProductDelegate
) {
    @PostMapping("/v1/carts/{merchant-id}/products/{product-id}")
    @PreAuthorize(value = "hasAuthority('cart-manage')")
    public fun invoke(
        @PathVariable(name = "merchant-id") merchantId: Long,
        @PathVariable(name = "product-id") productId: Long,
        @Valid @RequestBody request: UpdateProductRequest
    ) {
        delegate.invoke(merchantId, productId, request)
    }
}
