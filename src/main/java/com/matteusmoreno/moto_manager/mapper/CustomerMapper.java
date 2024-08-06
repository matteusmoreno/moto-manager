package com.matteusmoreno.moto_manager.mapper;

import com.matteusmoreno.moto_manager.entity.Address;
import com.matteusmoreno.moto_manager.entity.Customer;
import com.matteusmoreno.moto_manager.request.CreateCustomerRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collections;
import java.util.List;

@Component
public class CustomerMapper {

    public Customer setCustomerAttributes(CreateCustomerRequest request, Address address) {

        List<Address> addresses = Collections.singletonList(address);

        return Customer.builder()
                .name(request.name())
                .email(request.email())
                .birthDate(request.birthDate())
                .age(Period.between(request.birthDate(), LocalDate.now()).getYears())
                .phone(request.phone())
                .address(addresses)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .active(true)
                .build();
    }
}
