package com.matteusmoreno.moto_manager.supplier.service;

import com.matteusmoreno.moto_manager.exception.SupplierAlreadyExistsException;
import com.matteusmoreno.moto_manager.supplier.request.CreateSupplierRequest;
import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
import com.matteusmoreno.moto_manager.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@org.springframework.stereotype.Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public Supplier createSupplier(CreateSupplierRequest request) {

        if (supplierRepository.existsByNameAndCnpjIgnoreCase(request.name(), request.cnpj())) {
            throw new SupplierAlreadyExistsException(request.name());
        }

        Supplier supplier = Supplier.builder()
                .name(request.name().toUpperCase())
                .cnpj(request.cnpj())
                .phone(request.phone())
                .email(request.email())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .active(true)
                .build();

        supplierRepository.save(supplier);

        return supplier;
    }
}
