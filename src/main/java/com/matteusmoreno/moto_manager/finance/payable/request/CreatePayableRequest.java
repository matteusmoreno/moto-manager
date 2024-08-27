package com.matteusmoreno.moto_manager.finance.payable.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePayableRequest(
        @NotNull(message = "Supplier Id is required")
        Long supplierId,
        String description,
        @NotNull(message = "Value is required")
        @PositiveOrZero(message = "Value must be greater than or equal to zero")
        BigDecimal value,
        @NotNull(message = "Issue Date is required")
        LocalDate issueDate,
        @NotNull(message = "Due Date is required")
        LocalDate dueDate) {
}
