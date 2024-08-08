package com.matteusmoreno.moto_manager.repository;

import com.matteusmoreno.moto_manager.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameAndManufacturerIgnoreCase(String name, String manufacturer);

    Product findByNameAndManufacturerIgnoreCase(String name, String manufacturer);

}
