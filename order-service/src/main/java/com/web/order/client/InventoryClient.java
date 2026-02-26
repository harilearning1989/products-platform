package com.web.order.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/inventory")
public interface InventoryClient {

    //@PostExchange("/reserve")
    //ReserveResponse reserveStock(@RequestBody ReserveRequest reserveRequest);
}
