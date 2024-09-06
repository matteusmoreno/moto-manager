package com.matteusmoreno.moto_manager.customer.producer.customer_request;

import com.matteusmoreno.moto_manager.customer.entity.Customer;

public record CreateEmailCustomerRequest(
        String to,
        String id,
        String customerName,
        String birthDate,
        String age,
        String phone,
        String createdAt) {

    public CreateEmailCustomerRequest(Customer customer) {
        this(
                customer.getEmail(),
                customer.getId().toString(),
                customer.getName(),
                customer.getBirthDate().toString(),
                customer.getAge().toString(),
                customer.getPhone(),
                customer.getCreatedAt().toLocalDate().toString()
        );
    }
}
