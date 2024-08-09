package com.matteusmoreno.moto_manager.customer.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MotorcycleCustomerRequest(
        @NotNull(message = "Customer id is required")
        UUID customerId,
        @NotNull(message = "Motorcycle id is required")
        UUID motorcycleId) {
}
