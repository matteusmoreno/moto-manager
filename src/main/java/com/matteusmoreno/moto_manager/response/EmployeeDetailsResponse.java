package com.matteusmoreno.moto_manager.response;

import com.matteusmoreno.moto_manager.entity.Employee;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record EmployeeDetailsResponse(
        UUID id,
        String username,
        String password,
        String name,
        String email,
        String phone,
        LocalDate birthDate,
        Integer age,
        String cpf,
        String role,
        AddressDetailsResponse address,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        Boolean active) {

    public EmployeeDetailsResponse(Employee employee) {
        this(
                employee.getId(),
                employee.getUsername(),
                employee.getPassword(),
                employee.getName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getBirthDate(),
                employee.getAge(),
                employee.getCpf(),
                employee.getRole().name(),
                new AddressDetailsResponse(employee.getAddress()),
                employee.getCreatedAt(),
                employee.getUpdatedAt(),
                employee.getDeletedAt(),
                employee.getActive());
    }
}

