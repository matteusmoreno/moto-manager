package com.matteusmoreno.moto_manager.customer.mapper;

import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.customer.request.CreateCustomerRequest;
import com.matteusmoreno.moto_manager.address.entity.Address;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Component
public class CustomerMapper {

    public Customer mapToCustomerForCreation(CreateCustomerRequest request, Address address) {

        return Customer.builder()
                .name(request.name())
                .email(request.email())
                .birthDate(request.birthDate())
                .age(Period.between(request.birthDate(), LocalDate.now()).getYears())
                .phone(request.phone())
                .addresses(List.of(address))
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .active(true)
                .build();
    }
}
