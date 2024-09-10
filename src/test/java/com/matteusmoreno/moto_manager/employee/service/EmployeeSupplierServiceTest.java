package com.matteusmoreno.moto_manager.employee.service;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.address.response.AddressDetailsResponse;
import com.matteusmoreno.moto_manager.address.service.AddressService;
import com.matteusmoreno.moto_manager.employee.constant.EmployeeRole;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.employee.producer.EmployeeProducer;
import com.matteusmoreno.moto_manager.employee.repository.EmployeeRepository;
import com.matteusmoreno.moto_manager.employee.request.CreateEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.request.UpdateEmployeeRequest;
import com.matteusmoreno.moto_manager.employee.response.EmployeeDetailsResponse;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Employee Service tests")
class EmployeeSupplierServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private EmployeeProducer employeeProducer;

    @InjectMocks
    private EmployeeService employeeService;

    private Address address;
    private Employee employee;
    private UUID employeeId;

    @BeforeEach
    void setup() {
        employeeId = UUID.randomUUID();
        address = new Address(1L, "28994-675", "Street", "Neighborhood", "City", "State", "21", "Casa", LocalDateTime.now());
        employee = new Employee(employeeId, "username", "password", "Employee", "employee@email.com", "(99)999999999", LocalDate.of(2000, 2, 10),
                Period.between(LocalDate.of(2000, 2, 10), LocalDate.now()).getYears(), "222.222.222-22", EmployeeRole.SELLER, address, LocalDateTime.now(), null, null, true);

    }

    @Test
    @DisplayName("Should create a new employee successfully")
    void shouldCreateANewEmployeeSuccessfully() {

        CreateEmployeeRequest request = new CreateEmployeeRequest("username", "password", "Employee", "employee@email.com", "(99)999999999", "(99)999999999", LocalDate.of(2000, 2, 10), EmployeeRole.SELLER, "28994-675", "21", "Casa");

        when(addressService.createAddress(request.zipcode(), request.number(), request.complement())).thenReturn(address);
        when(passwordEncoder.encode(request.password())).thenReturn("EncodedPassword");

        Employee result = employeeService.createEmployee(request);

        verify(addressService, times(1)).createAddress(request.zipcode(), request.number(), request.complement());
        verify(employeeRepository, times(1)).save(result);
        verify(employeeProducer, times(1)).publishCreateEmployeeEmail(result, request);

        assertAll(
                () -> assertEquals(request.username(), result.getUsername()),
                () -> assertEquals("EncodedPassword", result.getPassword()),
                () -> assertEquals(request.name(), result.getName()),
                () -> assertEquals(request.email(), result.getEmail()),
                () -> assertEquals(request.phone(), result.getPhone()),
                () -> assertEquals(request.birthDate(), result.getBirthDate()),
                () -> assertEquals(Period.between(request.birthDate(), LocalDate.now()).getYears(), result.getAge()),
                () -> assertEquals(request.cpf(), result.getCpf()),
                () -> assertEquals(request.role(), result.getRole()),
                () -> assertEquals(address, result.getAddress()),
                () -> assertNotNull(result.getCreatedAt()),
                () -> assertNull(result.getUpdatedAt()),
                () -> assertNull(result.getDeletedAt()),
                () -> assertTrue(result.getActive())
        );
    }

    @Test
    @DisplayName("Should return a paginated list of employees")
    void shouldReturnAPaginatedListOfEmployees() {

        Page<Employee> employeePage = new PageImpl<>(Collections.singletonList(employee));
        Pageable pageable = Pageable.ofSize(10);

        when(employeeRepository.findAll(pageable)).thenReturn(employeePage);

        Page<EmployeeDetailsResponse> responsePage = employeeService.findAllEmployees(pageable);
        EmployeeDetailsResponse response = responsePage.getContent().get(0);

        verify(employeeRepository, times(1)).findAll(pageable);

        assertEquals(1, responsePage.getTotalElements());
        assertEquals(employee.getId(), response.id());
        assertEquals(employee.getUsername(), response.username());
        assertEquals(employee.getName(), response.name());
        assertEquals(employee.getEmail(), response.email());
        assertEquals(employee.getPhone(), response.phone());
        assertEquals(employee.getBirthDate(), response.birthDate());
        assertEquals(employee.getAge(), response.age());
        assertEquals(employee.getCpf(), response.cpf());
        assertEquals(employee.getRole().toString(), response.role());
        assertEquals(new AddressDetailsResponse(employee.getAddress()), response.address());
        assertEquals(employee.getCreatedAt(), response.createdAt());
        assertEquals(employee.getUpdatedAt(), response.updatedAt());
        assertEquals(employee.getDeletedAt(), response.deletedAt());
        assertEquals(employee.getActive(), response.active());
    }

    @Test
    @DisplayName("Should update an employee successfully")
    void shouldUpdateAnEmployeeSuccessfully() {
        UpdateEmployeeRequest request = new UpdateEmployeeRequest(employeeId, "New Name", "newemail@email.com",
                "(00)000000000", LocalDate.of(2010, 9, 20), "444.444.444-44");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.ofNullable(employee));

        Employee result = employeeService.updateEmployee(request);

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(result);
        verify(employeeProducer, times(1)).publishUpdateEmployeeEmail(result);

        assertEquals(employeeId, result.getId());
        assertEquals(employee.getUsername(), result.getUsername());
        assertEquals(request.name(), result.getName());
        assertEquals(request.email(), result.getEmail());
        assertEquals(request.phone(), result.getPhone());
        assertEquals(request.birthDate(), result.getBirthDate());
        assertEquals(Period.between(request.birthDate(), LocalDate.now()).getYears(), result.getAge());
        assertEquals(request.cpf(), result.getCpf());
        assertEquals(employee.getRole(), result.getRole());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException if the employee ID does not exist in the database")
    void shouldThrowEntityNotFoundExceptionIfEmployeeIdDoesNotExist() {
        UpdateEmployeeRequest request = new UpdateEmployeeRequest(employeeId, "New Name", "newemail@email.com",
                "(00)000000000", LocalDate.of(2010, 9, 20), "444.444.444-44");

        when(employeeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> employeeService.updateEmployee(request));

        verify(employeeRepository, times(1)).findById(any());
        verify(employeeRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Should disable an employee successfully")
    void shouldDisableAnEmployeeSuccessfully() {
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.ofNullable(employee));

        employeeService.disableEmployee(employeeId);

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(employee);
        verify(employeeProducer, times(1)).publishDisableEmployeeEmail(employee);

        assertFalse(employee.getActive());
        assertNotNull(employee.getDeletedAt());
    }

    @Test
    @DisplayName("Should enable an employee successfully")
    void shouldEnableAnEmployeeSuccessfully() {
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.ofNullable(employee));

        Employee result = employeeService.enableEmployee(employeeId);

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(employee);
        verify(employeeProducer, times(1)).publishEnableEmployeeEmail(employee);

        assertTrue(result.getActive());
        assertNotNull(result.getUpdatedAt());
        assertNull(result.getDeletedAt());
    }

}