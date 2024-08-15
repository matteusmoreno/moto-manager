package com.matteusmoreno.moto_manager.customer.mapper;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.customer.request.CreateCustomerRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Customer Mapper Tests")
class CustomerMapperTest {

    @Autowired
    private CustomerMapper customerMapper;

    @Test
    @DisplayName("Should map CreateCustomerRequest to Customer correctly")
    void shouldMapCreateCustomerRequestToCustomerCorrectly() {

        Address address = new Address(2L, "28994-675", "Saquarema", "Bacaxá(Bacaxá)", "RJ",
                "Rua Alfredo Menezes", "223", "Armação Motos", LocalDateTime.now());


        CreateCustomerRequest request = new CreateCustomerRequest("Customer name", "email@email.com", LocalDate.of(1990, 8, 28),
                "(22)222222222", "28994-675", "223", "Armação Motos");

        Customer result = customerMapper.mapToCustomerForCreation(request, address);

        assertEquals(request.name(), result.getName());
        assertEquals(request.email(), result.getEmail());
        assertEquals(request.birthDate(), result.getBirthDate());
        assertEquals(Period.between(LocalDate.now(), request.birthDate()).getYears(), result.getAge());
        assertEquals(request.phone(), result.getPhone());
        assertEquals(address, result.getAddresses().get(0));
        assertNotNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
        assertTrue(result.getActive());
    }
}