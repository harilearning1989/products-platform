package com.web.inventory.services;

import com.web.inventory.dtos.InventoryDto;
import com.web.inventory.exceptions.InventoryNotFoundException;
import com.web.inventory.models.Inventory;
import com.web.inventory.repos.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public boolean reserveStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElse(null);

        if (inventory == null) {
            return false;
        }

        // Check stock
        if (inventory.getAvailableQuantity() < quantity) {
            return false;
        }

        // Reserve stock
        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() - quantity
        );

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() + quantity
        );

        inventoryRepository.save(inventory);

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
