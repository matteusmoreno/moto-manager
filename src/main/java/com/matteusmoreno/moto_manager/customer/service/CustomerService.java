package com.matteusmoreno.moto_manager.customer.service;

import com.matteusmoreno.moto_manager.address.service.AddressService;
import com.matteusmoreno.moto_manager.customer.request.MotorcycleCustomerRequest;
import com.matteusmoreno.moto_manager.customer.request.UpdateCustomerRequest;
import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.customer.mapper.CustomerMapper;
import com.matteusmoreno.moto_manager.customer.repository.CustomerRepository;
import com.matteusmoreno.moto_manager.customer.request.CreateCustomerRequest;
import com.matteusmoreno.moto_manager.customer.response.CustomerDetailsResponse;
import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.exception.AddressAlreadyAssignedToCustomerException;
import com.matteusmoreno.moto_manager.exception.MotorcycleAlreadyAssignedException;
import com.matteusmoreno.moto_manager.exception.MotorcycleNotOwnedByCustomerException;
import com.matteusmoreno.moto_manager.motorcycle.repository.MotorcycleRepository;
import com.matteusmoreno.moto_manager.address.request.AddressCustomerRequest;
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
    private final MotorcycleRepository motorcycleRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, AddressService addressService, MotorcycleRepository motorcycleRepository) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.addressService = addressService;
        this.motorcycleRepository = motorcycleRepository;
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

    @Transactional
    public Customer addMotorcycle(MotorcycleCustomerRequest request) {
        Motorcycle motorcycle = motorcycleRepository.findById(request.motorcycleId())
                .orElseThrow(() -> new EntityNotFoundException("Motorcycle not found"));

        if (motorcycle.getCustomer() != null) throw new MotorcycleAlreadyAssignedException();

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        motorcycle.setCustomer(customer);
        customer.getMotorcycles().add(motorcycle);

        customerRepository.save(customer);
        motorcycleRepository.save(motorcycle);

        return customer;
    }

    @Transactional
    public Customer removeMotorcycle(MotorcycleCustomerRequest request) {
        Motorcycle motorcycle = motorcycleRepository.findById(request.motorcycleId())
                .orElseThrow(() -> new EntityNotFoundException("Motorcycle not found"));

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        if (motorcycle.getCustomer() == null || !motorcycle.getCustomer().equals(customer)) throw new MotorcycleNotOwnedByCustomerException();

        motorcycle.setCustomer(null);
        customer.getMotorcycles().remove(motorcycle);

        customerRepository.save(customer);
        motorcycleRepository.save(motorcycle);

        return customer;
    }

    @Transactional
    public Customer addAddress(AddressCustomerRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Address address = addressService.createAddress(request.zipcode(), request.number(), request.complement());

        if (customer.getAddresses().stream().anyMatch(a -> a.equals(address))) throw new AddressAlreadyAssignedToCustomerException();

        customer.getAddresses().add(address);
        customerRepository.save(customer);

        return customer;
    }
}
