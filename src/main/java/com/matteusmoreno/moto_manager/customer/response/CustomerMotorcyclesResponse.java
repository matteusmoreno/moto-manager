package com.matteusmoreno.moto_manager.customer.response;

import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.motorcycle.response.MotorcycleDetailsResponse;

import java.util.List;
import java.util.UUID;

public record CustomerMotorcyclesResponse(
        UUID id,
        String name,
        String email,
        String phone,
        List<MotorcycleDetailsResponse> motorcycles) {

    public CustomerMotorcyclesResponse(Customer customer) {
        this(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getMotorcycles().stream().map(MotorcycleDetailsResponse::new).toList());
    }
}
