package com.matteusmoreno.moto_manager.client.email_sender.employee_request;

import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.employee.request.CreateEmployeeRequest;

public record CreateEmailEmployeeRequest(
        String to,
        String id,
        String employeeName,
        String username,
        String password,
        String phone,
        String birthDate,
        String cpf,
        String role,
        String street,
        String number,
        String neighborhood,
        String city,
        String state,
        String zipcode,
        String complement) {

    public CreateEmailEmployeeRequest(Employee employee, CreateEmployeeRequest request) {
        this(
                employee.getEmail(),
                employee.getId().toString(),
                employee.getName(),
                employee.getUsername(),
                request.password(),
                employee.getPhone(),
                employee.getBirthDate().toString(),
                employee.getCpf(),
                employee.getRole().toString(),
                employee.getAddress().getStreet(),
                employee.getAddress().getNumber(),
                employee.getAddress().getNeighborhood(),
                employee.getAddress().getCity(),
                employee.getAddress().getState(),
                employee.getAddress().getZipcode(),
                employee.getAddress().getComplement()
        );
    }
}
