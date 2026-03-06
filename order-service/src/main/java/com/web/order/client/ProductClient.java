package com.web.order.client;

import com.web.order.dtos.ProductResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.Set;

@HttpExchange("/products")//http://product-service//products/3
public interface ProductClient {
    @GetExchange("/{id}")
    ProductResponse getProduct(@PathVariable Long id);

    @PostExchange("/bulk")
    List<ProductResponse> getProducts(
            @RequestBody Set<Long> ids);
}
