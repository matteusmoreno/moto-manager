package com.matteusmoreno.moto_manager.mapper;

import com.matteusmoreno.moto_manager.entity.Product;
import com.matteusmoreno.moto_manager.request.CreateProductRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProductMapper {

    public Product mapToProductForCreation(CreateProductRequest request) {
        return Product.builder()
                .name(request.name().toUpperCase())
                .description(request.description())
                .manufacturer(request.manufacturer().toUpperCase())
                .price(request.price())
                .quantity(request.quantity())
                .createdAt(LocalDateTime.now())
                .active(true)
                .build();
    }
}
