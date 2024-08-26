package com.matteusmoreno.moto_manager.exception;

public class PayableAlreadyPaidException extends RuntimeException {
    public PayableAlreadyPaidException(String message) {
        super(message);
    }
}
