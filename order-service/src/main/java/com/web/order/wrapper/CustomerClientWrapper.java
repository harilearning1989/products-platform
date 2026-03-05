package com.web.order.wrapper;

import com.web.order.client.CustomerClient;
import com.web.order.dtos.CustomerResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerClientWrapper {

    private final CustomerClient customerClient;

    @CircuitBreaker(name = "customerService", fallbackMethod = "customerFallback")
    public CustomerResponse getCustomerById(Long userId) {
        return customerClient.getCustomerById(userId);
    }

    public CustomerResponse customerFallback(List<Long> ids, Throwable ex) {
        log.error("Customer service is DOWN. Circuit breaker triggered.", ex);

        throw new RuntimeException("Product service unavailable. Try later.");
    }

    public List<CustomerResponse> getCustomersBulk(List<Long> userIds) {
        return customerClient.getCustomersBulk(userIds);
    }
}
