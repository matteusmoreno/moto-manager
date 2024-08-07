package com.matteusmoreno.moto_manager.exception;

public class MotorcycleAlreadyExistsException extends RuntimeException {

    public MotorcycleAlreadyExistsException() {
        super("Motorcycle already exists.");
    }
}
