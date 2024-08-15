package com.matteusmoreno.moto_manager.customer.response;

import com.matteusmoreno.moto_manager.address.response.AddressDetailsResponse;
import com.matteusmoreno.moto_manager.customer.entity.Customer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CustomerDetailsResponse(
        UUID id,
        String name,
        LocalDate birthDate,
        Integer age,
        String email,
        String phone,
        List<AddressDetailsResponse> addresses,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean active) {

    public CustomerDetailsResponse(Customer customer) {
        this(
                customer.getId(),
                customer.getName(),
                customer.getBirthDate(),
                customer.getAge(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddresses().stream().map(AddressDetailsResponse::new).toList(),
                customer.getCreatedAt(),
                customer.getUpdatedAt(),
                customer.getDeletedAt(),
                customer.getActive());
    }
}
