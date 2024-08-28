package com.matteusmoreno.moto_manager.finance.service;

import com.lowagie.text.DocumentException;
import com.matteusmoreno.moto_manager.exception.PdfReportGenerationException;
import com.matteusmoreno.moto_manager.finance.PdfReportGenerator;
import com.matteusmoreno.moto_manager.finance.entity.Finance;
import com.matteusmoreno.moto_manager.finance.payable.entity.Payable;
import com.matteusmoreno.moto_manager.finance.payable.repository.PayableRepository;
import com.matteusmoreno.moto_manager.finance.receivable.entity.Receivable;
import com.matteusmoreno.moto_manager.finance.receivable.repository.ReceivableRepository;
import com.matteusmoreno.moto_manager.finance.response.FinanceDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FinanceService {

    private final ReceivableRepository receivableRepository;
    private final PayableRepository payableRepository;
    private final PdfReportGenerator pdfReportGenerator;

    @Autowired
    public FinanceService(ReceivableRepository receivableRepository, PayableRepository payableRepository, PdfReportGenerator pdfReportGenerator) {
        this.receivableRepository = receivableRepository;
        this.payableRepository = payableRepository;
        this.pdfReportGenerator = pdfReportGenerator;
    }

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

        Finance finance = Finance.builder()
                .reportName("Weekly Financial Report")
                .reportDate(LocalDate.now())
                .receivables(receivables)
                .payables(payables)
                .totalReceivables(totalReceivables)
                .totalPayables(totalPayables)
                .profitOrLoss(profitOrLoss)
                .build();

        generateAndSavePdfReport(finance, "weekly-report-");

        return finance;
    }

    public Finance generateMonthlyReport(Integer year, Integer month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());

        List<Receivable> receivables = receivableRepository.findReceivablesWithinDateRange(startDate, endDate);
        List<Payable> payables = payableRepository.findPayablesWithinDateRange(startDate, endDate);

        BigDecimal totalReceivables = receivables.stream()
                .map(Receivable::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPayables = payables.stream()
                .map(Payable::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal profitOrLoss = totalReceivables.subtract(totalPayables);

        Finance finance = Finance.builder()
                .reportName("Monthly Financial Report")
                .reportDate(LocalDate.now())
                .receivables(receivables)
                .payables(payables)
                .totalReceivables(totalReceivables)
                .totalPayables(totalPayables)
                .profitOrLoss(profitOrLoss)
                .build();

        generateAndSavePdfReport(finance, "monthly-report-");

        return finance;
    }

    @Transactional
    public Finance generateYearlyReport(Integer year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        List<Receivable> receivables = receivableRepository.findReceivablesWithinDateRange(startDate, endDate);
        List<Payable> payables = payableRepository.findPayablesWithinDateRange(startDate, endDate);

        BigDecimal totalReceivables = receivables.stream()
                .map(Receivable::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPayables = payables.stream()
                .map(Payable::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal profitOrLoss = totalReceivables.subtract(totalPayables);

        Finance finance = Finance.builder()
                .reportName("Yearly Financial Report")
                .reportDate(LocalDate.now())
                .receivables(receivables)
                .payables(payables)
                .totalReceivables(totalReceivables)
                .totalPayables(totalPayables)
                .profitOrLoss(profitOrLoss)
                .build();

        generateAndSavePdfReport(finance, "yearly-report-");

        return finance;
    }


    private void generateAndSavePdfReport(Finance finance, String fileName) {
        FinanceDetailsResponse financeDetails = new FinanceDetailsResponse(finance);
        try {
            pdfReportGenerator.generateReportPdf(financeDetails, fileName + LocalDateTime.now() + ".pdf");
        } catch (IOException | DocumentException e) {
            throw new PdfReportGenerationException("Error generating PDF report");
        }
    }
}
