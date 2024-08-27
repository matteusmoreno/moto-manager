package com.matteusmoreno.moto_manager.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<InvalidFields>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> errors = ex.getFieldErrors();
        List<InvalidFields> invalidFields = errors.stream().map(InvalidFields::new).toList();

        return ResponseEntity.badRequest().body(invalidFields);
    }

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

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<String> handleProductAlreadyExistsException(ProductAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InsufficientProductQuantityException.class)
    public ResponseEntity<String> handleInsufficientProductQuantityException(InsufficientProductQuantityException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MotorcycleNotAssignedException.class)
    public ResponseEntity<String> handleMotorcycleNotAssignedException(MotorcycleNotAssignedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AddressNotOwnedByCustomerException.class)
    public ResponseEntity<String> handleAddressNotOwnedByCustomerException(AddressNotOwnedByCustomerException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SupplierAlreadyExistsException.class)
    public ResponseEntity<String> handleSupplierAlreadyExistsException(SupplierAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDueDateException.class)
    public ResponseEntity<String> handleInvalidDueDateException(InvalidDueDateException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PayableAlreadyPaidException.class)
    public ResponseEntity<String> handlePayableAlreadyPaidException(PayableAlreadyPaidException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PayableAlreadyCanceledException.class)
    public ResponseEntity<String> handlePayableAlreadyCanceledException(PayableAlreadyCanceledException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceOrderStatusException.class)
    public ResponseEntity<String> handleServiceOrderStatusException(ServiceOrderStatusException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentStatusException.class)
    public ResponseEntity<String> handlePaymentStatusException(PaymentStatusException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
