package com.matteusmoreno.moto_manager.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "Product name is required")
        String name,
        String description,
        String manufacturer,
        @PositiveOrZero(message = "Product price must be greater than or equal to zero")
        BigDecimal price,
        @PositiveOrZero(message = "Quantity must be greater than or equal to zero")
        Integer quantity) {
}
