package com.matteusmoreno.moto_manager.product.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotNull(message = "Product id is required")
        Long id,
        String name,
        String manufacturer,
        String description,
        @PositiveOrZero(message = "Product price must be greater than or equal to zero")
        BigDecimal price) {
}
