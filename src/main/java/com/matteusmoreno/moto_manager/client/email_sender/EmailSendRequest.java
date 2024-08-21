package com.matteusmoreno.moto_manager.client;

import com.matteusmoreno.moto_manager.employee.request.CreateEmployeeRequest;

public record EmailSendRequest(
        String to,
        String employeeName,
        String username,
        String phone,
        String birthDate,
        String cpf,
        String role) {

    public EmailSendRequest(CreateEmployeeRequest request) {
        this(
                request.email(),
                request.name(),
                request.username(),
                request.phone(),
                request.birthDate().toString(),
                request.cpf(),
                request.role().toString()
        );
    }
}
