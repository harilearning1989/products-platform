package com.web.order.controls;

import com.web.order.dtos.CreateOrderRequest;
import com.web.order.dtos.OrderResponse;
import com.web.order.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService service;

    @PostMapping
    public OrderResponse create(@RequestBody CreateOrderRequest request) {
        return service.createOrder(request);
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable Long id) {
        return service.getOrder(id);
    }
}
