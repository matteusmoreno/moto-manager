package com.matteusmoreno.moto_manager.exception;

public class MotorcycleAlreadyAssignedException extends RuntimeException {

    public MotorcycleAlreadyAssignedException() {
        super("Motorcycle already assigned to a customer");
    }
}
