package com.web.order.services;

import com.web.order.dtos.CreateOrderRequest;
import com.web.order.dtos.OrderItemResponse;
import com.web.order.dtos.OrderResponse;
import com.web.order.models.Order;
import com.web.order.models.OrderItem;
import com.web.order.producer.OrderEventProducer;
import com.web.order.repos.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer producer;

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {

        BigDecimal total = request.items().stream()
                .map(i -> i.price().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .customerEmail(request.customerEmail())
                .totalAmount(total)
                .build();

        order.setItems(
                request.items().stream()
                        .map(i -> OrderItem.builder()
                                .productId(i.productId())
                                .quantity(i.quantity())
                                .price(i.price())
                                .order(order)
                                .build())
                        .collect(Collectors.toList())
        );

        Order saved = orderRepository.save(order);

        producer.publishOrderCreated(
                saved.getId(),
                saved.getCustomerEmail(),
                saved.getTotalAmount().toString()
        );

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerEmail(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(i -> new OrderItemResponse(
                                i.getProductId(),
                                i.getQuantity(),
                                i.getPrice()
                        )).toList()
        );
    }
}
