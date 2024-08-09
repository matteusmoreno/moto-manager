package com.matteusmoreno.moto_manager.employee.request;

import com.matteusmoreno.moto_manager.employee.constant.EmployeeRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record CreateEmployeeRequest(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        String password,
        @NotBlank(message = "Name is required")
        String name,
        @Email(message = "Invalid email")
        String email,
        @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "Invalid CPF: XXX.XXX.XXX-XX")
        String cpf,
        @Pattern(regexp = "^\\(\\d{2}\\)\\d{9}$", message = "Invalid phone format (xx)xxxxxxxxx")
        String phone,
        LocalDate birthDate,
        EmployeeRole employeeRole,
        @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "Invalid zipcode format (xxxxx-xxx)")
        String zipcode,
        String number,
        String complement) {
}
