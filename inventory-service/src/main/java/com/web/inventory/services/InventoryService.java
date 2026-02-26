package com.web.inventory.services;

import com.web.inventory.dtos.InventoryDto;
import com.web.inventory.models.Inventory;

import java.util.Optional;

public interface InventoryService {

    InventoryDto getInventory(Long productId);

    InventoryDto reserve(Long productId, Integer quantity);

    InventoryDto confirm(Long productId, Integer quantity);

    InventoryDto release(Long productId, Integer quantity);

    void saveInventory(Inventory inventory);

    boolean findByProductId(Long aLong);

    boolean reserveStock(Long productId, Integer quantity);
}
