package com.matteusmoreno.moto_manager.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record CreateCustomerRequest(
        @NotBlank(message = "Name is required")
        String name,
        @Email(message = "Invalid email")
        String email,
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format (aaaa-mm-dd")
        LocalDate birthDate,
        @Pattern(regexp = "^\\(\\d{2}\\)\\d{9}$", message = "Invalid phone format (xx)xxxxxxxxx")
        String phone,
        @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "Invalid zipcode format (xxxxx-xxx)")
        String zipcode,
        String number,
        String complement) {
}
