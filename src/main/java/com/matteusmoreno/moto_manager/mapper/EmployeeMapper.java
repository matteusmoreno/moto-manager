package com.matteusmoreno.moto_manager.mapper;

import com.matteusmoreno.moto_manager.entity.Address;
import com.matteusmoreno.moto_manager.entity.Employee;
import com.matteusmoreno.moto_manager.request.CreateEmployeeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Component
public class EmployeeMapper {

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeMapper(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Employee setEmployeeAttributes(CreateEmployeeRequest request, Address address) {
        return Employee.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .birthDate(request.birthDate())
                .age(Period.between(request.birthDate(), LocalDate.now()).getYears())
                .cpf(request.cpf())
                .role(request.role())
                .address(address)
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .active(true)
                .build();
    }
}
