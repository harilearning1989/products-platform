package com.web.order.wrapper;

import com.web.order.client.ProductClient;
import com.web.order.dtos.ProductResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductClientWrapper {

    private final ProductClient productClient;

    //@Retry(name = "productService")
    @CircuitBreaker(name = "productService", fallbackMethod = "productFallback")
    public List<ProductResponse> fetchProducts(Set<Long> ids) {
        return productClient.getProducts(ids);
    }

    public List<ProductResponse> productFallback(Set<Long> ids, Throwable ex) {
        log.error("Product service is DOWN. Circuit breaker triggered.", ex);

        return ids.stream()
                .map(ProductResponse::empty)
                .toList();
    }
}
