package com.matteusmoreno.moto_manager.employee.producer.employee_request;

import com.matteusmoreno.moto_manager.employee.entity.Employee;

public record EnableAndDisableEmailEmployeeRequest(
        String to,
        String id,
        String employeeName,
        String username,
        String cpf,
        String role) {

    public EnableAndDisableEmailEmployeeRequest(Employee employee) {
        this(
                employee.getEmail(),
                employee.getId().toString(),
                employee.getName(),
                employee.getUsername(),
                employee.getCpf(),
                employee.getRole().toString()
        );
    }
}
