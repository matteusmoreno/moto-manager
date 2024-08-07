package com.matteusmoreno.moto_manager.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateEmployeeRequest(
        @NotNull(message = "Id cannot be null")
        UUID id,
        String name,
        @Email(message = "Invalid email")
        String email,
        String phone,
        LocalDate birthDate,
        String cpf) {
}
