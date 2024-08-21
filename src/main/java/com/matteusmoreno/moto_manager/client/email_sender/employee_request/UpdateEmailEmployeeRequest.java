package com.matteusmoreno.moto_manager.client.email_sender.employee_request;

import com.matteusmoreno.moto_manager.employee.entity.Employee;

public record UpdateEmailEmployeeRequest(
        String to,
        String id,
        String employeeName,
        String username,
        String phone,
        String birthDate,
        String age,
        String cpf,
        String role,
        String street,
        String number,
        String neighborhood,
        String city,
        String state,
        String zipcode,
        String complement) {

    public UpdateEmailEmployeeRequest(Employee employee) {
        this(
                employee.getEmail(),
                employee.getId().toString(),
                employee.getName(),
                employee.getUsername(),
                employee.getPhone(),
                employee.getBirthDate().toString(),
                employee.getAge().toString(),
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
