package com.matteusmoreno.moto_manager.customer.response;

import com.matteusmoreno.moto_manager.address.entity.Address;
import com.matteusmoreno.moto_manager.address.response.AddressDetailsResponse;
import com.matteusmoreno.moto_manager.customer.entity.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer Details Response Tests")
class CustomerDetailsResponseTest {

    @Test
    @DisplayName("Should return a Customer Details Response successfully")
    void shouldReturnCustomerDetailsResponseSuccessfully() {
        Address address = new Address(1L, "28994-675", "Street", "Neighborhood", "City", "State", "21", "Casa", LocalDateTime.now());
        Customer customer = new Customer(UUID.randomUUID(), "Name", "email@email.com", LocalDate.of(1990, 8, 28), Period.between(LocalDate.of(1990, 8, 28), LocalDate.now()).getYears(), "(99)999999999", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), null, null, true);
        customer.getAddresses().add(address);

        CustomerDetailsResponse result = new CustomerDetailsResponse(customer);

        assertAll(
            () -> assertEquals(customer.getId(), result.id()),
            () -> assertEquals(customer.getName(), result.name()),
            () -> assertEquals(customer.getBirthDate(), result.birthDate()),
            () -> assertEquals(customer.getAge(), result.age()),
            () -> assertEquals(customer.getEmail(), result.email()),
            () -> assertEquals(customer.getPhone(), result.phone()),
            () -> assertEquals(new AddressDetailsResponse(customer.getAddresses().get(0)), result.addresses().get(0)),
            () -> assertEquals(customer.getCreatedAt(), result.createdAt()),
            () -> assertEquals(customer.getUpdatedAt(), result.updatedAt()),
            () -> assertEquals(customer.getDeletedAt(), result.deletedAt()),
            () -> assertEquals(customer.getActive(), result.active())
        );

    }
}