package com.matteusmoreno.moto_manager.supplier.response;

public record SupplierListResponse(
        Long id,
        String name,
        String cnpj,
        String phone,
        String email) {

    public SupplierListResponse(com.matteusmoreno.moto_manager.supplier.entity.Supplier supplier) {
        this(
            supplier.getId(),
            supplier.getName(),
            supplier.getCnpj(),
            supplier.getPhone(),
            supplier.getEmail()
        );
    }
}
