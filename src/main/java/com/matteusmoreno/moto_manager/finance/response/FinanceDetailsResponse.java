package com.matteusmoreno.moto_manager.finance.response;

import com.matteusmoreno.moto_manager.finance.entity.Finance;
import com.matteusmoreno.moto_manager.finance.payable.response.PayableDetailsResponse;
import com.matteusmoreno.moto_manager.finance.receivable.response.ReceivableDetailsResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FinanceDetailsResponse(
        String reportName,
        LocalDate reportDate,
        List<ReceivableDetailsResponse> receivables,
        List<PayableDetailsResponse> payables,
        BigDecimal totalReceivables,
        BigDecimal totalPayables,
        BigDecimal profitOrLoss) {

    public FinanceDetailsResponse(Finance finance) {
        this(
                finance.getReportName(),
                finance.getReportDate(),
                finance.getReceivables().stream().map(ReceivableDetailsResponse::new).toList(),
                finance.getPayables().stream().map(PayableDetailsResponse::new).toList(),
                finance.getTotalReceivables(),
                finance.getTotalPayables(),
                finance.getProfitOrLoss()
        );
    }
}
