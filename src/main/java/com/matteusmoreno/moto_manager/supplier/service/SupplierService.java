package com.matteusmoreno.moto_manager.supplier.service;

import com.matteusmoreno.moto_manager.exception.SupplierAlreadyExistsException;
import com.matteusmoreno.moto_manager.supplier.request.CreateSupplierRequest;
import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
import com.matteusmoreno.moto_manager.supplier.repository.SupplierRepository;
import com.matteusmoreno.moto_manager.supplier.request.UpdateSupplierRequest;
import com.matteusmoreno.moto_manager.supplier.response.SupplierListResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
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

    public Page<SupplierListResponse> listAllSuppliers(Pageable pageable) {
        return supplierRepository.findAll(pageable).map(SupplierListResponse::new);
    }

    @Transactional
    public Supplier updateSupplier(UpdateSupplierRequest request) {
        Supplier supplier = supplierRepository.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        if (request.name() != null) supplier.setName(request.name().toUpperCase());
        if (request.cnpj() != null) supplier.setCnpj(request.cnpj());
        if (request.phone() != null) supplier.setPhone(request.phone());
        if (request.email() != null) supplier.setEmail(request.email());

        supplier.setUpdatedAt(LocalDateTime.now());
        supplierRepository.save(supplier);

        return supplier;
    }

    @Transactional
    public void disableSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        supplier.setActive(false);
        supplier.setDeletedAt(LocalDateTime.now());
        supplierRepository.save(supplier);
    }

    @Transactional
    public Supplier enableSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        supplier.setActive(true);
        supplier.setDeletedAt(null);
        supplier.setUpdatedAt(LocalDateTime.now());
        supplierRepository.save(supplier);

        return supplier;
    }
}
