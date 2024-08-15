package com.matteusmoreno.moto_manager.exception;

public class AddressNotOwnedByCustomerException extends RuntimeException {

    public AddressNotOwnedByCustomerException() {
        super("This customer is not linked to this address");
    }
}
