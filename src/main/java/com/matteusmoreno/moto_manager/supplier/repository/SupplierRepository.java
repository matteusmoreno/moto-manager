package com.matteusmoreno.moto_manager.supplier.repository;

import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    boolean existsByNameAndCnpjIgnoreCase(String name, String cnpj);
}
