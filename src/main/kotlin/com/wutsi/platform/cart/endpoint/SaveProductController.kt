package com.wutsi.platform.cart.endpoint

import com.wutsi.platform.cart.`delegate`.SaveProductDelegate
import com.wutsi.platform.cart.dto.SaveProductRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid
import kotlin.Long

@RestController
public class SaveProductController(
    private val `delegate`: SaveProductDelegate
) {
    @PostMapping("/v1/carts/{merchant-id}/products/{product-id}")
    @PreAuthorize(value = "hasAuthority('cart-manage')")
    public fun invoke(
        @PathVariable(name = "merchant-id") merchantId: Long,
        @PathVariable(name = "product-id") productId: Long,
        @Valid @RequestBody request: SaveProductRequest
    ) {
        delegate.invoke(merchantId, productId, request)
    }
}
