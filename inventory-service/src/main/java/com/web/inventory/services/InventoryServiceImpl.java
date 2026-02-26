package com.web.inventory.services;

import com.web.inventory.dtos.InventoryDto;
import com.web.inventory.dtos.OrderItemEvent;
import com.web.inventory.exceptions.InventoryNotFoundException;
import com.web.inventory.models.Inventory;
import com.web.inventory.repos.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public InventoryDto getInventory(Long productId) {
        Inventory inventory = getInventoryEntity(productId);
        return mapToDto(inventory);
    }

    @Override
    public InventoryDto reserve(Long productId, Integer quantity) {
        Inventory inventory = getInventoryEntity(productId);

        if (inventory.getAvailableQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() - quantity
        );

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() + quantity
        );

        return mapToDto(inventory);
    }

    @Override
    public InventoryDto confirm(Long productId, Integer quantity) {

        Inventory inventory = getInventoryEntity(productId);

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() - quantity
        );

        return mapToDto(inventory);
    }

    @Override
    public InventoryDto release(Long productId, Integer quantity) {
        Inventory inventory = getInventoryEntity(productId);
        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() + quantity
        );

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() - quantity
        );

        return mapToDto(inventory);
    }

    @Override
    public void saveInventory(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

    @Override
    public boolean findByProductId(Long aLong) {
        return inventoryRepository.findByProductId(aLong).isPresent();
    }

    @Transactional
    public boolean reserveStock(List<OrderItemEvent> items) {
        // 1️⃣ Collect product IDs
        List<Long> productIds = items.stream()
                .map(OrderItemEvent::productId)
                .toList();

        // 2️⃣ Fetch all inventory rows with lock
        List<Inventory> inventories =
                inventoryRepository.findAllByProductIdsForUpdate(productIds);

        Map<Long, Inventory> inventoryMap =
                inventories.stream()
                        .collect(Collectors.toMap(
                                Inventory::getProductId,
                                i -> i
                        ));

        // 3️⃣ VALIDATION LOOP (no deduction yet)
        for (OrderItemEvent item : items) {
            Inventory inventory =
                    inventoryMap.get(item.productId());

            if (inventory == null ||
                    inventory.getAvailableQuantity()
                            < item.quantity()) {
                return false; // fail early
            }
        }

        // 4️⃣ DEDUCTION LOOP (only if all valid)
        for (OrderItemEvent item : items) {
            Inventory inventory =
                    inventoryMap.get(item.productId());
            inventory.setAvailableQuantity(
                    inventory.getAvailableQuantity()
                            - item.quantity());
        }
        return true;
    }

    private Inventory getInventoryEntity(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(productId));
    }

    private InventoryDto mapToDto(Inventory inventory) {
        return new InventoryDto(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getAvailableQuantity(),
                inventory.getReservedQuantity(),
                inventory.getLastUpdated()
        );
    }
}
