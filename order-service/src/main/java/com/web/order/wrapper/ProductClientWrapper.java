package com.web.order.wrapper;

import com.web.order.client.ProductClient;
import com.web.order.dtos.ProductResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductClientWrapper {

    private final ProductClient productClient;

    //@Retry(name = "productService")
    @CircuitBreaker(name = "productService", fallbackMethod = "productFallback")
    public List<ProductResponse> fetchProducts(List<Long> ids) {
        return productClient.getProducts(ids);
    }

    public List<ProductResponse> productFallback(List<Long> ids, Throwable ex) {
        log.error("Product service is DOWN. Circuit breaker triggered.", ex);

        throw new RuntimeException("Product service unavailable. Try later.");
    }
}
