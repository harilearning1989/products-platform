package com.web.order.client;

import com.web.order.dtos.CustomerResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/customers")
public interface CustomerClient {

    @GetExchange("/{id}")
    CustomerResponse getCustomerById(@PathVariable Long id);
}
