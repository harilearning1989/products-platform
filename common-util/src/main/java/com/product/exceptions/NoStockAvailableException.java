package com.product.exceptions;

public class NoStockAvailableException extends RuntimeException {

    private final Long productId;

    public NoStockAvailableException(Long productId) {
        super("No Stock available for the Product:: " + productId);
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
