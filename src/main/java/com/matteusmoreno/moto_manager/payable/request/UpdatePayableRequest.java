package com.matteusmoreno.moto_manager.payable.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdatePayableRequest(
        @NotNull(message = "Payable Id is required")
        Long payableId,
        Long supplierId,
        String description,
        @PositiveOrZero(message = "Value must be greater than or equal to zero")
        BigDecimal value,
        LocalDate issueDate,
        LocalDate dueDate) {
}
