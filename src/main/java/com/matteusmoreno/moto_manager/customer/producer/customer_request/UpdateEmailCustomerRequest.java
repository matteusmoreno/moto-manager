package com.matteusmoreno.moto_manager.customer.producer.customer_request;

import com.matteusmoreno.moto_manager.customer.entity.Customer;

public record UpdateEmailCustomerRequest(
        String to,
        String id,
        String customerName,
        String email,
        String birthDate,
        String age,
        String phone,
        String createdAt,
        String updatedAt) {

    public UpdateEmailCustomerRequest(Customer customer) {
        this(
                customer.getEmail(),
                customer.getId().toString(),
                customer.getName(),
                customer.getEmail(),
                customer.getBirthDate().toString(),
                customer.getAge().toString(),
                customer.getPhone(),
                customer.getCreatedAt().toLocalDate().toString(),
                customer.getUpdatedAt().toLocalDate().toString()
        );
    }
}
