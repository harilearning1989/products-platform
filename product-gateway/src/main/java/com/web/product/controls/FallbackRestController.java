package com.web.product.controls;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FallbackRestController {

    @GetMapping("/fallback/products")
    public Map<String, String> productFallback() {
        return Map.of(
                "message", "Product service is temporarily unavailable",
                "status", "FAILED"
        );
    }

    @GetMapping("/fallback/payments")
    public Map<String, String> paymentFallback() {
        return Map.of(
                "message", "Payment service is temporarily unavailable",
                "status", "FAILED"
        );
    }
}
