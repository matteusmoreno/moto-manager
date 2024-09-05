package com.matteusmoreno.moto_manager.customer.service;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.address.repository.AddressRepository;
import com.matteusmoreno.moto_manager.address.request.AddressCustomerRequest;
import com.matteusmoreno.moto_manager.address.service.AddressService;
import com.matteusmoreno.moto_manager.client.email_sender.MailSenderClient;
import com.matteusmoreno.moto_manager.client.email_sender.customer_request.EnableAndDisableEmailCustomerRequest;
import com.matteusmoreno.moto_manager.client.email_sender.customer_request.UpdateEmailCustomerRequest;
import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.customer.repository.CustomerRepository;
import com.matteusmoreno.moto_manager.customer.request.CreateCustomerRequest;
import com.matteusmoreno.moto_manager.customer.request.MotorcycleCustomerRequest;
import com.matteusmoreno.moto_manager.customer.request.RemoveCustomerAddressRequest;
import com.matteusmoreno.moto_manager.customer.request.UpdateCustomerRequest;
import com.matteusmoreno.moto_manager.customer.response.CustomerDetailsResponse;
import com.matteusmoreno.moto_manager.exception.AddressAlreadyAssignedToCustomerException;
import com.matteusmoreno.moto_manager.exception.AddressNotOwnedByCustomerException;
import com.matteusmoreno.moto_manager.exception.MotorcycleAlreadyAssignedException;
import com.matteusmoreno.moto_manager.exception.MotorcycleNotOwnedByCustomerException;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.motorcycle.repository.MotorcycleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Customer Service Tests")
@ExtendWith(MockitoExtension.class)
class CustomerSupplierServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private MotorcycleRepository motorcycleRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private MailSenderClient mailSenderClient;

    @InjectMocks
    private CustomerService customerService;

    private CreateCustomerRequest createRequest;
    private Customer customer;
    private Address address;
    private UUID customerId;
    private Motorcycle motorcycle;
    private UUID motorcycleId;
    private MotorcycleCustomerRequest motorcycleCustomerRequest;

    @BeforeEach
    void setup() {
        customerId = UUID.randomUUID();
        motorcycleId = UUID.randomUUID();
        address = new Address(1L, "28994-675", "Street", "Neighborhood", "City", "State", "21", "Casa", LocalDateTime.now());
        customer = new Customer(customerId, "Name", "email@email.com", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "(99)999999999", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), null, null, true);

        customer.getAddresses().add(address);

        createRequest = new CreateCustomerRequest("Name", "email@email.com", LocalDate.of(1990, 8, 28), "(99)999999999", "28994-675", "21", "Casa");
        motorcycle = new Motorcycle(motorcycleId, MotorcycleBrand.HONDA, "Model", MotorcycleColor.BLACK, "KKK0000", "2012", null, LocalDateTime.now(), null, null, true);
        motorcycleCustomerRequest = new MotorcycleCustomerRequest(customerId, motorcycleId);
    }

    @Test
    @DisplayName("Should create a new customer successfully")
    void shouldCreateANewCustomerSuccessfully() {

        when(addressService.createAddress(createRequest.zipcode(), createRequest.number(), createRequest.complement())).thenReturn(address);

        Customer result = customerService.createCustomer(createRequest);

        verify(addressService, times(1)).createAddress(createRequest.zipcode(), createRequest.number(), createRequest.complement());
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
    @DisplayName("Should return a paginated list of customers")
    void shouldReturnAPaginatedListOfCustomers() {

        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(customer));
        Pageable pageable = Pageable.ofSize(10);

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        Page<CustomerDetailsResponse> responsePage = customerService.findAllCustomers(pageable);
        CustomerDetailsResponse response = responsePage.getContent().get(0);

        assertEquals(1, responsePage.getTotalElements());
        assertEquals(customer.getName(), response.name());
        assertEquals(customer.getEmail(), response.email());
        assertEquals(customer.getBirthDate(), response.birthDate());
        assertEquals(customer.getAge(), response.age());
        assertEquals(customer.getPhone(), response.phone());
        assertEquals(customer.getAddresses().get(0).getId(), response.addresses().get(0).id());
        assertEquals(customer.getAddresses().get(0).getZipcode(), response.addresses().get(0).zipcode());
        assertEquals(customer.getAddresses().get(0).getStreet(), response.addresses().get(0).street());
        assertEquals(customer.getAddresses().get(0).getNeighborhood(), response.addresses().get(0).neighborhood());
        assertEquals(customer.getAddresses().get(0).getCity(), response.addresses().get(0).city());
        assertEquals(customer.getAddresses().get(0).getState(), response.addresses().get(0).state());
        assertEquals(customer.getAddresses().get(0).getNumber(), response.addresses().get(0).number());
        assertEquals(customer.getAddresses().get(0).getComplement(), response.addresses().get(0).complement());
    }

    @Test
    @DisplayName("Should update a customer successfully")
    void shouldUpdateACustomerSuccessfully() {
        UpdateCustomerRequest updateRequest = new UpdateCustomerRequest(customerId, "New name",
                "newemail@email.com", LocalDate.of(2000, 8, 2), "(55)555555555");

        when(customerRepository.findById(updateRequest.id())).thenReturn(Optional.ofNullable(customer));

        Customer result = customerService.updateCustomer(updateRequest);


        UpdateEmailCustomerRequest expectedRequest = new UpdateEmailCustomerRequest(customer);
        verify(mailSenderClient, times(1)).customerUpdateEmail(expectedRequest);
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

        when(customerRepository.findById(customerId)).thenReturn(Optional.ofNullable(customer));

        customerService.disableCustomer(customerId);

        EnableAndDisableEmailCustomerRequest expectedRequest = new EnableAndDisableEmailCustomerRequest(customer);
        verify(mailSenderClient, times(1)).customerDisableEmail(expectedRequest);
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(customer);

        assertFalse(customer.getActive());
        assertNotNull(customer.getDeletedAt());
    }

    @Test
    @DisplayName("Should enable a customer successfully")
    void shouldEnableACustomerSuccessfully() {

        when(customerRepository.findById(customerId)).thenReturn(Optional.ofNullable(customer));

        Customer result = customerService.enableCustomer(customerId);

        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(result);

        assertTrue(result.getActive());
        assertNull(result.getDeletedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    @DisplayName("Should successfully add a motorcycle to a customer")
    void shouldAddMotorcycleToCustomerSuccessfully() {

        when(motorcycleRepository.findById(motorcycleCustomerRequest.motorcycleId())).thenReturn(Optional.ofNullable(motorcycle));
        when(customerRepository.findById(motorcycleCustomerRequest.customerId())).thenReturn(Optional.ofNullable(customer));

        Customer result = customerService.addMotorcycle(motorcycleCustomerRequest);

        verify(motorcycleRepository, times(1)).findById(motorcycleCustomerRequest.motorcycleId());
        verify(customerRepository, times(1)).findById(motorcycleCustomerRequest.customerId());
        verify(customerRepository, times(1)).save(result);
        verify(motorcycleRepository, times(1)).save(motorcycle);

        assertEquals(motorcycle, result.getMotorcycles().get(0));
    }

    @Test
    @DisplayName("Should throw MotorcycleAlreadyAssignedException when a motorcycle is already assigned to a customer")
    void shouldThrowMotorcycleAlreadyAssignedExceptionWhenMotorcycleIsAlreadyAssignedToACustomer() {
        Customer anotherCustomer = new Customer(UUID.randomUUID(), "Another Name", "another@email.com", LocalDate.of(1991, 9, 15), 32,
                "(88)88888888", List.of(address), new ArrayList<>(), LocalDateTime.now(), null, null, true);

        Motorcycle anotherMotorcycle = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "Another Model", MotorcycleColor.BLACK, "ERD2343", "2010",
                anotherCustomer, LocalDateTime.now(), null, null, true);

        anotherCustomer.getMotorcycles().add(anotherMotorcycle);

        MotorcycleCustomerRequest request = new MotorcycleCustomerRequest(anotherCustomer.getId(), anotherMotorcycle.getId());

        lenient().when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.of(anotherCustomer));
        lenient().when(motorcycleRepository.findById(any(UUID.class))).thenReturn(Optional.of(anotherMotorcycle));

        assertThrows(MotorcycleAlreadyAssignedException.class, () -> customerService.addMotorcycle(request));
    }

    @Test
    @DisplayName("Should successfully remove a motorcycle to a customer")
    void shouldRemoveMotorcycleToCustomerSuccessfully() {
        Customer anotherCustomer = new Customer(UUID.randomUUID(), "Another Name", "another@email.com", LocalDate.of(1991, 9, 15), 32,
                "(88)88888888", List.of(address), new ArrayList<>(), LocalDateTime.now(), null, null, true);

        Motorcycle anotherMotorcycle = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "Another Model", MotorcycleColor.BLACK, "ERD2343", "2010",
                anotherCustomer, LocalDateTime.now(), null, null, true);

        MotorcycleCustomerRequest request = new MotorcycleCustomerRequest(anotherCustomer.getId(), anotherMotorcycle.getId());

        when(customerRepository.findById(request.customerId())).thenReturn(Optional.of(anotherCustomer));
        when(motorcycleRepository.findById(request.motorcycleId())).thenReturn(Optional.of(anotherMotorcycle));

        Customer result = customerService.removeMotorcycle(request);

        verify(motorcycleRepository, times(1)).findById(request.motorcycleId());
        verify(customerRepository, times(1)).findById(request.customerId());
        verify(customerRepository, times(1)).save(result);
        verify(motorcycleRepository, times(1)).save(anotherMotorcycle);

        assertFalse(result.getMotorcycles().contains(anotherMotorcycle));
        assertNull(anotherMotorcycle.getCustomer());
    }

    @Test
    @DisplayName("Should return MotorcycleNotOwnedByCustomerException when the motorcycle is not owned by the customer")
    void shouldReturnMotorcycleNotOwnedByCustomerExceptionWhenMotorcycleIsNotOwnedByCustomer() {
        Customer anotherCustomer = new Customer(UUID.randomUUID(), "Another Name", "another@email.com", LocalDate.of(1991, 9, 15), 32,
                "(88)88888888", List.of(address), new ArrayList<>(), LocalDateTime.now(), null, null, true);

        Motorcycle anotherMotorcycle = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "Another Model", MotorcycleColor.BLACK, "ERD2343", "2010",
                anotherCustomer, LocalDateTime.now(), null, null, true);

        anotherCustomer.getMotorcycles().add(anotherMotorcycle);

        MotorcycleCustomerRequest request = new MotorcycleCustomerRequest(customerId, anotherMotorcycle.getId());

        when(customerRepository.findById(request.customerId())).thenReturn(Optional.of(customer));
        when(motorcycleRepository.findById(request.motorcycleId())).thenReturn(Optional.of(anotherMotorcycle));

        assertThrows(MotorcycleNotOwnedByCustomerException.class, () -> customerService.removeMotorcycle(request));

        assertEquals(anotherCustomer, anotherMotorcycle.getCustomer());
    }

    @Test
    @DisplayName("Should return MotorcycleNotOwnedByCustomerException when the motorcycle is not assigned to any customer")
    void shouldReturnMotorcycleNotOwnedByCustomerExceptionWhenMotorcycleIsNotAssignedToAnyCustomer() {
        Motorcycle anotherMotorcycle = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "Another Model", MotorcycleColor.BLACK, "ERD2343", "2010",
                null, LocalDateTime.now(), null, null, true);

        MotorcycleCustomerRequest request = new MotorcycleCustomerRequest(customerId, anotherMotorcycle.getId());

        when(customerRepository.findById(request.customerId())).thenReturn(Optional.of(customer));
        when(motorcycleRepository.findById(request.motorcycleId())).thenReturn(Optional.of(anotherMotorcycle));

        assertThrows(MotorcycleNotOwnedByCustomerException.class, () -> customerService.removeMotorcycle(request));

        assertNull(anotherMotorcycle.getCustomer());
    }

    @Test
    @DisplayName("Should successfully add a address to a customer")
    void shouldAddAddressToCustomerSuccessfully() {
        Address newAddress = new Address(2L, "28994-675", "Saquarema", "Bacaxá(Bacaxá)", "RJ",
                "Rua Alfredo Menezes", "223", "Armação Motos", LocalDateTime.now());
        AddressCustomerRequest request = new AddressCustomerRequest(customerId, "28994-675", "223", "Armação Motos");

        when(customerRepository.findById(request.customerId())).thenReturn(Optional.of(customer));
        when(addressService.createAddress(request.zipcode(), request.number(), request.complement())).thenReturn(newAddress);

        Customer result = customerService.addAddress(request);

        verify(customerRepository, times(1)).save(result);

        assertTrue(result.getAddresses().contains(newAddress));
        assertTrue(result.getAddresses().contains(address));
        assertEquals(2, result.getAddresses().size());
    }

    @Test
    @DisplayName("Should return AddressAlreadyAssignedToCustomerException when the address is already assigned to the customer")
    void shouldReturnAddressAlreadyAssignedToCustomerExceptionWhenAddressIsAlreadyAssignedToCustomer() {
        AddressCustomerRequest request = new AddressCustomerRequest(customerId, customer.getAddresses().get(0).getZipcode(), customer.getAddresses().get(0).getNumber(), "Armação Motos");

        when(customerRepository.findById(request.customerId())).thenReturn(Optional.of(customer));
        when(addressService.createAddress(request.zipcode(), request.number(), request.complement())).thenReturn(address);

        assertThrows(AddressAlreadyAssignedToCustomerException.class, () -> customerService.addAddress(request));

        verify(customerRepository, times(0)).save(customer);

        assertTrue(customer.getAddresses().contains(address));
    }

    @Test
    @DisplayName("Should successfully remove a address to a customer")
    void shouldRemoveAddressToCustomerSuccessfully() {
        RemoveCustomerAddressRequest request = new RemoveCustomerAddressRequest(customerId, address.getZipcode(), address.getNumber());

        when(customerRepository.findById(request.customerId())).thenReturn(Optional.ofNullable(customer));
        when(addressRepository.findByZipcodeAndNumber(request.zipcode(), request.number())).thenReturn(address);
        when(addressRepository.existsByZipcodeAndNumber(request.zipcode(), request.number())).thenReturn(true);

        Customer result = customerService.removeAddress(request);

        assertEquals(0, result.getAddresses().size());
        assertFalse(result.getAddresses().contains(address));
    }

    @Test
    @DisplayName("Should return EntityNotFoundException when the address not exists")
    void shouldReturnEntityNotFoundExceptionWhenTheAddressNotExists() {
        RemoveCustomerAddressRequest request = new RemoveCustomerAddressRequest(customerId, "00000-000", "10");

        when(addressRepository.existsByZipcodeAndNumber(request.zipcode(), request.number())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> customerService.removeAddress(request));
    }

    @Test
    @DisplayName("Should return AddressNotOwnedByCustomerException when customer is not linked to address")
    void shouldReturnAddressNotOwnedByCustomerExceptionWhenCustomerIsNotLinkedToAddress() {
        Address anotherAddress = new Address(2L, "28994-675", "Saquarema", "Bacaxá(Bacaxá)", "RJ",
                "Rua Alfredo Menezes", "223", "Armação Motos", LocalDateTime.now());

        RemoveCustomerAddressRequest request = new RemoveCustomerAddressRequest(customerId, anotherAddress.getZipcode(), anotherAddress.getNumber());

        when(addressRepository.existsByZipcodeAndNumber(request.zipcode(), request.number())).thenReturn(true);
        when(customerRepository.findById(request.customerId())).thenReturn(Optional.ofNullable(customer));
        when(addressRepository.findByZipcodeAndNumber(request.zipcode(), request.number())).thenReturn(anotherAddress);

        assertThrows(AddressNotOwnedByCustomerException.class, () -> customerService.removeAddress(request));

        assertFalse(customer.getAddresses().contains(anotherAddress));

    }
}