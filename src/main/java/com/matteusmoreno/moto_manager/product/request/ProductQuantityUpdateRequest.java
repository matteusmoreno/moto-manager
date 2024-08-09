package com.matteusmoreno.moto_manager.product.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductQuantityUpdateRequest(
        @NotNull(message = "Product id cannot be null")
        Long id,
        @PositiveOrZero(message = "Quantity cannot be negative")
        Integer quantity) {
}
