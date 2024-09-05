package com.matteusmoreno.moto_manager.customer.response;

import com.matteusmoreno.moto_manager.customer.entity.Customer;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleBrand;
import com.matteusmoreno.moto_manager.motorcycle.constant.MotorcycleColor;
import com.matteusmoreno.moto_manager.motorcycle.entity.Motorcycle;
import com.matteusmoreno.moto_manager.motorcycle.response.MotorcycleDetailsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Customer Motorcycles Response Tests")
class CustomerMotorcyclesResponseTest {

    @Test
    @DisplayName("Should return a Customer Motorcycles Response successfully")
    void shouldReturnCustomerMotorcyclesResponseSuccessfully() {
        Customer customer = new Customer(UUID.randomUUID(), "Name", "email@email.com", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "(99)999999999", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), null, null, true);
        Motorcycle motorcycle = new Motorcycle(UUID.randomUUID(), MotorcycleBrand.HONDA, "Biz 100", MotorcycleColor.RED, "FRP7898", "2010/2011", null, LocalDateTime.now(), null, null, true);
        customer.getMotorcycles().add(motorcycle);

        CustomerMotorcyclesResponse result = new CustomerMotorcyclesResponse(customer);

        assertAll(
            () -> assertEquals(customer.getId(), result.id()),
            () -> assertEquals(customer.getName(), result.name()),
            () -> assertEquals(customer.getEmail(), result.email()),
            () -> assertEquals(customer.getPhone(), result.phone()),
            () -> assertEquals(new MotorcycleDetailsResponse(motorcycle), result.motorcycles().get(0))
        );
    }

}