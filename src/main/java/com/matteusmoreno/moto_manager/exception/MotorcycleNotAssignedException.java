package com.matteusmoreno.moto_manager.exception;

public class MotorcycleNotAssignedException extends RuntimeException {

    public MotorcycleNotAssignedException() {
        super("The motorcycle is not assigned to any customer.");
}
}
