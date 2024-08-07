package com.matteusmoreno.moto_manager.service;

import com.matteusmoreno.moto_manager.entity.Address;
import com.matteusmoreno.moto_manager.entity.Customer;
import com.matteusmoreno.moto_manager.mapper.CustomerMapper;
import com.matteusmoreno.moto_manager.repository.CustomerRepository;
import com.matteusmoreno.moto_manager.request.CreateCustomerRequest;
import com.matteusmoreno.moto_manager.request.UpdateCustomerRequest;
import com.matteusmoreno.moto_manager.response.CustomerDetailsResponse;
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
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressService addressService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, AddressService addressService) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.addressService = addressService;
    }

    @Transactional
    public Customer createCustomer(CreateCustomerRequest request) {

        Address address = addressService.createAddress(request.zipcode(), request.number(), request.complement());
        Customer customer = customerMapper.mapToCustomerForCreation(request, address);

        customerRepository.save(customer);

        return customer;
    }

    public Page<CustomerDetailsResponse> findAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).map(CustomerDetailsResponse::new);
    }

    @Transactional
    public Customer updateCustomer(UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        if (request.name() != null) customer.setName(request.name());
        if (request.email() != null) customer.setEmail(request.email());
        if (request.birthDate() != null) {
            customer.setBirthDate(request.birthDate());
            customer.setAge(Period.between(request.birthDate(), LocalDate.now()).getYears());
        }
        if (request.phone() != null) customer.setPhone(request.phone());

        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);

        return customer;
    }

    @Transactional
    public void disableCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        customer.setActive(false);
        customer.setDeletedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    @Transactional
    public Customer enableCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        customer.setActive(true);
        customer.setDeletedAt(null);
        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);

        return customer;
    }
}
