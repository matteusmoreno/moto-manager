package com.matteusmoreno.moto_manager.supplier.controller;

import com.matteusmoreno.moto_manager.supplier.request.CreateSupplierRequest;
import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
import com.matteusmoreno.moto_manager.supplier.request.UpdateSupplierRequest;
import com.matteusmoreno.moto_manager.supplier.response.SupplierDetailsResponse;
import com.matteusmoreno.moto_manager.supplier.response.SupplierListResponse;
import com.matteusmoreno.moto_manager.supplier.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping("/create")
    public ResponseEntity<SupplierDetailsResponse> create(@RequestBody @Valid CreateSupplierRequest request, UriComponentsBuilder uriBuilder) {
        Supplier supplier = supplierService.createSupplier(request);
        URI uri = uriBuilder.path("/suppliers/{id}").buildAndExpand(supplier.getId()).toUri();

        return ResponseEntity.created(uri).body(new SupplierDetailsResponse(supplier));
    }

    @GetMapping("/find-all")
    public ResponseEntity<Page<SupplierListResponse>> listAll(@PageableDefault(sort = "name", size = 10) Pageable pageable) {
        Page<SupplierListResponse> page = supplierService.listAllSuppliers(pageable);

        return ResponseEntity.ok(page);
    }

    @PutMapping("/update")
    public ResponseEntity<SupplierDetailsResponse> update(@RequestBody @Valid UpdateSupplierRequest request) {
        Supplier supplier = supplierService.updateSupplier(request);

        return ResponseEntity.ok(new SupplierDetailsResponse(supplier));
    }

    @DeleteMapping("/disable/{id}")
    public ResponseEntity<Void> disable(@PathVariable Long id) {
        supplierService.disableSupplier(id);

        return ResponseEntity.noContent().build();
    }
}
