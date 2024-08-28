package com.matteusmoreno.moto_manager.finance.service;

import com.matteusmoreno.moto_manager.finance.entity.Finance;
import com.matteusmoreno.moto_manager.finance.payable.entity.Payable;
import com.matteusmoreno.moto_manager.finance.payable.repository.PayableRepository;
import com.matteusmoreno.moto_manager.finance.receivable.entity.Receivable;
import com.matteusmoreno.moto_manager.finance.receivable.repository.ReceivableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FinanceService {

    private final ReceivableRepository receivableRepository;
    private final PayableRepository payableRepository;

    @Autowired
    public FinanceService(ReceivableRepository receivableRepository, PayableRepository payableRepository) {
        this.receivableRepository = receivableRepository;
        this.payableRepository = payableRepository;
    }

    @Transactional
    public Finance generateWeeklyReport() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        List<Receivable> receivables = receivableRepository.findReceivablesWithinDateRange(startDate, endDate);
        List<Payable> payables = payableRepository.findPayablesWithinDateRange(startDate, endDate);

        BigDecimal totalReceivables = receivables.stream()
                .map(Receivable::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPayables = payables.stream()
                .map(Payable::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal profitOrLoss = totalReceivables.subtract(totalPayables);

        return Finance.builder()
                .reportName("Weekly Financial Report")
                .reportDate(LocalDate.now())
                .receivables(receivables)
                .payables(payables)
                .totalReceivables(totalReceivables)
                .totalPayables(totalPayables)
                .profitOrLoss(profitOrLoss)
                .build();
    }
}
