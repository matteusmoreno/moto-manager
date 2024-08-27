package com.matteusmoreno.moto_manager.finance.payable.controller;

import com.matteusmoreno.moto_manager.finance.payable.request.UpdatePayableRequest;
import com.matteusmoreno.moto_manager.finance.payable.response.PayableDetailsResponse;
import com.matteusmoreno.moto_manager.finance.payable.service.PayableService;
import com.matteusmoreno.moto_manager.finance.payable.entity.Payable;
import com.matteusmoreno.moto_manager.finance.payable.request.CreatePayableRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/payables")
public class PayableController {

    private final PayableService payableService;

    @Autowired
    public PayableController(PayableService payableService) {
        this.payableService = payableService;
    }

    @PostMapping("/create")
    public ResponseEntity<PayableDetailsResponse> create(@RequestBody @Valid CreatePayableRequest request, UriComponentsBuilder uriBuilder) {
        Payable payable = payableService.createPayable(request);
        URI uri = uriBuilder.path("/payables/{id}").buildAndExpand(payable.getId()).toUri();

        return ResponseEntity.created(uri).body(new PayableDetailsResponse(payable));
    }

    @GetMapping("/find-all")
    public ResponseEntity<Page<PayableDetailsResponse>> findAll(Pageable pageable) {
        Page<PayableDetailsResponse> page = payableService.findAllPayables(pageable);

        return ResponseEntity.ok(page);
    }

    @PutMapping("/update")
    public ResponseEntity<PayableDetailsResponse> update(@RequestBody @Valid UpdatePayableRequest request) {
        Payable payable = payableService.updatePayable(request);

        return ResponseEntity.ok(new PayableDetailsResponse(payable));
    }

    @PatchMapping("/pay/{id}")
    public ResponseEntity<PayableDetailsResponse> pay(@PathVariable Long id) {
        Payable payable = payableService.payPayable(id);

        return ResponseEntity.ok(new PayableDetailsResponse(payable));
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<PayableDetailsResponse> cancel(@PathVariable Long id) {
        Payable payable = payableService.cancelPayable(id);

        return ResponseEntity.ok(new PayableDetailsResponse(payable));
    }
}
