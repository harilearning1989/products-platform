package com.web.demo.producer;

import com.web.demo.dtos.ProductCreateDto;
import com.web.demo.dtos.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "product-created-topic";

    public void publishProductCreated(Long productId, String name) {
        /*ProductCreatedEvent event =
                new ProductCreatedEvent(productId, name);*/
        ProductCreateDto productCreateDto = new ProductCreateDto();
        productCreateDto.setProductId(productId);
        productCreateDto.setProductName(name);
        kafkaTemplate.send(TOPIC, productCreateDto);
    }
}
