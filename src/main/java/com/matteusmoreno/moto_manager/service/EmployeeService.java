package com.matteusmoreno.moto_manager.service;

import com.matteusmoreno.moto_manager.entity.Address;
import com.matteusmoreno.moto_manager.entity.Employee;
import com.matteusmoreno.moto_manager.mapper.CustomerMapper;
import com.matteusmoreno.moto_manager.mapper.EmployeeMapper;
import com.matteusmoreno.moto_manager.repository.EmployeeRepository;
import com.matteusmoreno.moto_manager.request.CreateEmployeeRequest;
import com.matteusmoreno.moto_manager.response.EmployeeDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Employee employee = employeeMapper.setEmployeeAttributes(request, address);

        employeeRepository.save(employee);

        return employee;
    }

    public Page<EmployeeDetailsResponse> findAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable).map(EmployeeDetailsResponse::new);
    }
}
