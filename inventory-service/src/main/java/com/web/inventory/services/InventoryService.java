package com.web.inventory.services;

import com.web.inventory.dtos.InventoryDto;

public interface InventoryService {

    InventoryDto getInventory(Long productId);

    InventoryDto reserve(Long productId, Integer quantity);

    InventoryDto confirm(Long productId, Integer quantity);

    InventoryDto release(Long productId, Integer quantity);
}
