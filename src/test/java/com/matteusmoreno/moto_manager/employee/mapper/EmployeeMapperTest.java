package com.matteusmoreno.moto_manager.employee.mapper;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.customer.request.CreateCustomerRequest;
import com.matteusmoreno.moto_manager.employee.constant.EmployeeRole;
import com.matteusmoreno.moto_manager.employee.entity.Employee;
import com.matteusmoreno.moto_manager.employee.request.CreateEmployeeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Employee Mapper Tests")
class EmployeeMapperTest {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should map CreateEmployeeRequest to Employee correctly")
    void shouldMapCreateEmployeeRequestToEmployeeCorrectly() {
        Address address = new Address(1L, "28994-675", "Street", "Neighborhood", "City",
                "State", "21", "Casa", LocalDateTime.now());

        CreateEmployeeRequest request = new CreateEmployeeRequest("username", "password", "Employee", "employee@email.com", "(99)999999999", "(99)999999999", LocalDate.of(2000, 2, 10),
                EmployeeRole.SELLER, "28994-675", "21", "Casa");

        Employee result = employeeMapper.mapToEmployeeForCreation(request, address);

        assertEquals(request.username(), result.getUsername());
        assertTrue(passwordEncoder.matches(request.password(), result.getPassword()));
        assertEquals(request.name(), result.getName());
        assertEquals(request.email(), result.getEmail());
        assertEquals(request.phone(), result.getPhone());
        assertEquals(request.birthDate(), result.getBirthDate());
        assertEquals(Period.between(request.birthDate(), LocalDate.now()).getYears(), result.getAge());
        assertEquals(request.cpf(), result.getCpf());
        assertEquals(request.role(), result.getRole());
        assertEquals(address, result.getAddress());
        assertNotNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
        assertNull(result.getDeletedAt());
        assertTrue(result.getActive());
    }
}