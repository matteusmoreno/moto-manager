package com.matteusmoreno.moto_manager.serice_order.controller;

import com.matteusmoreno.moto_manager.serice_order.entity.ServiceOrder;
import com.matteusmoreno.moto_manager.serice_order.request.CreateServiceOrderRequest;
import com.matteusmoreno.moto_manager.serice_order.response.ServiceOrderResponse;
import com.matteusmoreno.moto_manager.serice_order.service.ServiceOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/service-orders")
public class ServiceOrderController {

    private final ServiceOrderService serviceOrderService;

    @Autowired
    public ServiceOrderController(ServiceOrderService serviceOrderService) {
        this.serviceOrderService = serviceOrderService;
    }

    @PostMapping("/create")
    public ResponseEntity<ServiceOrderResponse> create(@RequestBody @Valid CreateServiceOrderRequest request, UriComponentsBuilder uriBuilder) {
        ServiceOrder serviceOrder = serviceOrderService.createServiceOrder(request);
        URI uri = uriBuilder.path("/service-orders/{id}").buildAndExpand(serviceOrder.getId()).toUri();

        return ResponseEntity.created(uri).body(new ServiceOrderResponse(serviceOrder));
    }

    @PatchMapping("/start/{id}")
    public ResponseEntity<ServiceOrderResponse> start(@PathVariable Long id) {
        ServiceOrder serviceOrder = serviceOrderService.startServiceOrder(id);

        return ResponseEntity.ok(new ServiceOrderResponse(serviceOrder));
    }
}
