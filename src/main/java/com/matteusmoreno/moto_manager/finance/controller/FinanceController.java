package com.matteusmoreno.moto_manager.finance.controller;

import com.matteusmoreno.moto_manager.finance.response.FinanceDetailsResponse;
import com.matteusmoreno.moto_manager.finance.service.FinanceService;
import com.matteusmoreno.moto_manager.finance.entity.Finance;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping("/weekly-report")
    public ResponseEntity<FinanceDetailsResponse> weeklyReport() {
        Finance finance = financeService.generateWeeklyReport();
        return ResponseEntity.ok(new FinanceDetailsResponse(finance));
    }
}
