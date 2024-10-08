package com.matteusmoreno.moto_manager.customer.controller;

import com.matteusmoreno.moto_manager.address.request.AddressCustomerRequest;
import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.customer.request.CreateCustomerRequest;
import com.matteusmoreno.moto_manager.customer.request.MotorcycleCustomerRequest;
import com.matteusmoreno.moto_manager.customer.request.RemoveCustomerAddressRequest;
import com.matteusmoreno.moto_manager.customer.request.UpdateCustomerRequest;
import com.matteusmoreno.moto_manager.customer.response.CustomerDetailsResponse;
import com.matteusmoreno.moto_manager.customer.response.CustomerMotorcyclesResponse;
import com.matteusmoreno.moto_manager.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerDetailsResponse> create(@RequestBody @Valid CreateCustomerRequest request, UriComponentsBuilder uriBuilder) {
        Customer customer = customerService.createCustomer(request);
        URI uri = uriBuilder.path("/customers/create/{id}").buildAndExpand(customer.getId()).toUri();

        return ResponseEntity.created(uri).body(new CustomerDetailsResponse(customer));
    }

    @GetMapping("/find-all")
    public ResponseEntity<Page<CustomerDetailsResponse>> findAllCustomers(@PageableDefault(sort = "name", size = 10) Pageable pageable) {
        Page<CustomerDetailsResponse> page = customerService.findAllCustomers(pageable);

        return ResponseEntity.ok(page);
    }

    @PutMapping("/update")
    public ResponseEntity<CustomerDetailsResponse> update(@RequestBody @Valid UpdateCustomerRequest request) {
        Customer customer = customerService.updateCustomer(request);

        return ResponseEntity.ok(new CustomerDetailsResponse(customer));
    }

    @DeleteMapping("/disable/{id}")
    public ResponseEntity<Void> disable(@PathVariable UUID id) {
        customerService.disableCustomer(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/enable/{id}")
    public ResponseEntity<CustomerDetailsResponse> enable(@PathVariable UUID id) {
        Customer customer = customerService.enableCustomer(id);

        return ResponseEntity.ok(new CustomerDetailsResponse(customer));
    }

    @PostMapping("/add-motorcycle")
    public ResponseEntity<CustomerMotorcyclesResponse> addMotorcycle(@RequestBody @Valid MotorcycleCustomerRequest request, UriComponentsBuilder uriBuilder) {
        Customer customer = customerService.addMotorcycle(request);
        URI uri = uriBuilder.path("/customers/add-motorcycle/{id}").buildAndExpand(customer.getId()).toUri();

        return ResponseEntity.created(uri).body(new CustomerMotorcyclesResponse(customer));
    }

    @DeleteMapping("/remove-motorcycle")
    public ResponseEntity<CustomerMotorcyclesResponse> removeMotorcycle(@RequestBody @Valid MotorcycleCustomerRequest request) {
        Customer customer = customerService.removeMotorcycle(request);

        return ResponseEntity.ok(new CustomerMotorcyclesResponse(customer));
    }

    @PostMapping("/add-address")
    public ResponseEntity<CustomerDetailsResponse> addAddress(@RequestBody @Valid AddressCustomerRequest request, UriComponentsBuilder uriBuilder) {
        Customer customer = customerService.addAddress(request);
        URI uri = uriBuilder.path("/customers/add-address/{id}").buildAndExpand(customer.getId()).toUri();

        return ResponseEntity.created(uri).body(new CustomerDetailsResponse(customer));
    }

    @DeleteMapping("/remove-address")
    public ResponseEntity<CustomerDetailsResponse> removeAddress(@RequestBody @Valid RemoveCustomerAddressRequest request) {
        Customer customer = customerService.removeAddress(request);

        return ResponseEntity.ok(new CustomerDetailsResponse(customer));
    }

}
