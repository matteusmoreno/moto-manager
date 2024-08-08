package com.matteusmoreno.moto_manager.response;

import com.matteusmoreno.moto_manager.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDetailsResponse(
        Long id,
        String name,
        String description,
        String manufacturer,
        BigDecimal price,
        Integer quantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean active) {

    public ProductDetailsResponse(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getManufacturer(),
                product.getPrice(),
                product.getQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getDeletedAt(),
                product.getActive()
        );
    }
}
