package com.web.inventory.controls;

import com.web.inventory.dtos.InventoryDto;
import com.web.inventory.dtos.OrderItemEvent;
import com.web.inventory.dtos.ReserveRequest;
import com.web.inventory.dtos.ReserveResponse;
import com.web.inventory.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryRestController {

    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    public InventoryDto getInventory(@PathVariable Long productId) {
        return inventoryService.getInventory(productId);
    }

    @PostMapping("/reserveTmp")
    public InventoryDto reserve(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return inventoryService.reserve(productId, quantity);
    }

    @PostMapping("/confirm")
    public InventoryDto confirm(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return inventoryService.confirm(productId, quantity);
    }

    @PostMapping("/release")
    public InventoryDto release(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return inventoryService.release(productId, quantity);
    }

    @PostMapping("/reserve")
    public ReserveResponse reserve(@RequestBody List<OrderItemEvent> items) {
        boolean reserved = inventoryService.reserveStock(items
        );

        if (!reserved) {
            return new ReserveResponse(false, "Insufficient stock",
                    123L, 0);
        }

        return new ReserveResponse(true, "Stock reserved",
                123L, 12);
    }
}
