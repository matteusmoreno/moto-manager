package com.matteusmoreno.moto_manager.finance.controller;

import com.matteusmoreno.moto_manager.finance.entity.Finance;
import com.matteusmoreno.moto_manager.finance.service.FinanceService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/finance")
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping("/weekly-report/download")
    public ResponseEntity<Resource> downloadWeeklyReport() throws IOException {
        Finance finance = financeService.generateWeeklyReport();
        Resource pdfResource = financeService.generateAndGetPdfReport(finance, "weekly-report-");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfResource.getFilename());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfResource.contentLength())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfResource);
    }

    @GetMapping("/monthly-report")
    public ResponseEntity<Resource> monthlyReport(@RequestParam Integer year, @RequestParam Integer month) throws IOException{
        Finance finance = financeService.generateMonthlyReport(year, month);
        Resource pdfResource = financeService.generateAndGetPdfReport(finance, "monthly-report-");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfResource.getFilename());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfResource.contentLength())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfResource);
    }

    @GetMapping("/yearly-report")
    public ResponseEntity<Resource> yearlyReport(@RequestParam Integer year) throws IOException {
        Finance finance = financeService.generateYearlyReport(year);
        Resource pdfResource = financeService.generateAndGetPdfReport(finance, "yearly-report-");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfResource.getFilename());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfResource.contentLength())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfResource);


    }
}
