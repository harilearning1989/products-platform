package com.web.order.client;

import com.web.order.dtos.ReserveRequest;
import com.web.order.dtos.ReserveResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/inventory")
public interface InventoryClient {
    @PostMapping("/reserve")
    ReserveResponse reserveStock(ReserveRequest reserveRequest);
}
