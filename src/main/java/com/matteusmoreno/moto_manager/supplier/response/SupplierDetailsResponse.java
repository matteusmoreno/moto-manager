package com.matteusmoreno.moto_manager.supplier.response;

import com.matteusmoreno.moto_manager.supplier.entity.Supplier;

import java.time.LocalDateTime;

public record SupplierDetailsResponse(
        Long id,
        String name,
        String cnpj,
        String phone,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean active) {

    public SupplierDetailsResponse(Supplier supplier) {
        this(
            supplier.getId(),
            supplier.getName(),
            supplier.getCnpj(),
            supplier.getPhone(),
            supplier.getEmail(),
            supplier.getCreatedAt(),
            supplier.getUpdatedAt(),
            supplier.getDeletedAt(),
            supplier.getActive()
        );
    }
}
