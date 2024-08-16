package com.matteusmoreno.moto_manager.exception;

import org.springframework.validation.FieldError;

public record InvalidFields(String field, String message) {

    public InvalidFields(FieldError fieldError) {
        this(
                fieldError.getField(),
                fieldError.getDefaultMessage()
        );

    }
}
