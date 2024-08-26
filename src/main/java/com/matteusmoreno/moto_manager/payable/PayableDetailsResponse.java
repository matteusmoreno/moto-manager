package com.matteusmoreno.moto_manager.payable;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PayableDetailsResponse(
        Long id,
        String supplierName,
        String supplierCnpj,
        String description,
        BigDecimal value,
        LocalDate issueDate,
        LocalDate dueDate,
        LocalDate paymentDate,
        String status) {

    public PayableDetailsResponse(Payable payable) {
        this(
            payable.getId(),
            payable.getSupplier().getName(),
            payable.getSupplier().getCnpj(),
            payable.getDescription(),
            payable.getValue(),
            payable.getIssueDate(),
            payable.getDueDate(),
            payable.getPaymentDate(),
            payable.getStatus().getDisplayName()
        );
    }
}
