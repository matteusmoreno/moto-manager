package com.matteusmoreno.moto_manager.response;

import com.matteusmoreno.moto_manager.entity.Customer;

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
