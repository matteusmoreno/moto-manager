package com.matteusmoreno.moto_manager.customer.producer.customer_request;

import com.matteusmoreno.moto_manager.customer.entity.Customer;

public record EnableAndDisableEmailCustomerRequest(
        String to,
        String id,
        String customerName,
        String email,
        String birthDate,
        String age,
        String phone) {

    public EnableAndDisableEmailCustomerRequest(Customer customer) {
        this(
                customer.getEmail(),
                customer.getId().toString(),
                customer.getName(),
                customer.getEmail(),
                customer.getBirthDate().toString(),
                customer.getAge().toString(),
                customer.getPhone()
        );
    }
}
