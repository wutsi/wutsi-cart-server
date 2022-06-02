package com.wutsi.ecommerce.cart.endpoint

import com.wutsi.ecommerce.cart.`delegate`.AddProductDelegate
import com.wutsi.ecommerce.cart.dto.AddProductRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid
import kotlin.Long

@RestController
public class AddProductController(
    private val `delegate`: AddProductDelegate
) {
    @PostMapping("/v1/carts/{merchant-id}/products")
    @PreAuthorize(value = "hasAuthority('cart-manage')")
    public fun invoke(
        @PathVariable(name = "merchant-id") merchantId: Long,
        @Valid @RequestBody
        request: AddProductRequest
    ) {
        delegate.invoke(merchantId, request)
    }
}
