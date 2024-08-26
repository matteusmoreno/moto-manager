package com.matteusmoreno.moto_manager.supplier.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateSupplierRequest(
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "CNPJ is required")
        @Pattern(message = "CNPJ is invalid", regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}\\/\\d{4}\\-\\d{2}$")
        String cnpj,
        @Pattern(regexp = "^\\(\\d{2}\\)\\d{9}$", message = "Invalid phone format (xx)xxxxxxxxx")
        String phone,
        @Email(message = "Invalid email format")
        String email) {
}
