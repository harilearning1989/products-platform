package com.web.demo.services;

import com.web.demo.dtos.ProductRequest;
import com.web.demo.dtos.ProductResponse;
import com.web.demo.records.ProductDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);

    ProductResponse create(ProductRequest request);

    ProductResponse update(Long id, ProductRequest request);

    ProductResponse getById(Long id);

    Page<ProductResponse> getAll(int page, int size);

    void delete(Long id);
}
