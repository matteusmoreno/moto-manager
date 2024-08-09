package com.matteusmoreno.moto_manager.product.repository;

import com.matteusmoreno.moto_manager.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameAndManufacturerIgnoreCase(String name, String manufacturer);
}
