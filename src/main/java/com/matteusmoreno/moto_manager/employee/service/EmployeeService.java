package com.matteusmoreno.moto_manager.employee.service;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.address.service.AddressService;
import com.matteusmoreno.moto_manager.client.email_sender.MailSenderClient;
import com.matteusmoreno.moto_manager.client.email_sender.employee_request.CreateEmailEmployeeRequest;
import com.matteusmoreno.moto_manager.client.email_sender.employee_request.EnableAndDisableEmailEmployeeRequest;
import com.matteusmoreno.moto_manager.client.email_sender.employee_request.UpdateEmailEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.employee.repository.EmployeeRepository;
import com.matteusmoreno.moto_manager.employee.request.CreateEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.request.UpdateEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.response.EmployeeDetailsResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AddressService addressService;
    private final MailSenderClient mailSenderClient;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, AddressService addressService, MailSenderClient mailSenderClient, BCryptPasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.addressService = addressService;
        this.mailSenderClient = mailSenderClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Employee createEmployee(CreateEmployeeRequest request) {
        Address address = addressService.createAddress(request.zipcode(), request.number(), request.complement());
        Employee employee = Employee.builder()
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

        employeeRepository.save(employee);
        return employee;
    }

    public Page<EmployeeDetailsResponse> findAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(EmployeeDetailsResponse::new);
    }

    @Transactional
    public Employee updateEmployee(UpdateEmployeeRequest request) {
        Employee employee = employeeRepository.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (request.name() != null) employee.setName(request.name());
        if (request.email() != null) employee.setEmail(request.email());
        if (request.phone() != null) employee.setPhone(request.phone());
        if (request.birthDate() != null) {
            employee.setBirthDate(request.birthDate());
            employee.setAge(Period.between(request.birthDate(), LocalDate.now()).getYears());
        }
        if (request.cpf() != null) employee.setCpf(request.cpf());

        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);
        mailSenderClient.employeeUpdateEmail(new UpdateEmailEmployeeRequest(employee));

        return employee;
    }

    @Transactional
    public void disableEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        employee.setActive(false);
        employee.setDeletedAt(LocalDateTime.now());
        employeeRepository.save(employee);
        mailSenderClient.employeeDisableEmail(new EnableAndDisableEmailEmployeeRequest(employee));
    }

    @Transactional
    public Employee enableEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        employee.setActive(true);
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setDeletedAt(null);
        employeeRepository.save(employee);
        mailSenderClient.employeeEnableEmail(new EnableAndDisableEmailEmployeeRequest(employee));

        return employee;
    }
}
