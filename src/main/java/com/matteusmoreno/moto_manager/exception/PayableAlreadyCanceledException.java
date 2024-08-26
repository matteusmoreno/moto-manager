package com.matteusmoreno.moto_manager.exception;

public class PayableAlreadyCanceledException extends RuntimeException {
    public PayableAlreadyCanceledException(String message) {
        super(message);
    }
}
