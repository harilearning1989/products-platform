package com.web.inventory.controls;

import com.web.inventory.dtos.InventoryDto;
import com.web.inventory.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryRestController {

    private final InventoryService service;

    @GetMapping("/{productId}")
    public InventoryDto getInventory(@PathVariable Long productId) {
        return service.getInventory(productId);
    }

    @PostMapping("/reserve")
    public InventoryDto reserve(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return service.reserve(productId, quantity);
    }

    @PostMapping("/confirm")
    public InventoryDto confirm(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return service.confirm(productId, quantity);
    }

    @PostMapping("/release")
    public InventoryDto release(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        return service.release(productId, quantity);
    }
}
