package com.matteusmoreno.moto_manager.payable;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
