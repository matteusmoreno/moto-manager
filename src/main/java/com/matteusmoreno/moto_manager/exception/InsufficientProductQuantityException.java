package com.matteusmoreno.moto_manager.exception;

public class InsufficientProductQuantityException extends RuntimeException {

    public InsufficientProductQuantityException(String productName) {
        super("Insufficient product quantity: " + productName);
    }
}
