package com.matteusmoreno.moto_manager.exception;

public class InvalidZipcodeException extends RuntimeException {

    public InvalidZipcodeException() {
        super("Invalid zipcode");
    }
}
