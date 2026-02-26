package com.web.inventory.dtos;

public record ReserveResponse(Boolean reserved, String stock,Long productId, Integer quantity) {
}
