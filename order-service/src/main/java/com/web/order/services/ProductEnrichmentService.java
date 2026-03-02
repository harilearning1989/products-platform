package com.web.order.services;

import com.web.order.dtos.ProductResponse;
import com.web.order.wrapper.ProductClientWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductEnrichmentService {

    private final ProductClientWrapper productClientWrapper;

    public Map<Long, ProductResponse> fetchProductMap(List<Long> productIds) {

        List<ProductResponse> products =
                productClientWrapper.fetchProducts(productIds);

        return products.stream()
                .collect(Collectors.toMap(
                        ProductResponse::id,
                        p -> p
                ));
    }
}
