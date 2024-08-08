package com.matteusmoreno.moto_manager.exception;

public class InsufficientProductQuantityException extends RuntimeException {

    public InsufficientProductQuantityException() {
        super("Insufficient product quantity");
    }
}
