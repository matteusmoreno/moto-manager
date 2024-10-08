package com.matteusmoreno.moto_manager.finance.receivable.controller;

import com.matteusmoreno.moto_manager.finance.receivable.entity.Receivable;
import com.matteusmoreno.moto_manager.finance.receivable.response.ReceivableDetailsResponse;
import com.matteusmoreno.moto_manager.finance.receivable.service.ReceivableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receivables")
public class ReceivableController {

    private final ReceivableService receivableService;

    @Autowired
    public ReceivableController(ReceivableService receivableService) {
        this.receivableService = receivableService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<Page<ReceivableDetailsResponse>> findAll(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(receivableService.findAllReceivables(pageable));
    }

    @PatchMapping("/pay/{id}")
    public ResponseEntity<ReceivableDetailsResponse> pay(@PathVariable Long id) {
        Receivable receivable = receivableService.payReceivable(id);

        return ResponseEntity.ok(new ReceivableDetailsResponse(receivable));
    }
}
