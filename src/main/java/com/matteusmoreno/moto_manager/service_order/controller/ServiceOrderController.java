package com.matteusmoreno.moto_manager.service_order.controller;

import com.matteusmoreno.moto_manager.service_order.entity.ServiceOrder;
import com.matteusmoreno.moto_manager.service_order.request.CreateServiceOrderRequest;
import com.matteusmoreno.moto_manager.service_order.request.UpdateServiceOrderRequest;
import com.matteusmoreno.moto_manager.service_order.response.ServiceOrderResponse;
import com.matteusmoreno.moto_manager.service_order.service.ServiceOrderService;
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

    @PatchMapping("/complete/{id}")
    public ResponseEntity<ServiceOrderResponse> complete(@PathVariable Long id) {
        ServiceOrder serviceOrder = serviceOrderService.completeServiceOrder(id);

        return ResponseEntity.ok(new ServiceOrderResponse(serviceOrder));
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<ServiceOrderResponse> cancel(@PathVariable Long id) {
        ServiceOrder serviceOrder = serviceOrderService.cancelServiceOrder(id);

        return ResponseEntity.ok(new ServiceOrderResponse(serviceOrder));
    }

    @PutMapping("/update")
    public ResponseEntity<ServiceOrderResponse> update(@RequestBody @Valid UpdateServiceOrderRequest request) {
        ServiceOrder serviceOrder = serviceOrderService.updateServiceOrder(request);

        return ResponseEntity.ok(new ServiceOrderResponse(serviceOrder));
    }
}
