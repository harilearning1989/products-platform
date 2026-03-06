package com.web.order.client;

import com.product.dtos.CustomerResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@HttpExchange("/customers")
public interface CustomerClient {

    @GetExchange("/{id}")
    CustomerResponse getCustomerById(@PathVariable Long id);

    @PostExchange("/bulk")
    List<CustomerResponse> getCustomersBulk(@RequestBody List<Long> userIds);
}
