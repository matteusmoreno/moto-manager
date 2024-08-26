package com.matteusmoreno.moto_manager.exception;

public class SupplierAlreadyExistsException extends RuntimeException {

    public SupplierAlreadyExistsException(String supplierName) {
        super("Supplier already exists: " + supplierName);
    }
}
