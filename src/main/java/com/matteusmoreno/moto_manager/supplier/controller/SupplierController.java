package com.matteusmoreno.moto_manager.supplier.controller;

import com.matteusmoreno.moto_manager.supplier.request.CreateSupplierRequest;
import com.matteusmoreno.moto_manager.supplier.entity.Supplier;
import com.matteusmoreno.moto_manager.supplier.response.SupplierDetailsResponse;
import com.matteusmoreno.moto_manager.supplier.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
