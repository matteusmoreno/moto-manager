package com.matteusmoreno.moto_manager.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MotorcycleAlreadyExistsException.class)
    public ResponseEntity<String> handleMotorcycleAlreadyExistsException(MotorcycleAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MotorcycleAlreadyAssignedException.class)
    public ResponseEntity<String> handleMotorcycleAlreadyAssignedException(MotorcycleAlreadyAssignedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MotorcycleNotOwnedByCustomerException.class)
    public ResponseEntity<String> handleMotorcycleNotAssignedToCustomerException(MotorcycleNotOwnedByCustomerException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AddressAlreadyAssignedToCustomerException.class)
    public ResponseEntity<String> handleAddressAlreadyAssignedToCustomerException(AddressAlreadyAssignedToCustomerException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidZipcodeException.class)
    public ResponseEntity<String> handleInvalidZipcodeException(InvalidZipcodeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
