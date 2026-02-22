package com.web.demo.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.web.demo.dtos.ProductRequest;
import com.web.demo.dtos.ProductResponse;
import com.web.demo.exceptions.ProductNotFoundException;
import com.web.demo.exceptions.ResourceNotFoundException;
import com.web.demo.models.Product;
import com.web.demo.producer.ProductEventProducer;
import com.web.demo.reader.JsonFileReader;
import com.web.demo.records.ProductDto;
import com.web.demo.repos.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService{

    private static final String FILE_NAME = "products.json";

    private final JsonFileReader jsonFileReader;

    private List<ProductDto> products;

    private ProductRepository repository;
    private ProductEventProducer producer;

    public ProductServiceImpl(JsonFileReader jsonFileReader,
                              ProductRepository repository) {
        this.jsonFileReader = jsonFileReader;
        this.repository = repository;
    }

    @PostConstruct
    public void loadProducts() {
        this.products = jsonFileReader.readListFromFile(
                FILE_NAME,
                new TypeReference<>() {}
        );
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return products;
    }

    @Override
    public ProductDto getProductById(Long id) {
        return products.stream()
                .filter(c -> c.id().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with id: " + id));
    }

    @Override
    public ProductResponse create(ProductRequest request) {

        if (repository.existsBySku(request.sku())) {
            throw new RuntimeException("SKU already exists");
        }

        Product product = repository.save(Product.builder()
                .sku(request.sku())
                .name(request.name())
                .brand(request.brand())
                .category(request.category())
                .price(request.price())
                .weightInGrams(request.weightInGrams())
                .description(request.description())
                .active(true)
                .build());

        // Publish event after save
        producer.publishProductCreated(product.getId(), product.getName());

        return mapToResponse(product);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(request.name());
        product.setBrand(request.brand());
        product.setCategory(request.category());
        product.setPrice(request.price());
        product.setWeightInGrams(request.weightInGrams());
        product.setDescription(request.description());

        return mapToResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAll(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return repository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public void delete(Long id) {
        com.web.demo.models.Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setActive(false); // soft delete
    }

    private ProductResponse mapToResponse(com.web.demo.models.Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getBrand(),
                product.getCategory(),
                product.getPrice(),
                product.getWeightInGrams(),
                product.getActive(),
                product.getDescription(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
