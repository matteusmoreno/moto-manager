package com.matteusmoreno.moto_manager.employee.service;

import com.matteusmoreno.moto_manager.address.service.AddressService;
import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.employee.mapper.EmployeeMapper;
import com.matteusmoreno.moto_manager.employee.repository.EmployeeRepository;
import com.matteusmoreno.moto_manager.employee.request.CreateEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.request.UpdateEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.response.EmployeeDetailsResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final AddressService addressService;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, AddressService addressService) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.addressService = addressService;
    }

    @Transactional
    public Employee createEmployee(CreateEmployeeRequest request) {
        Address address = addressService.createAddress(request.zipcode(), request.number(), request.complement());
        Employee employee = employeeMapper.mapToEmployeeForCreation(request, address);

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

        return employee;
    }

    @Transactional
    public void disableEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        employee.setActive(false);
        employee.setDeletedAt(LocalDateTime.now());
        employeeRepository.save(employee);
    }

    @Transactional
    public Employee enableEmployee(UUID id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        employee.setActive(true);
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setDeletedAt(null);
        employeeRepository.save(employee);

        return employee;
    }
}
