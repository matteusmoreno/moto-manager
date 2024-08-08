package com.matteusmoreno.moto_manager.exception;

public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException() {
        super("Product already exists");
    }
}
