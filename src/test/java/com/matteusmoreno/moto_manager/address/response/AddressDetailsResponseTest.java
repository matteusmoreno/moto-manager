package com.matteusmoreno.moto_manager.address.response;

import com.matteusmoreno.moto_manager.address.entity.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Address Details Response Tests")
class AddressDetailsResponseTest {

    @Test
    @DisplayName("Should return a Address Details Response successfully")
    void shouldReturnAddressDetailsResponseSuccessfully() {
        Address address = new Address(1L, "28994-675", "Street", "Neighborhood", "City", "State", "21", "Casa", LocalDateTime.now());

        AddressDetailsResponse result = new AddressDetailsResponse(address);

        assertAll(
            () -> assertEquals(address.getId(), result.id()),
            () -> assertEquals(address.getZipcode(), result.zipcode()),
            () -> assertEquals(address.getCity(), result.city()),
            () -> assertEquals(address.getNeighborhood(), result.neighborhood()),
            () -> assertEquals(address.getState(), result.state()),
            () -> assertEquals(address.getStreet(), result.street()),
            () -> assertEquals(address.getNumber(), result.number()),
            () -> assertEquals(address.getComplement(), result.complement()),
            () -> assertEquals(address.getCreatedAt(), result.createdAt())
        );
    }

}