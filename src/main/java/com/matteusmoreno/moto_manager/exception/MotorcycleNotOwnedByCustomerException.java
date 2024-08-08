package com.matteusmoreno.moto_manager.exception;

public class MotorcycleNotOwnedByCustomerException extends RuntimeException {

    public MotorcycleNotOwnedByCustomerException() {
        super("Motorcycle is not owned by this customer.");
    }
}
