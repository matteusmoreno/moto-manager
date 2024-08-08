package com.matteusmoreno.moto_manager.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddressCustomerRequest(
        @NotNull(message = "Customer id is required")
        UUID customerId,
        @NotBlank(message = "Zipcode is required")
        String zipcode,
        String number,
        String complement) {
}
