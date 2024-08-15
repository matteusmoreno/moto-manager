package com.matteusmoreno.moto_manager.customer.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RemoveCustomerAddressRequest(
        @NotNull(message = "Customer id is required")
        UUID customerId,
        @NotBlank(message = "Zipcode is required")
        String zipcode,
        @NotBlank(message = "Number is required")
        String number) {
}
