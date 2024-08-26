package com.matteusmoreno.moto_manager.payable;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePayableRequest(
        @NotNull(message = "Supplier Id is required")
        Long supplierId,
        String description,
        @NotNull(message = "Value is required")
        BigDecimal value,
        @NotNull(message = "Issue Date is required")
        LocalDate issueDate,
        @NotNull(message = "Due Date is required")
        LocalDate dueDate) {
}
