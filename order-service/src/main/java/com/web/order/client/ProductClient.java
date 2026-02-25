package com.web.order.client;

import com.web.order.dtos.ProductResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/products")
public interface ProductClient {
    @GetExchange("/{id}")
    ProductResponse getProduct(@PathVariable Long id);
}
