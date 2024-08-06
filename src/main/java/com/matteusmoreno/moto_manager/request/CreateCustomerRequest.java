package com.matteusmoreno.moto_manager.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CreateCustomerRequest(
        @NotBlank(message = "Name is required")
        String name,
        @Email(message = "Invalid email")
        String email,
        LocalDate birthDate,
        String phone,
        String zipcode,
        String number,
        String complement) {
}
