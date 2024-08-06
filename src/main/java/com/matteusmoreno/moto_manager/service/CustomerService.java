package com.matteusmoreno.moto_manager.service;

import com.matteusmoreno.moto_manager.entity.Address;
import com.matteusmoreno.moto_manager.entity.Customer;
import com.matteusmoreno.moto_manager.mapper.CustomerMapper;
import com.matteusmoreno.moto_manager.repository.CustomerRepository;
import com.matteusmoreno.moto_manager.request.CreateCustomerRequest;
import com.matteusmoreno.moto_manager.response.CustomerDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Customer customer = customerMapper.setCustomerAttributes(request, address);

        customerRepository.save(customer);

        return customer;
    }

    public Page<CustomerDetailsResponse> findAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).map(CustomerDetailsResponse::new);
    }
}
