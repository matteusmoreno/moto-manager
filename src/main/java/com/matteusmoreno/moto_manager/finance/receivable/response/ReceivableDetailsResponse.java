package com.matteusmoreno.moto_manager.finance.receivable.response;

import com.matteusmoreno.moto_manager.finance.receivable.entity.Receivable;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReceivableDetailsResponse(
        Long receivableId,
        Long serviceOrderId,
        String motorcycleModel,
        String motorcyclePlate,
        String customerName,
        String sellerName,
        String mechanicName,
        String description,
        BigDecimal value,
        LocalDate issueDate,
        LocalDate paymentDate,
        String status) {

    public ReceivableDetailsResponse(Receivable receivable) {
        this(
            receivable.getId(),
            receivable.getServiceOrder().getId(),
            receivable.getServiceOrder().getMotorcycle().getModel(),
            receivable.getServiceOrder().getMotorcycle().getPlate(),
            receivable.getServiceOrder().getMotorcycle().getCustomer().getName(),
            receivable.getServiceOrder().getSeller().getName(),
            receivable.getServiceOrder().getMechanic().getName(),
            receivable.getServiceOrder().getDescription(),
            receivable.getValue(),
            receivable.getIssueDate(),
            receivable.getPaymentDate(),
            receivable.getStatus().getDisplayName()
        );
    }
}
