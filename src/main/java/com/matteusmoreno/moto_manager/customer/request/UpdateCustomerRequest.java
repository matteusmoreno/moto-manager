package com.matteusmoreno.moto_manager.customer.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateCustomerRequest(
        @NotNull(message = "Id is required")
        UUID id,
        String name,
        String email,
        LocalDate birthDate,
        String phone) {
}
