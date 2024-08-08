package com.matteusmoreno.moto_manager.exception;

public class AddressAlreadyAssignedToCustomerException extends RuntimeException {

    public AddressAlreadyAssignedToCustomerException() {
        super("Address already assigned to customer");
    }
}
