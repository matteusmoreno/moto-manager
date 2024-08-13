package com.matteusmoreno.moto_manager.customer.service;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.address.service.AddressService;
import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.customer.mapper.CustomerMapper;
import com.matteusmoreno.moto_manager.customer.repository.CustomerRepository;
import com.matteusmoreno.moto_manager.customer.request.CreateCustomerRequest;
import com.matteusmoreno.moto_manager.customer.request.UpdateCustomerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Customer Service Tests")
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private CustomerService customerService;

    private CreateCustomerRequest createRequest;
    private Customer customer;
    private Address address;
    private UUID uuid;

    @BeforeEach
    void setup() {
        uuid = UUID.randomUUID();

        address = new Address(1L, "28994-675", "Street", "Neighborhood", "City", "State", "21", "Casa", LocalDateTime.now());


        customer = new Customer(uuid, "Name", "email@email.com", LocalDate.of(1990, 8, 28), 33,
                "(99)999999999", List.of(address), null, LocalDateTime.now(), null, null, true);

        createRequest = new CreateCustomerRequest("Name", "email@email.com", LocalDate.of(1990, 8, 28),
                "(99)999999999", "28994-675", "21", "Casa");
    }

    @Test
    @DisplayName("Should create a new customer successfully")
    void shouldCreateANewCustomerSuccessfully() {

        when(addressService.createAddress(createRequest.zipcode(), createRequest.number(), createRequest.complement())).thenReturn(address);
        when(customerMapper.mapToCustomerForCreation(createRequest, address)).thenReturn(customer);

        Customer result = customerService.createCustomer(createRequest);

        verify(addressService, times(1)).createAddress(createRequest.zipcode(), createRequest.number(), createRequest.complement());
        verify(customerMapper, times(1)).mapToCustomerForCreation(createRequest, address);
        verify(customerRepository, times(1)).save(result);

        assertEquals(createRequest.name(), result.getName());
        assertEquals(createRequest.email(), result.getEmail());
        assertEquals(createRequest.birthDate(), result.getBirthDate());
        assertEquals(Period.between(createRequest.birthDate(), LocalDate.now()).getYears(), result.getAge());
        assertEquals(createRequest.phone(), result.getPhone());
        assertEquals(address, result.getAddresses().get(0));
        assertNull(result.getMotorcycles());
        assertNotNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
        assertNull(result.getDeletedAt());
        assertTrue(result.getActive());
    }

    @Test
    @DisplayName("Should update a customer successfully")
    void shouldUpdateACustomerSuccessfully() {
        UpdateCustomerRequest updateRequest = new UpdateCustomerRequest(uuid, "New name",
                "newemail@email.com", LocalDate.of(2000, 8, 2), "(55)555555555");

        when(customerRepository.findById(updateRequest.id())).thenReturn(Optional.ofNullable(customer));

        Customer result = customerService.updateCustomer(updateRequest);

        verify(customerRepository, times(1)).findById(updateRequest.id());
        verify(customerRepository, times(1)).save(result);

        assertEquals(updateRequest.name(), result.getName());
        assertEquals(updateRequest.email(), result.getEmail());
        assertEquals(updateRequest.birthDate(), result.getBirthDate());
        assertEquals(Period.between(updateRequest.birthDate(), LocalDate.now()).getYears(), result.getAge());
        assertEquals(updateRequest.phone(), result.getPhone());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    @DisplayName("Should disable a customer successfully")
    void shouldDisableACustomerSuccessfully() {

        when(customerRepository.findById(uuid)).thenReturn(Optional.ofNullable(customer));

        customerService.disableCustomer(uuid);

        verify(customerRepository, times(1)).findById(uuid);
        verify(customerRepository, times(1)).save(customer);

        assertFalse(customer.getActive());
        assertNotNull(customer.getDeletedAt());
    }

    @Test
    @DisplayName("Should enable a customer successfully")
    void shouldEnableACustomerSuccessfully() {

        when(customerRepository.findById(uuid)).thenReturn(Optional.ofNullable(customer));

        Customer result = customerService.enableCustomer(uuid);

        verify(customerRepository, times(1)).findById(uuid);
        verify(customerRepository, times(1)).save(result);

        assertTrue(result.getActive());
        assertNull(result.getDeletedAt());
        assertNotNull(result.getUpdatedAt());
    }
}