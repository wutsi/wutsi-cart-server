package com.wutsi.ecommerce.cart.endpoint

import com.wutsi.ecommerce.cart.`delegate`.SearchCartsDelegate
import com.wutsi.ecommerce.cart.dto.SearchCartRequest
import com.wutsi.ecommerce.cart.dto.SearchCartResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class SearchCartsController(
    private val `delegate`: SearchCartsDelegate
) {
    @PostMapping("/v1/carts/search")
    @PreAuthorize(value = "hasAuthority('cart-read')")
    public fun invoke(@Valid @RequestBody request: SearchCartRequest): SearchCartResponse =
        delegate.invoke(request)
}
