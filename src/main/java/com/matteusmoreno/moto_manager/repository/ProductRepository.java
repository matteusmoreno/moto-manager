package com.matteusmoreno.moto_manager.repository;

import com.matteusmoreno.moto_manager.entity.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameAndManufacturerIgnoreCase(String manufacturer, String name);

    Product findByNameAndManufacturerIgnoreCase(String name, String manufacturer);
}
