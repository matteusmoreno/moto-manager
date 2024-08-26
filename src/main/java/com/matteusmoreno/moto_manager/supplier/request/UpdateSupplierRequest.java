package com.matteusmoreno.moto_manager.supplier.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateSupplierRequest(
        @NotNull(message = "Supplier id is required")
        Long id,
        String name,
        @Pattern(message = "CNPJ is invalid", regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}$")
        String cnpj,
        @Pattern(regexp = "^\\(\\d{2}\\)\\d{9}$", message = "Invalid phone format (xx)xxxxxxxxx")
        String phone,
        @Email(message = "Invalid email format")
        String email) {
}
